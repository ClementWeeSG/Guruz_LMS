package io.udash.demos.rest.views.index

import io.udash._
import lms.routing.IndexState

object IndexViewFactory extends ViewFactory[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = {
    val model = ModelProperty(new IndexViewModel())
    val presenter = new IndexPresenter(model)
    val view = new IndexView(model, presenter)
    (view, presenter)
  }
}
