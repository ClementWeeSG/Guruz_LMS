package io.udash.demos.rest.views

import io.udash._
import lms.routing.IndexState

object ErrorViewFactory extends StaticViewFactory[IndexState.type](() => new ErrorView)

class ErrorView extends FinalView {
  import scalatags.JsDom.all._

  override def getTemplate: Modifier =
    h3("URL not found!").render
}