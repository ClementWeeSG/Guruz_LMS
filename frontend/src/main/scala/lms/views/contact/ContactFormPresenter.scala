package io.udash.demos.rest.views.contact

import io.udash._
import io.udash.core.Presenter
import io.udash.demos.rest.model.{Contact, ContactId}
import lms.routing.{ContactFormState, IndexState}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class ContactFormPresenter(model: ModelProperty[ContactEditorModel]) extends Presenter[ContactFormState] {
  import lms.ApplicationContext._

  override def handleState(state: ContactFormState): Unit = {
    state match {
      case ContactFormState(None) =>
        model.subProp(_.loaded).set(true)
        model.subProp(_.loadingText).set("")

        model.subProp(_.isNewContact).set(true)
        model.subProp(_.firstName).set("")
        model.subProp(_.lastName).set("")
        model.subProp(_.phone).set("")
        model.subProp(_.email).set("")
      case ContactFormState(Some(id)) =>
        model.subProp(_.loaded).set(false)
        model.subProp(_.loadingText).set("Loading contact data...")
        model.subProp(_.isNewContact).set(false)

        loadContactData(id)
    }
  }

  def loadContactData(id: ContactId): Unit = {
    restServer.contacts(id).load() onComplete {
      case Success(contact) =>
        model.subProp(_.loaded).set(true)
        model.subProp(_.id).set(id)
        model.subProp(_.firstName).set(contact.firstName)
        model.subProp(_.lastName).set(contact.lastName)
        model.subProp(_.phone).set(contact.phone)
        model.subProp(_.email).set(contact.email)
      case Failure(ex) =>
        model.subProp(_.loadingText).set(s"Problem with contact details loading: $ex")
    }
  }

  def createContact(): Unit = {
    restServer.contacts().create(Contact(
      ContactId(-1),
      model.subProp(_.firstName).get,
      model.subProp(_.lastName).get,
      model.subProp(_.phone).get,
      model.subProp(_.email).get
    )) onComplete {
      case Success(contact) =>
        applicationInstance.goTo(IndexState)
      case Failure(ex) =>
        dom.window.alert(s"Contact creation failed: $ex")
    }
  }

  def updateContact(): Unit =
    restServer.contacts(model.subProp(_.id).get).update(Contact(
      model.subProp(_.id).get,
      model.subProp(_.firstName).get,
      model.subProp(_.lastName).get,
      model.subProp(_.phone).get,
      model.subProp(_.email).get
    )) onComplete {
      case Success(contact) =>
        applicationInstance.goTo(IndexState)
      case Failure(ex) =>
        dom.window.alert(s"Contact update failed: $ex")
    }
}
