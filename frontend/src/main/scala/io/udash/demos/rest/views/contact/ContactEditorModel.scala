package io.udash.demos.rest.views.contact

import io.udash.demos.rest.model.ContactId
import io.udash.properties.HasModelPropertyCreator

case class ContactEditorModel(
  loaded: Boolean = false,
  loadingText: String = "",

  isNewContact: Boolean = false,
  id: ContactId = ContactId(-1),
  firstName: String = "",
  lastName: String = "",
  phone: String = "",
  email: String = ""
)

object ContactEditorModel extends HasModelPropertyCreator[ContactEditorModel]
