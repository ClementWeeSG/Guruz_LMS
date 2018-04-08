package lms.api.wishlist

import io.udash.rest._
import lms.models.wishlist.{SchoolTag, SchoolsByLibraryRow}

import scala.concurrent.Future

@REST
trait NeglectedSchools {
  @SkipRESTName
  def all(): Future[List[SchoolsByLibraryRow]]

  @SkipRESTName
  def byLibrary(@URLPart lib: String): Future[List[SchoolTag]]
}
