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
    layoutRow(h3("Search for Items by Type")),
    layoutRow(selector),
    layoutRow(ItemDetails(presenter.info))
  )

  private def layoutRow(modifiers: Modifier*): JsDom.TypedTag[Div] = div(BootstrapStyles.row)(modifiers)

  private def selector = {
    produce(presenter.categories){cats =>
      UdashForm.inline(
        UdashInputGroup()(
          UdashInputGroup.addon("Find Item Type: "),
          UdashForm.select(presenter.selectedCategory, cats)
        ).render
      ).render
    }

  }
}
