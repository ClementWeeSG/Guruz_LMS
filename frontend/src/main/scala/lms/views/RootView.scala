package io.udash.demos.rest.views

import io.udash._
import io.udash.bootstrap.utils.{UdashIcons, UdashJumbotron}
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css._
import io.udash.demos.rest.views.components.Header
import lms.routing.RootState
import lms.views.components.GuruzSidebar
import scalatags.JsDom.tags2._

object RootViewFactory extends StaticViewFactory[RootState.type](() => new RootView)

class RootView extends ContainerView with CssView {
  import scalatags.JsDom.all._

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      GuruzSidebar.render(),
      contentHolder,
      script(
        " $(document).ready(function () {\n" +
          "                 $('#sidebarCollapse').on('click', function () {\n" +
          "                     $('#sidebar').toggleClass('active');\n" +
          "                 });\n" +
          "             });"
      )
    )
  ).render

  private def contentHolder = div(id := "content")(
    nav(BootstrapStyles.Navigation.nav, BootstrapStyles.Navigation.navbarDefault)(
      div(BootstrapStyles.containerFluid)(
        div(BootstrapStyles.Navigation.navbarHeader)(
          button(attr("type") := "button", id := "sidebarCollapse", cls := "btn btn-info navbar-btn")(
            i(UdashIcons.Glyphicon.alignJustify)
          )
        )
      )
    ),
    childViewContainer
  )

  override def getTemplate: Modifier = content
}