package lms.views.wishlist

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import scalatags.JsDom.all._

abstract class WishListPanel(title: String) {
  def allPanel(): Modifier

  def specificPanel(): Modifier

  def render(showingSpecific: ReadableProperty[Boolean]): Modifier = {
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading(h2(title)),
      UdashPanel.body(
        div(
          produce(showingSpecific) { isSpecific =>
            if (isSpecific) div(specificPanel()).render else div(allPanel()).render
          }
        ).render
      )
    ).render
  }
}
