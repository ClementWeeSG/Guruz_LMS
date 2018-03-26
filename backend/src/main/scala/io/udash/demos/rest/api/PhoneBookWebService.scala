package io.udash.demos.rest.api

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import com.avsystem.commons.serialization.GenCodec
import io.udash.demos.rest.model._
import io.udash.demos.rest.services.{ContactService, InMemoryContactService, InMemoryPhoneBookService, PhoneBookService}
import io.udash.rpc.DefaultUdashSerialization
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}


trait PhoneBookWebServiceSpec extends DefaultUdashSerialization {
  private val staticsDir = "frontend/target/UdashStatics/WebContent"

  val phoneBookService: PhoneBookService
  val contactService: ContactService

  implicit def optionMarshaller[T](implicit codec: GenCodec[T]): ToResponseMarshaller[Option[T]] =
    gencodecMarshaller[Option[T]](GenCodec.optionCodec(codec))

  implicit def gencodecMarshaller[T](implicit codec: GenCodec[T]): ToEntityMarshaller[T] =
    Marshaller.withFixedContentType(MediaTypes.`application/json`) { value =>
      var string: String = null
      val output = outputSerialization((serialized) => string = serialized)
      codec.write(output, value)
      HttpEntity(MediaTypes.`application/json`, string)
    }

  implicit def gencodecUnmarshaller[T](implicit codec: GenCodec[T]): FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(MediaTypes.`application/json`).map{ data =>
      val input = inputSerialization(data)
      val out: T = codec.read(input)
      out
    }

  private def completeIfNonEmpty[T](ctx: RequestContext)(opt: Option[T])(implicit rm: ToResponseMarshaller[T]) =
    opt match {
      case Some(v) => complete(v)(ctx)
      case None => complete(StatusCodes.NotFound)(ctx)
    }

  val route = {
    path("") {
      getFromFile(s"$staticsDir/index.html")
    } ~
    pathPrefix("scripts"){
      getFromDirectory(s"$staticsDir/scripts")
    } ~
    pathPrefix("assets"){
      getFromDirectory(s"$staticsDir/assets")
    } ~
    pathPrefix("api") {
      pathPrefix("book") {
        pathPrefix(Segment) { segment =>
          val bookId = PhoneBookId(segment.toInt)
          pathPrefix("contacts") {
            pathPrefix("count") {
              get {
                /** Adds contact to phone book */
                complete { phoneBookService.contactsCount(bookId) }
              }
            } ~
              pathPrefix("manage") {
                post {
                  /** Adds contact to phone book */
                  entity(as[ContactId]) { contactId =>
                    complete { phoneBookService.addContact(bookId, contactId) }
                  }
                } ~
                  delete {
                    /** Removes contact from phone book */
                    entity(as[ContactId]) { contactId =>
                      complete { phoneBookService.removeContact(bookId, contactId) }
                    }
                  }
              } ~
              get {
                /** Return contacts ids from selected book */
                complete { phoneBookService.contacts(bookId) }
              }
          } ~
            get { ctx =>
              /** Return phone book info */
              completeIfNonEmpty(ctx) { phoneBookService.load(bookId) }
            } ~
            put {
              /** Updates phone book info */
              entity(as[PhoneBookInfo]) { phoneBookInfo =>
                complete { phoneBookService.update(bookId, phoneBookInfo) }
              }
            } ~
            delete { ctx =>
              /** Removes phone book */
              completeIfNonEmpty(ctx) { phoneBookService.remove(bookId) }
            }
        } ~
          get {
            /** Return phone books list */
            complete { phoneBookService.load() }
          } ~
          post {
            /** Creates new phone book */
            entity(as[PhoneBookInfo]) { phoneBookInfo =>
              complete { phoneBookService.create(phoneBookInfo) }
            }
          }
      } ~
      pathPrefix("contact") {
        path(Segment) { segment =>
          val contactId = ContactId(segment.toInt)
          get { ctx =>
            /** Return contact details */
            completeIfNonEmpty(ctx) { contactService.load(contactId) }
          } ~
            put {
              /** Updates contact */
              entity(as[Contact]) { contact =>
                complete { contactService.update(contactId, contact) }
              }
            } ~
            delete { ctx =>
              /** Removes contact */
              completeIfNonEmpty(ctx) {
                phoneBookService.contactRemoved(contactId)
                contactService.remove(contactId)
              }
            }
        } ~
          get {
            /** Creates new contact */
            complete { contactService.load() }
          } ~
          post {
            /** Creates new contact */
            entity(as[Contact]) { contact =>
              complete { contactService.create(contact) }
            }
          }
      }
    }
  }
}

class PhoneBookWebService() extends PhoneBookWebServiceSpec {
  override val phoneBookService: PhoneBookService = InMemoryPhoneBookService
  override val contactService: ContactService = InMemoryContactService
}
