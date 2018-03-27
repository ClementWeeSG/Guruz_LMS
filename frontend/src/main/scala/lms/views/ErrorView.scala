package io.udash.demos.rest.views

import io.udash._
import lms.routing._

object ErrorViewFactory extends StaticViewFactory[RoutingState](() => new ErrorView)

class ErrorView extends FinalView {
  import scalatags.JsDom.all._

  override def getTemplate: Modifier =
    h3("URL not found!").render
}