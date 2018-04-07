package lms.models

import com.avsystem.commons.serialization.HasGenCodec

case class LibraryItemInfo(series: String, title: String, order: Option[Int], numLibs: Int)

object LibraryItemInfo extends HasGenCodec[LibraryItemInfo]
