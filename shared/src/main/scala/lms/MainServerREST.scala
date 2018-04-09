package lms

import io.udash.rest._
import lms.api.{ItemPopularityAPI, ItemTypeInfoAPI, MemberInfoAPI, WishListAPI}

import scala.concurrent.Future

@REST
trait MainServerREST {
  @RESTName("members")
  def members(): MemberInfoAPI
  @RESTName("item-popularity")
  def popularity(): ItemPopularityAPI
  @SkipRESTName
  def series(): ItemTypeInfoAPI

  @GET
  @RESTName("libraries")
  def libraries(): Future[List[String]]

  @RESTName("wishlist")
  def wishlist(): WishListAPI
}

/**
  * object MainServerREST {
  * lazy val instance: MainServerREST = DefaultServerREST[MainServerREST](
  *Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  * )
  * }
  */