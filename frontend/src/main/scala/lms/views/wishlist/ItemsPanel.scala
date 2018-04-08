package lms.views.wishlist

import org.scalajs.dom.Node

case class ItemsPanel(model: ItemsLoadingModel) extends WishListPanel("Items Not Yet Brought For Visits") {
  override def allPanel(): Seq[Node] = ???

  override def specificPanel(): Seq[Node] = ???
}
