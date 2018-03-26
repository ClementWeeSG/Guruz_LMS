package io.udash.demos.rest.views.index

import io.udash._
import io.udash.demos.rest.IndexState
import io.udash.demos.rest.model.{Contact, ContactId, PhoneBookId, PhoneBookInfo}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class IndexPresenter(model: ModelProperty[IndexViewModel]) extends Presenter[IndexState.type] {
  import io.udash.demos.rest.ApplicationContext._

  override def handleState(state: IndexState.type): Unit =
    refresh()

  def removeContact(id: ContactId): Unit = {
    restServer.contacts(id).remove() onComplete {
      case Success(removedContact) =>
        model.subSeq(_.contacts.elements).remove(removedContact)
        refreshPhoneBooksSizes(model.subModel(_.books))
      case Failure(ex) =>
        dom.window.alert(s"Contact removing failed! ($ex)")
    }
  }

  def removePhoneBook(id: PhoneBookId): Unit = {
    restServer.phoneBooks(id).remove() onComplete {
      case Success(_) =>
        val elements = model.subSeq(_.books.elements)
        val removed = elements.get.find(_.id == id)
        removed.foreach(elements.remove)
      case Failure(ex) =>
        dom.window.alert(s"Phone book removing failed! ($ex)")
    }
  }

  def refresh(): Unit = {
    refreshPhoneBooks(model.subModel(_.books), restServer.phoneBooks().load(), "Loading phone books...")
    refreshContacts(model.subModel(_.contacts), restServer.contacts().load(), "Loading contacts...")
  }

  private def refreshContacts(model: ModelProperty[DataLoadingModel[Contact]], elements: Future[Seq[Contact]], loadingText: String) : Unit = {
    model.subProp(_.loaded).set(false)
    model.subProp(_.loadingText).set(loadingText)

    elements onComplete {
      case Success(elems) =>
        model.subProp(_.loaded).set(true)
        model.subSeq(_.elements).set(elems)
      case Failure(ex) =>
        model.subProp(_.loadingText).set(s"Error: $ex")
    }
  }

  private def refreshPhoneBooks(model: ModelProperty[DataLoadingModel[PhoneBookExtInfo]], elements: Future[Seq[PhoneBookInfo]], loadingText: String) : Unit = {
    model.subProp(_.loaded).set(false)
    model.subProp(_.loadingText).set(loadingText)

    elements onComplete {
      case Success(elems) =>
        model.subProp(_.loaded).set(true)
        model.subSeq(_.elements).clear()

        elems.foreach { el =>
          model.subSeq(_.elements).append(
            PhoneBookExtInfo(el.id, el.name, el.description, 0)
          )
        }

        refreshPhoneBooksSizes(model)
      case Failure(ex) =>
        model.subProp(_.loadingText).set(s"Error: $ex")
    }
  }

  private def refreshPhoneBooksSizes(model: ModelProperty[DataLoadingModel[PhoneBookExtInfo]]): Unit = {
    model.subSeq(_.elements).elemProperties.foreach { el =>
      val element = el.asModel
      restServer.phoneBooks(el.get.id).contacts().count() onComplete {
        case Success(count) =>
          element.subProp(_.contactsCount).set(count)
        case Failure(ex) =>
          dom.window.alert(s"Contacts count for book ${el.get.id} loading failed: $ex")
          element.subProp(_.contactsCount).set(-1)
      }
    }
  }
}
