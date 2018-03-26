package io.udash.demos.rest.services

import io.udash.demos.rest.model._

import scala.collection.mutable
import scala.util.Random

trait PhoneBookService {
  def load(): Seq[PhoneBookInfo]
  def load(id: PhoneBookId): Option[PhoneBookInfo]

  def create(contact: PhoneBookInfo): PhoneBookInfo
  def update(id: PhoneBookId, contact: PhoneBookInfo): PhoneBookInfo
  def remove(id: PhoneBookId): Option[PhoneBookInfo]

  def contacts(id: PhoneBookId): Seq[ContactId]
  def contactsCount(id: PhoneBookId): Int
  def addContact(id: PhoneBookId, contactId: ContactId): Unit
  def removeContact(id: PhoneBookId, contactId: ContactId): Unit

  def contactRemoved(id: ContactId): Unit
}

object InMemoryPhoneBookService extends PhoneBookService {
  private val books: mutable.Map[PhoneBookId, (PhoneBookInfo, mutable.HashSet[ContactId])] = mutable.Map.empty

  private var idc = -1
  private def newId() = synchronized {
    idc += 1
    PhoneBookId(idc)
  }

  override def load(): Seq[PhoneBookInfo] = synchronized {
    books.values.map(_._1).toSeq
  }

  override def load(id: PhoneBookId): Option[PhoneBookInfo] = synchronized {
    books.get(id).map(_._1)
  }

  override def create(book: PhoneBookInfo): PhoneBookInfo = synchronized {
    val id = newId()
    val withId = book.copy(id = id)
    books(id) = (withId, mutable.HashSet.empty)
    withId
  }

  override def update(id: PhoneBookId, phoneBook: PhoneBookInfo): PhoneBookInfo = synchronized {
    val oldPhoneBook = books(id)
    books(id) = oldPhoneBook.copy(_1 = phoneBook)
    oldPhoneBook._1
  }

  override def remove(id: PhoneBookId): Option[PhoneBookInfo] = synchronized {
    books.remove(id).map(_._1)
  }

  override def contacts(id: PhoneBookId): Seq[ContactId] = synchronized {
    books(id)._2.toSeq
  }

  override def contactsCount(id: PhoneBookId): Int = synchronized {
    books(id)._2.size
  }

  override def addContact(id: PhoneBookId, contactId: ContactId): Unit = synchronized {
    books.get(id).map(_._2).foreach(_.+=(contactId))
  }

  override def removeContact(id: PhoneBookId, contactId: ContactId): Unit = synchronized {
    books.get(id).map(_._2).foreach(_.-=(contactId))
  }

  def contactRemoved(id: ContactId): Unit = synchronized {
    books.foreach (book => book._2._2.remove(id))
  }

  // Init data
  synchronized {
    Seq.fill(2)(newId()).foreach(id =>
      books(id) = (PhoneBookInfo(id, s"Book ${id.value}", "Yet another phone book..."), mutable.HashSet(ContactId(Random.nextInt(5))))
    )
  }
}
