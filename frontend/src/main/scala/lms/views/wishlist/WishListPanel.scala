package lms.views.wishlist

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import org.scalajs.dom.Node
import scalatags.JsDom.all._
import scalatags.JsDom._

abstract class WishListPanel(title: String) {
  def allPanel(): Seq[Node]

  def specificPanel(): Seq[Node]

  def render(showingSpecific: ReadableProperty[Boolean]): Modifier = {
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading(h2(title)),
      UdashPanel.body(
        div(
          produce(showingSpecific) { isSpecific =>
            if (isSpecific) specificPanel() else allPanel()
          }
        ).render
      )
    ).render
  }
}
