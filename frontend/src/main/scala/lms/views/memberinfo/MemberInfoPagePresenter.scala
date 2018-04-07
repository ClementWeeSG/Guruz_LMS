package lms.views.memberinfo

import io.udash._
import io.udash.core.{View, ViewFactory}
import io.udash.properties.model.ModelProperty
import io.udash.properties.seq.SeqProperty
import io.udash.properties.single.Property
import lms.api.LMSGlobal
import lms.models._
import lms.routing.MemberInfoState

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MemberInfoPagePresenter extends Presenter[MemberInfoState] with ViewFactory[MemberInfoState] with MemberInfoCreation {

  val info: ModelProperty[MemberInfo] = ModelProperty.empty[MemberInfo]
  val members = SeqProperty.empty[String]
  val selectedCard = Property.empty[String]

  MemberInfoUtils.getCards().map {
    cards => members.set(cards, true)
  } recover {
    case t: Throwable =>
      members.append("<ERROR>")

  }

  override def handleState(state: MemberInfoState): Unit = {
    state.card match {
      case None => info.set(MemberInfo())
      case Some("<Error>") =>
        info.subModel(_.memberDetails).subProp(_.memberName).set("<ERROR>")
      case Some(card) =>
        val loadingModel1 = ModelProperty.empty[SingleLoadingModel[MemberDetails]]
        loadingModel1.set(new SingleLoadingModel[MemberDetails](item = MemberDetails("", "", "N/A", 0)))
        loadMemberDetails(loadingModel1, LMSGlobal.memberAPI.getMemberDetails(card)) { _ =>
          val loadingModel2 = ModelProperty.empty[DataLoadingModel[BookTransactionDetails]]
          loadingModel2.set(new DataLoadingModel[BookTransactionDetails])
          loadTransactions(loadingModel2, LMSGlobal.memberAPI.getMemberTransactions(card))
        }
    }

    def loadMemberDetails(loadingModel: ModelProperty[SingleLoadingModel[MemberDetails]], target: Future[MemberDetails])(callback: MemberDetails => Unit): Unit = {
      loadingModel.subProp(_.loaded).set(false)
      loadingModel.subProp(_.loadingText).set("Loading ...")

      target onComplete {
        case Success(elem) =>
          loadingModel.subProp(_.loaded).set(true)
          loadingModel.subProp(_.item).set(elem)
          callback(elem)
        case Failure(ex) =>
          loadingModel.subProp(_.loadingText).set(s"Error: $ex")
      }

    }

    def loadTransactions(loadingModel: ModelProperty[DataLoadingModel[BookTransactionDetails]], elements: Future[Seq[BookTransactionDetails]]): Unit = {
      loadingModel.subProp(_.loaded).set(false)
      loadingModel.subProp(_.loadingText).set("Loading ...")

      elements onComplete {
        case Success(elems) =>
          loadingModel.subProp(_.loaded).set(true)
          loadingModel.subSeq(_.elements).clear()

          elems.foreach { el =>
            loadingModel.subSeq(_.elements).append(el)
          }
        case Failure(ex) =>
          loadingModel.subProp(_.error).set(true)
          loadingModel.subProp(_.loadingText).set(s"Error: $ex")
      }
    }
  }

  def createView() = new MemberInfoView(this)

  override def create(): (View, Presenter[MemberInfoState]) = (createView(), this)
}
