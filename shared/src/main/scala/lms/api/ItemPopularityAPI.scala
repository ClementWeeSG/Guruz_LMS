package lms.api

import io.udash.rest._
import lms.models.ItemPopularity

import scala.concurrent.Future

@REST
trait ItemPopularityAPI {
  @GET
  @SkipRESTName()
  def getTop3Books(start: String, end: String): Future[List[ItemPopularity]]
}

object DummyItemPopularityAPI extends ItemPopularityAPI {
  override def getTop3Books(start: String, end: String): Future[List[ItemPopularity]] = Future.successful(List(
    ItemPopularity("789345", "Harry Potter and the Dark Legacy", 32, 25),
    ItemPopularity("9084ddfdfdfdfdr", "Ready Player One", 24, 30),
    ItemPopularity("904888jkl3455", "Beyond Fury: My Time In The Trump White House", 15, 9)
  ))
}
