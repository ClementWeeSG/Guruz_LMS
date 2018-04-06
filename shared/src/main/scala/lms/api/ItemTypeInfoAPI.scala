package lms.api

import io.udash.rest._
import lms.models.LibraryItemInfo

import scala.concurrent.Future

@REST
trait ItemTypeInfoAPI {
  @GET
  @RESTName("item-types")
  def types(): Future[List[String]]

  @GET
  @RESTName("series-items")
  def items(@URLPart itemType: String): Future[Seq[LibraryItemInfo]]
}

object DummyItemTypeInfoAPI extends ItemTypeInfoAPI {
  override def types(): Future[List[String]] = Future.successful(List("Audio", "Book", "Video", "Magazine"))

  override def items(itemType: String): Future[Seq[LibraryItemInfo]] = Future.successful(List.empty)
}
