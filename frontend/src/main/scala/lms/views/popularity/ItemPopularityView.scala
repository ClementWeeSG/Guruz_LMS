package lms.views.popularity

import io.udash.bindings.TextInput
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.core.FinalView
import io.udash.css.CssView
import lms.models.{DataLoadingModel, ItemPopularity}
import io.udash.properties.model.ModelProperty
import org.scalajs.dom.Element
import scalatags.JsDom.all._
import scalatags.generic.Modifier

class ItemPopularityView(val presenter: ItemPopularityPagePresenter) extends FinalView with CssView {

  override def getTemplate: Modifier[Element] = div(BootstrapStyles.containerFluid)(
    dateSelector,
    div(BootstrapStyles.row)(PopularityBoard(presenter.popularItems))
  )

  private def dateSelector = {
    div(BootstrapStyles.row)(
      UdashInputGroup()(
        UdashInputGroup.addon("From"),
        UdashInputGroup.input(TextInput.debounced(presenter.startDate).render),
        UdashInputGroup.addon("to"),
        UdashInputGroup.input(TextInput.debounced(presenter.endDate).render)
      ).render
    ).render
  }
}