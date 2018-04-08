package lms.models.wishlist

import com.avsystem.commons.serialization.{HasGenCodec, transparent}
import io.udash.properties.ModelPropertyCreator

case class SchoolsByLibraryRow(library: String, school: String)

object SchoolsByLibraryRow extends HasGenCodec[SchoolsByLibraryRow] {
  implicit def modelPropertyCreator = ModelPropertyCreator.materialize[SchoolsByLibraryRow]
}

@transparent
case class SchoolTag(value: String)

@transparent
object SchoolTag extends HasGenCodec[SchoolTag] {
  implicit def modelPropertyCreator = ModelPropertyCreator.materialize[SchoolTag]
}