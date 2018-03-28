package lms.views.memberinfo

import io.udash.FinalView
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import io.udash.properties.seq.SeqProperty
import io.udash.properties.single.CastableProperty
import lms.ApplicationContext
import lms.models.{BookTransactionDetails, MemberDetails}
import lms.routing.MemberInfoState
import scalatags.JsDom.all._

class MemberInfoView(presenter: MemberInfoPagePresenter) extends FinalView with CssView {

  val details: ModelProperty[MemberDetails] = presenter.info.subModel(_.memberDetails)
  val transactions: SeqProperty[BookTransactionDetails, CastableProperty[BookTransactionDetails]] = presenter.info.subSeq(_.transactions)

  presenter.selectedCard.listen {
    cardId => ApplicationContext.applicationInstance.goTo(MemberInfoState(Option(cardId)))
  }

  override def getTemplate: Modifier = frag(
    div(BootstrapStyles.row)(
      UdashForm.inline(
        UdashInputGroup.addon("Find Card Id: "),
        UdashForm.select(presenter.selectedCard, presenter.members.get)
      ).render
    ),
    div(BootstrapStyles.row)(),
    div(BootstrapStyles.row)()
  )
}
