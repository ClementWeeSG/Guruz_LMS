package io.udash.demos.rest.views.index

import io.udash._
import lms.routing.IndexState

object DemoIndexViewFactory extends ViewFactory[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    val model = ModelProperty(new DemoIndexViewModel())
    val presenter = new DemoIndexPresenter(model)
    val view = new DemoIndexView(model, presenter)
    (view, presenter)
  }
}
