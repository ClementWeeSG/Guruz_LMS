package lms.views.items

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.css.CssView
import lms.ApplicationContext
import lms.routing.ItemTypeInfoState
import org.scalajs.dom.html.Div
import scalatags.JsDom
import scalatags.JsDom.all._

class ItemInfoView(presenter: ItemInfoPagePresenter) extends FinalView with CssView {

  presenter.selectedCategory.listen {
    cat => ApplicationContext.applicationInstance.goTo(ItemTypeInfoState(Option(cat).filter(_.nonEmpty)))
  }

  override def getTemplate: Modifier = frag(
    layoutRow(selector),
    layoutRow(ItemDetails(presenter.info))
  )

  private def layoutRow(modifiers: Modifier*): JsDom.TypedTag[Div] = div(BootstrapStyles.row)(modifiers)

  private def selector = {
    val dropdown: JsDom.all.Modifier = UdashForm.select(presenter.selectedCategory, presenter.categories.get)
    UdashForm.inline(
      UdashForm.group(
        UdashInputGroup.addon("Find Card Id: "),
        dropdown
      )
    ).render
  }
}
