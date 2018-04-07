package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import lms.ApplicationContext
import lms.models._
import lms.routing.MemberInfoState
import org.scalajs.dom.html.Div
import scalatags.JsDom
import scalatags.JsDom.all._

class MemberInfoView(presenter: MemberInfoPagePresenter) extends FinalView with CssView with MemberInfoCreation {

  val details = presenter.info.subModel(_.memberDetails)
  val transactions = presenter.info.subSeq(_.transactions)
  val transactionsData = ModelProperty(new DataLoadingModel[BookTransactionDetails]())

  presenter.selectedCard.listen {
    cardId => ApplicationContext.applicationInstance.goTo(MemberInfoState(Option(cardId).filter(_.nonEmpty)))
  }

  override def getTemplate: Modifier = frag(
    layoutRow(MemberSelector),
    layoutRow(MemberDetailsPanel(details)),
    layoutRow(MemberTransactions(transactionsData))
  )

  private def layoutRow(modifiers: Modifier*): JsDom.TypedTag[Div] = div(BootstrapStyles.row)(modifiers)

  private def MemberSelector = {
    //val dropdown: JsDom.all.Modifier = UdashForm.select(presenter.selectedCard, presenter.members.get)
    UdashForm.inline(
      UdashForm.group(
        UdashInputGroup.addon("Find Card Id: "),
        UdashForm.select(presenter.selectedCard, presenter.members.get)
      )
    ).render
  }
}
