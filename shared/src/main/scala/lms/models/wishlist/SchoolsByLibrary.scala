package lms.models.wishlist

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.ModelPropertyCreator

case class SchoolsByLibraryRow(library: String, school: String)

object SchoolsByLibraryRow extends HasGenCodec[SchoolsByLibraryRow] {
  implicit def modelPropertyCreator = ModelPropertyCreator.materialize[SchoolsByLibraryRow]
}


case class SchoolTag(school: String, addr: String)


object SchoolTag extends HasGenCodec[SchoolTag] {
  implicit def modelPropertyCreator = ModelPropertyCreator.materialize[SchoolTag]
}