package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.css.CssView
//import io.udash.properties.model.ModelProperty
import lms.ApplicationContext
import lms.models._
import lms.routing.MemberInfoState
import org.scalajs.dom.html.Div
import scalatags.JsDom
import scalatags.JsDom.all._

class MemberInfoView(presenter: MemberInfoPagePresenter) extends FinalView with CssView with MemberInfoCreation {

  presenter.selectedCard.listen {
    cardId => ApplicationContext.applicationInstance.goTo(MemberInfoState(Option(cardId).filter(_.nonEmpty)))
  }

  override def getTemplate: Modifier = frag(
    layoutRow(MemberSelector),
    layoutRow(MemberDetailsPanel(presenter.details)),
    layoutRow(MemberTransactions(presenter.transactions))
  )

  private def layoutRow(modifiers: Modifier*): JsDom.TypedTag[Div] = div(BootstrapStyles.row)(modifiers)

  private def MemberSelector = {
    produce(presenter.members) { mems =>
      UdashForm.inline(
        UdashInputGroup()(
          UdashInputGroup.addon("Find Member ID: "),
          UdashForm.select(presenter.selectedCard, mems)
        ).render
      ).render
    }
  }
}
