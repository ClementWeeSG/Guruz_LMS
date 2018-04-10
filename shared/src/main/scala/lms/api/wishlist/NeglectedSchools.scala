package lms.api.wishlist

import io.udash.rest._
import lms.models.wishlist.{SchoolTag}

import scala.concurrent.Future

@REST
trait NeglectedSchools {
  @SkipRESTName @GET
  def all(): Future[List[SchoolTag]]

  @SkipRESTName @GET
  def byLibrary(@URLPart lib: String): Future[List[SchoolTag]]
}
