package lms.views.wishlist

import io.udash._
import io.udash.bootstrap._
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.css.CssView
import scalatags.JsDom.all._

class WishlistView(presenter: WishListPresenter) extends FinalView with CssView {

  case class Panel(override val title: String, override val content: WishListPanel) extends TabPanel


  override def getTemplate: Modifier = div(
    layoutRow(span(h3("School Visits: Check List"))),
    layoutRow(selector),
    layoutRow(innerPanel)
  )

  private def innerPanel: Modifier = {
    div(BootstrapStyles.row, BootstrapStyles.containerFluid)(
      div(BootstrapStyles.row)(
        new SchoolsPanel(presenter).render(presenter)
      ),
      div(BootstrapStyles.row)(
        new ItemsPanel(presenter.itemsLoadingModel).render(presenter)
      )
    ).render
  }

  private def layoutRow(modifiers: Modifier*) = div(BootstrapStyles.row)(modifiers)

  private def selector = {
    produce(presenter.libraries) { cats =>
      UdashForm.inline(
        UdashInputGroup()(
          UdashInputGroup.addon("Filter by Library: "),
          UdashForm.select(presenter.selectedLibrary, cats)
        ).render
      ).render
    }

  }
}

trait TabPanel {
  val title: String
  val content: WishListPanel
}
