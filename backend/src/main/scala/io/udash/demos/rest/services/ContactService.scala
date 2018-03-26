package io.udash.demos.rest.services

import io.udash.demos.rest.model._

import scala.collection.mutable

trait ContactService {
  def load(): Seq[Contact]
  def load(id: ContactId): Option[Contact]

  def create(contact: Contact): Contact
  def update(id: ContactId, contact: Contact): Contact
  def remove(id: ContactId): Option[Contact]
}

object InMemoryContactService extends ContactService {
  private val contacts: mutable.Map[ContactId, Contact] = mutable.Map.empty

  private var idc = -1
  private def newId() = synchronized {
    idc += 1
    ContactId(idc)
  }

  override def load(): Seq[Contact] = synchronized {
    contacts.values.toSeq
  }

  override def load(id: ContactId): Option[Contact] = synchronized {
    contacts.get(id)
  }

  override def create(contact: Contact): Contact = synchronized {
    val id = newId()
    val withId = contact.copy(id = id)
    contacts(id) = withId
    withId
  }

  override def update(id: ContactId, contact: Contact): Contact = synchronized {
    val oldContact = contacts(id)
    contacts(id) = contact
    oldContact
  }

  override def remove(id: ContactId): Option[Contact] = synchronized {
    contacts.remove(id)
  }

  // Init data
  synchronized {
    Seq.fill(5)(newId()).foreach(id =>
      contacts(id) = Contact(id, s"John ${id.value}", "Doe", s"+22 123 43 2${id.value}", s"john.${id.value}@dmail.com")
    )
  }
}