package io.udash.demos.rest

import io.udash.demos.rest.model._
import io.udash.rest._
import io.udash.rpc.RPCName

import scala.concurrent.Future

@REST
trait MainServerREST {
  @RESTName("book")
  def phoneBooks(): PhoneBooksREST

  @RESTName("book") @RPCName("selectBook")
  def phoneBooks(@URLPart id: PhoneBookId): PhoneBookManagementREST

  @RESTName("contact")
  def contacts(): ContactsREST

  @RESTName("contact") @RPCName("selectContact")
  def contacts(@URLPart id: ContactId): ContactManagementREST
}

@REST
trait PhoneBooksREST {
  @GET @SkipRESTName @RPCName("loadAll")
  def load(): Future[Seq[PhoneBookInfo]]

  @POST @SkipRESTName
  def create(@Body book: PhoneBookInfo): Future[PhoneBookInfo]
}

@REST
trait PhoneBookManagementREST {
  @GET @SkipRESTName
  def load(): Future[PhoneBookInfo]

  @PUT @SkipRESTName
  def update(@Body book: PhoneBookInfo): Future[PhoneBookInfo]

  @DELETE @SkipRESTName
  def remove(): Future[PhoneBookInfo]

  def contacts(): PhoneBookContactsREST
}

@REST
trait PhoneBookContactsREST {
  @GET @SkipRESTName
  def load(): Future[Seq[ContactId]]

  @GET
  def count(): Future[Int]

  @POST @RESTName("manage")
  def add(@Body contactId: ContactId): Future[Unit]

  @DELETE @RESTName("manage")
  def remove(@Body contactId: ContactId): Future[Unit]
}

@REST
trait ContactsREST {
  @GET @SkipRESTName @RPCName("loadAll")
  def load(): Future[Seq[Contact]]

  @POST @SkipRESTName
  def create(@Body contact: Contact): Future[Contact]
}

@REST
trait ContactManagementREST {
  @GET @SkipRESTName
  def load(): Future[Contact]

  @PUT @SkipRESTName
  def update(@Body book: Contact): Future[Contact]

  @DELETE @SkipRESTName
  def remove(): Future[Contact]
}