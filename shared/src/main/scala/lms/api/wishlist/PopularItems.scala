package lms.api.wishlist

import io.udash.rest._

import scala.concurrent.Future

@REST
trait PopularItems {
  @SkipRESTName
  def all(): Future[List[lms.models.wishlist.PopularItems.All]]

  @SkipRESTName
  def byLibrary(@URLPart lib: String): Future[List[lms.models.wishlist.PopularItems.ByLibrary]]
}
