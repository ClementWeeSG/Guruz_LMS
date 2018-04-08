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

  val details = ModelProperty(new SingleLoadingModel[MemberDetails]())
  val transactions = ModelProperty(new DataLoadingModel[BookTransactionDetails]())
  val members = SeqProperty.empty[String]
  val selectedCard = Property.empty[String]

  LMSGlobal.memberAPI.selectCardNos() map {
    cards =>
      members.set("" :: cards)
  } recover {
    case t: Throwable =>
      members.append("<ERROR>")

  }

  override def handleState(state: MemberInfoState): Unit = {
    state.card match {
      case None =>
      //info.set(MemberInfo())
      case Some("<Error>") =>
      //info.subModel(_.memberDetails).subProp(_.memberName).set("<ERROR>")
      case Some(card) =>
        println(s"Member Info: Navigating to data for $card")
        selectedCard.set(card)
        println(s"Member Info: Loading Member Details for member: $card")
        loadMemberDetails(details, LMSGlobal.memberAPI.getMemberDetails(card))
        //println("Member Info: Loaded Member Details successfully")
        println(s"Member Info: Loading transactions for member: $card")
        loadTransactions(transactions, LMSGlobal.memberAPI.getMemberTransactions(card))

        }
  }

  def loadMemberDetails(loadingModel: ModelProperty[SingleLoadingModel[MemberDetails]], target: Future[MemberDetails]): Unit = {
    loadingModel.subProp(_.loaded).set(false)
    loadingModel.subProp(_.loadingText).set("Loading ...")

    target onComplete {
      case Success(elem) =>
        loadingModel.subProp(_.loaded).set(true)
        loadingModel.subProp(_.error).set(false)
        loadingModel.subProp(_.item).set(Some(elem))
        println("Member Info: Loaded Member Details successfully")
        println(s"Member Info retrieved: $elem")
      case Failure(ex) =>
        loadingModel.subProp(_.error).set(true)
        loadingModel.subProp(_.loadingText).set(s"Error: $ex")
        println(s"Member Info: Error in retrieval: $ex")
    }

  }

  def loadTransactions(loadingModel: ModelProperty[DataLoadingModel[BookTransactionDetails]], elements: Future[Seq[BookTransactionDetails]]): Unit = {
    loadingModel.subProp(_.loaded).set(false)
    loadingModel.subProp(_.loadingText).set("Loading ...")

    elements onComplete {
      case Success(elems) =>
        loadingModel.subProp(_.loaded).set(true)
        loadingModel.subSeq(_.elements).clear()
        loadingModel.subSeq(_.elements).set(elems)
      case Failure(ex) =>
        loadingModel.subProp(_.error).set(true)
        loadingModel.subProp(_.loadingText).set(s"Error: $ex")
    }
  }


  def createView() = new MemberInfoView(this)

  override def create(): (View, Presenter[MemberInfoState]) = (createView(), this)
}
