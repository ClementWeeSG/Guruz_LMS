package lms.views.items

import io.udash.properties.model.ModelProperty
import lms.models._
import lms.views._
import scalatags.JsDom.all._

object ItemDetails {
  def apply(itemModel: ModelProperty[DataLoadingModel[ItemInfo]]) = new ItemDetails(itemModel).render
}

class ItemDetails(items: ModelProperty[DataLoadingModel[ItemInfo]]) {
  def render() = DataTable[ItemInfo](
    items,
    Seq("Series", "Title", "Series Order", "Number of Libraries With Stock"),
    (prop) => {
      val data = prop.get
      Seq(
        data.series,
        data.title,
        data.order.map(_.toString).getOrElse("N/A"),
        data.numLibs
      )
    }
  )
}
