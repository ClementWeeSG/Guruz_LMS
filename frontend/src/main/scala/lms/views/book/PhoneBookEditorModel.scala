package io.udash.demos.rest.views.book

import io.udash.demos.rest.model.{Contact, ContactId, PhoneBookId}
import io.udash.properties.HasModelPropertyCreator

case class PhoneBookEditorModel(
  loaded: Boolean = false,
  loadingText: String = "",

  isNewBook: Boolean = true,
  id: PhoneBookId = PhoneBookId(-1),
  name: String = "",
  description: String = "",

  allContacts: Seq[Contact] = Seq.empty,
  selectedContacts: Seq[ContactId] = Seq.empty
)

object PhoneBookEditorModel extends HasModelPropertyCreator[PhoneBookEditorModel]
