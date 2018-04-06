package io.udash.demos.rest.views.index

import io.udash._
import lms.routing.IndexState

object IndexViewFactory extends ViewFactory[IndexState.type] {
  override def create(): (View, Presenter[IndexState.type]) = (IndexView, IndexPresenter)
}
