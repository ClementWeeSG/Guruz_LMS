package lms.api

import io.udash.rest.{GET, REST, RESTName, URLPart}
import lms.models.ItemInfo

import scala.concurrent.Future

@REST
trait ItemTypeInfoAPI {
  @GET
  @RESTName("item-types")
  def types(): Future[List[String]]

  @GET
  @RESTName("series-items")
  def items(@URLPart itemType: String): Future[List[ItemInfo]]
}

object DummyItemTypeInfoAPI extends ItemTypeInfoAPI {
  override def types(): Future[List[String]] = Future.successful(List("Audio", "Book", "Video", "Magazine"))

  override def items(itemType: String): Future[List[ItemInfo]] = Future.successful(List.empty)
}
