package lms.views.wishlist

import io.udash._
import io.udash.bootstrap._
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.bootstrap.navs.UdashNav
import io.udash.css.CssView
import io.udash.properties.seq.SeqProperty
import org.scalajs.dom.Event
import scalatags.JsDom.all._

class WishlistView(presenter: WishListPresenter) extends FinalView with CssView {

  case class Panel(override val title: String, override val content: WishListPanel) extends TabPanel

  private val panels: SeqProperty[Panel, CastableProperty[Panel]] = SeqProperty(Seq(
    Panel("Schools", new SchoolsPanel(presenter.schoolsLoadingModel)),
    Panel("Books", new ItemsPanel(presenter.itemsLoadingModel))))


  val selected = Property[Panel](panels.elemProperties.head.get)

  private def isActiveTab(panel: ReadableProperty[Panel]) = {
    panel.combine(selected) {
      case (cdate, tgt) => cdate.title == tgt.title
    }
  }

  override def getTemplate: Modifier = div(
    layoutRow(span(h3("School Visits: Check List"))),
    layoutRow(selector),
    layoutRow(innerPanel)
  )

  private def innerPanel: Modifier = {
    div(BootstrapStyles.row, BootstrapStyles.containerFluid)(
      UdashNav.tabs(justified = true)(panels)(
        elemFactory = (panel) => a(href := "", onclick :+= ((ev: Event) => {
          selected.set(panel.get)
          true
        }))(bind(panel.transform(_.title))).render,
        isActive = isActiveTab _
      ).render,
      div(BootstrapStyles.Well.well)(
        selected.transform(_.content.render(presenter.showingSpecific)).get
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
