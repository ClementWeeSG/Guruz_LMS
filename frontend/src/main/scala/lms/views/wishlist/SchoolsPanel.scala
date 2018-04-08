package lms.views.wishlist

import org.scalajs.dom.Node

case class SchoolsPanel(model: SchoolsLoadingModel) extends WishListPanel("Schools Not Yet Visited") {
  override def allPanel(): Seq[Node] = ???

  override def specificPanel(): Seq[Node] = ???
}
