package io.udash.demos.rest.views

import io.udash._
import io.udash.bootstrap.utils.UdashJumbotron
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css._
import io.udash.demos.rest.RootState
import io.udash.demos.rest.views.components.Header

import scalatags.JsDom.tags2.main

object RootViewFactory extends StaticViewFactory[RootState.type](() => new RootView)

class RootView extends ContainerView with CssView {
  import scalatags.JsDom.all._

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("REST with Akka HTTP server"),
          p("Welcome in the Udash REST and the Udash Bootstrap modules demo!")
        ).render,
        childViewContainer
      )
    )
  ).render

  override def getTemplate: Modifier = content
}