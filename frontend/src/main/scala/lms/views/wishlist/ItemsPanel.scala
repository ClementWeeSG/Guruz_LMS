package lms.views.wishlist

import lms.models.wishlist.PopularItems._
import lms.views.DataTable
import scalatags.JsDom.all._

case class ItemsPanel(model: ItemsLoadingModel) extends WishListPanel("Books Not Yet Brought For Visits") {
  override def allPanel(): Modifier = DataTable[All](
    model.allModel,
    Seq("Library", "Item Title", "Series Title", "Series Order", "No. of Copies in Stock"),
    prop => {
      val data = prop.get
      Seq(
        (data.library),
        (data.title),
        (data.series.toOption.getOrElse("")),
        (data.order.toOption.map(_.toString).getOrElse("")),
        (data.numCopies.toString)
      ).map(span(_))
    })

  override def specificPanel(): Modifier = DataTable[ByLibrary](
    model.singleLibraryModel,
    Seq("Item Title", "Series Title", "Series Order", "No. of Copies in Stock"),
    prop => {
      val data = prop.get
      Seq(
        data.title,
        data.series.toOption.getOrElse(""),
        data.order.toOption.map(_.toString).getOrElse(""),
        data.numCopies.toString
      ).map(span(_))
    })
}
