package lms.models.wishlist

import com.avsystem.commons.serialization.HasGenCodec

case class SchoolsByLibraryRow(library: String, school: String)

object SchoolsByLibraryRow extends HasGenCodec[SchoolsByLibraryRow]