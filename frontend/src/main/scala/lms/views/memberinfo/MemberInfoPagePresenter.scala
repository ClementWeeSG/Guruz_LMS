package lms.views.memberinfo

import io.udash.Presenter
import io.udash.core.{View, ViewFactory}
import io.udash.properties.model.ModelProperty
import io.udash.properties.seq.SeqProperty
import io.udash.properties.single.Property
import lms.models.{MemberInfo, MemberInfoUtils}
import lms.routing.MemberInfoState

class MemberInfoPagePresenter extends Presenter[MemberInfoState] with ViewFactory[MemberInfoState] {

  val info: ModelProperty[MemberInfo] = ModelProperty(MemberInfo())
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
        MemberInfoUtils.getInfo(card).map {
          case Some(mInfo) => info.set(mInfo)
          case None =>
        }
    }
  }

  def createView() = new MemberInfoView(this)

  override def create(): (View, Presenter[MemberInfoState]) = (createView(), this)
}
