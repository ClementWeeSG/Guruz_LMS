package lms.models

import lms.models.ItemTypeInfo.Series

object ItemTypeInfo {

  case class Series(title: String, items: List[Item])

  case class Item(seriesOrder: Int, title: String, numLibs: Int)

}

case class ItemTypeInfo(itemType: String, series: List[Series])

case class ItemInfo(series: String, title: String, order: Option[Int], numLibs: Int)
