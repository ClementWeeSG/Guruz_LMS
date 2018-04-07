package lms.views.items

import io.udash.properties.model.ModelProperty
import lms.models._
import lms.views._
import scalatags.JsDom.all._

object ItemDetails {
  def apply(itemModel: ModelProperty[DataLoadingModel[LibraryItemInfo]]) = new ItemDetails(itemModel).render
}

class ItemDetails(items: ModelProperty[DataLoadingModel[LibraryItemInfo]]) {
  def render() = DataTable[LibraryItemInfo](
    items,
    Seq("Series Title", "Item Title", "Series Order", "Number of Libraries With Stock"),
    (prop) => {
      val data = prop.get
      val orderStr = data.order.map(_.toString).getOrElse("")
      Seq(
        data.series,
        data.title,
        orderStr,
        data.numLibs.toString
      )
    }
  )
}
