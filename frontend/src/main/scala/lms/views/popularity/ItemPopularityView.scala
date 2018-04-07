package lms.views.popularity

import io.udash.bindings.TextInput
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.UdashInputGroup
import io.udash.core.FinalView
import io.udash.css.CssView
import org.scalajs.dom.Element
import scalatags.JsDom.all._
import scalatags.generic.Modifier

class ItemPopularityView(val presenter: ItemPopularityPagePresenter) extends FinalView with CssView {

  override def getTemplate: Modifier[Element] = div(BootstrapStyles.containerFluid)(
    div(BootstrapStyles.row)(h3("Top Items Borrowed This Year")),
    dateSelector,
    div(BootstrapStyles.row)(PopularityBoard(presenter.popularItemsLoadingModel))
  )

  private def dateSelector = {
    div(BootstrapStyles.row)(
      UdashInputGroup()(
        UdashInputGroup.addon("Date From"),
        UdashInputGroup.input(TextInput.debounced(presenter.startDate).render),
        UdashInputGroup.addon("Date To"),
        UdashInputGroup.input(TextInput.debounced(presenter.endDate).render)
      ).render
    ).render
  }
}