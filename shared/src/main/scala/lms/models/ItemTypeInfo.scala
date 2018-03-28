package lms.models

import lms.models.ItemTypeInfo.Series

object ItemTypeInfo {

  case class Series(title: String, items: List[Item])

  case class Item(seriesOrder: Int, title: String)

}

case class ItemTypeInfo(itemType: String, series: List[Series])
