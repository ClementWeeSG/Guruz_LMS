package io.udash.demos.rest.views.index

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.css.CssView

object IndexView extends FinalView with CssView {
  import scalatags.JsDom.all._

  override def getTemplate: Modifier = div(BootstrapStyles.containerFluid) {
    div(BootstrapStyles.row)(span("Welcome to the Guruz Library Management. Please select one of the left-hand-side links to get started."))
  }
}