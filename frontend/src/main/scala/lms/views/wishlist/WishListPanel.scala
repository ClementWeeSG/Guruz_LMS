package lms.views.wishlist

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import scalatags.JsDom.all._

abstract class WishListPanel(title: String) {
  def allPanel(): Modifier

  def specificPanel(): Modifier

  def render(presenter: WishListPresenter): Modifier = {
    val hdr = presenter.selectedLibrary.transform(lib => title + " By " + lib.replace("%20"," "))
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading(h2(bind(hdr))),
      UdashPanel.body(
        div(
          produce(presenter.showingSpecific) { isSpecific =>
            if (isSpecific) div(specificPanel()).render else div(allPanel()).render
          }
        ).render
      )
    ).render
  }
}
