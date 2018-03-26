package io.udash.demos.rest.views.index

import io.udash._
import io.udash.bootstrap.button.{ButtonSize, ButtonStyle, UdashButton}
import io.udash.bootstrap.table.UdashTable
import io.udash.css.CssView
import io.udash.demos.rest._
import io.udash.demos.rest.model.Contact
import io.udash.properties.PropertyCreator

class IndexView(model: ModelProperty[IndexViewModel], presenter: IndexPresenter) extends FinalView with CssView {
  import scalatags.JsDom.all._

  private def headerButtons(loadedProp: Property[Boolean], creatorState: RoutingState): Modifier = {
    produce(loadedProp) { loaded =>
      if (loaded) {
        val btn = UdashButton(ButtonStyle.Primary)("Create new")

        btn.listen {
          case UdashButton.ButtonClickEvent(_, _) =>
            ApplicationContext.applicationInstance.goTo(creatorState)
        }

        span(style := "float: right", btn.render).render
      } else span().render
    }
  }

  private def actionButtons(editState: RoutingState, removeCallback: () => Any): Modifier = {
    val editBtn = UdashButton(ButtonStyle.Link, ButtonSize.ExtraSmall)("Edit")
    editBtn.listen {
      case UdashButton.ButtonClickEvent(_, _) =>
        ApplicationContext.applicationInstance.goTo(editState)
    }

    val removeBtn = UdashButton(ButtonStyle.Link, ButtonSize.ExtraSmall)("Remove")
    removeBtn.listen {
      case UdashButton.ButtonClickEvent(_, _) =>
        removeCallback()
    }

    span(editBtn.render, " | ", removeBtn.render)
  }

  private def elementsTable[T: PropertyCreator](
    model: ModelProperty[DataLoadingModel[T]],
    headers: Seq[String],
    tableElementsFactory: CastableProperty[T] => Seq[Modifier]
  ): Modifier = {
    produce(model.subProp(_.loaded)) { loaded =>
      if (loaded) {
        UdashTable(hover = Property(true))(model.subSeq(_.elements))(
          rowFactory = (p) => tr(tableElementsFactory(p).map(name => td(name))).render,
          headerFactory = Some(() => tr(headers.map(name => th(name))).render)
        ).render
      } else span(bind(model.subProp(_.loadingText))).render
    }
  }

  private val content = div(
    div(
      headerButtons(model.subProp(_.books.loaded), PhoneBookFormState(None)),
      h2("Phone Books")
    ),
    hr,
    elementsTable[PhoneBookExtInfo](
      model.subModel(_.books),
      Seq("Id", "Name", "Description", "Contacts",  "Actions"),
      (prop: CastableProperty[PhoneBookExtInfo]) => {
        val book: PhoneBookExtInfo = prop.get
        Seq(
          book.id.value,
          book.name,
          book.description,
          i(bind(prop.asModel.subProp(_.contactsCount))).render,
          actionButtons(
            PhoneBookFormState(Some(book.id)),
            () => presenter.removePhoneBook(book.id)
          )
        )
      }
    ),

    div(
      headerButtons(model.subProp(_.contacts.loaded), ContactFormState(None)),
      h2("Contacts")
    ),
    hr,
    elementsTable[Contact](
      model.subModel(_.contacts),
      Seq("Id", "Name", "Phone", "E-mail", "Actions"),
      (prop: Property[Contact]) => {
        val contact: Contact = prop.get
        Seq(
          contact.id.value,
          s"${contact.firstName} ${contact.lastName}",
          contact.phone,
          contact.email,
          actionButtons(
            ContactFormState(Some(contact.id)),
            () => presenter.removeContact(contact.id)
          )
        )
      }
    )
  ).render

  override def getTemplate: Modifier = content
}