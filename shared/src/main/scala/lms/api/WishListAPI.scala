package lms.api

import io.udash.rest.{REST, RESTName}

@REST
trait WishListAPI {
  @RESTName("schools")
  def neglectedSchools(): wishlist.NeglectedSchools

  @RESTName("books")
  def popularItems(): wishlist.PopularItems
}
