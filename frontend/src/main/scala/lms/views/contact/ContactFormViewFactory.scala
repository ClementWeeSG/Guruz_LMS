package io.udash.demos.rest.views.contact

import io.udash._
import io.udash.core.Presenter
import io.udash.demos.rest.model.ContactId
import lms.routing.ContactFormState

case class ContactFormViewFactory(id: Option[ContactId]) extends ViewFactory[ContactFormState] {
  override def create(): (View, Presenter[ContactFormState]) = {
    val model = ModelProperty(ContactEditorModel()) // use default values defined in model
    val presenter = new ContactFormPresenter(model)
    val view = new ContactFormView(model, presenter)
    (view, presenter)
  }
}
