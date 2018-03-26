package io.udash.demos.rest.views.book

import io.udash._
import io.udash.core.Presenter
import io.udash.demos.rest.model.{ContactId, PhoneBookId, PhoneBookInfo}
import io.udash.demos.rest.{ApplicationContext, IndexState, PhoneBookFormState}
import org.scalajs.dom

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class PhoneBookFormPresenter(model: ModelProperty[PhoneBookEditorModel]) extends Presenter[PhoneBookFormState] {
  import ApplicationContext._

  override def handleState(state: PhoneBookFormState): Unit = {
    state match {
      case PhoneBookFormState(None) =>
        model.subProp(_.loaded).set(true)
        model.subProp(_.loadingText).set("")

        model.subProp(_.isNewBook).set(true)
        model.subProp(_.name).set("")
        model.subProp(_.description).set("")
      case PhoneBookFormState(Some(id)) =>
        model.subProp(_.loaded).set(false)
        model.subProp(_.loadingText).set("Loading phone book data...")
        model.subProp(_.isNewBook).set(false)

        loadPhoneBookInfo(id)
        loadSelectedContacts(id)
        loadContacts()
    }
  }

  def loadPhoneBookInfo(id: PhoneBookId): Unit = {
    restServer.phoneBooks(id).load() onComplete {
      case Success(book) =>
        model.subProp(_.loaded).set(true)
        model.subProp(_.id).set(id)
        model.subProp(_.name).set(book.name)
        model.subProp(_.description).set(book.description)
      case Failure(ex) =>
        model.subProp(_.loadingText).set(s"Problem with phone book details loading: $ex")
    }
  }

  def loadContacts(): Unit = {
    restServer.contacts().load() onComplete {
      case Success(contacts) =>
        model.subProp(_.allContacts).set(contacts)
      case Failure(ex) =>
        dom.window.alert(s"Problem with contacts loading: $ex")
    }
  }

  def loadSelectedContacts(id: PhoneBookId): Unit = {
    restServer.phoneBooks(id).contacts().load() onComplete {
      case Success(contacts) =>
        model.subProp(_.selectedContacts).set(contacts)
        model.subSeq(_.selectedContacts).listenStructure(patch => {
          patch.added.foreach(item => addContactToBook(id, item.get))
          patch.removed.foreach(item => removeContactFromBook(id, item.get))
        })
      case Failure(ex) =>
        dom.window.alert(s"Problem with selected contacts loading: $ex")
    }
  }

  def addContactToBook(id: PhoneBookId, contactId: ContactId): Unit = {
    restServer.phoneBooks(id).contacts().add(contactId).failed.foreach { ex =>
      model.subSeq(_.selectedContacts).remove(contactId)
      dom.window.alert(s"Contact adding failed: $ex")
    }
  }

  def removeContactFromBook(id: PhoneBookId, contactId: ContactId): Unit = {
    restServer.phoneBooks(id).contacts().remove(contactId).failed.foreach { ex =>
      model.subSeq(_.selectedContacts).append(contactId)
      dom.window.alert(s"Contact remove failed: $ex")
    }
  }

  def createPhoneBook(): Unit = {
    restServer.phoneBooks().create(PhoneBookInfo(
      PhoneBookId(-1),
      model.subProp(_.name).get,
      model.subProp(_.description).get
    )) onComplete {
      case Success(_) =>
        applicationInstance.goTo(IndexState)
      case Failure(ex) =>
        dom.window.alert(s"Phone Book creation failed: $ex")
    }
  }

  def updatePhoneBook(): Unit = {
    restServer.phoneBooks(model.subProp(_.id).get).update(PhoneBookInfo(
      model.subProp(_.id).get,
      model.subProp(_.name).get,
      model.subProp(_.description).get
    )) onComplete {
      case Success(_) =>
        applicationInstance.goTo(IndexState)
      case Failure(ex) =>
        dom.window.alert(s"Phone Book update failed: $ex")
    }
  }
}
