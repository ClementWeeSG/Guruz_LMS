package io.udash.demos.rest.views.book

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.UdashBootstrap.ComponentId
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import io.udash.css.CssView
import io.udash.demos.rest.model.ContactId
import org.scalajs.dom.raw.Event

class PhoneBookFormView(model: ModelProperty[PhoneBookEditorModel], presenter: PhoneBookFormPresenter)
  extends FinalView with CssView {

  import scalatags.JsDom.all._

  private def onFormSubmit(ev: Event): Unit = {
    if (model.subProp(_.isNewBook).get) presenter.createPhoneBook()
    else presenter.updatePhoneBook()
  }

  private val selectedStrings: SeqProperty[String] = model.subSeq(_.selectedContacts).transform(
    (id: ContactId) => id.value.toString,
    (s: String) => ContactId(s.toInt)
  )

  private val saveButton = UdashButton(buttonStyle = ButtonStyle.Primary)(
    tpe := "submit",
    produce(model.subProp(_.isNewBook)) {
      case true => span("Create").render
      case false => span("Save changes").render
    }
  )

  private val contactsForm = produce(model.subProp(_.isNewBook)) {
    case true =>
      h3("Create phone book to manage contacts").render
    case false =>
      div(
        h3("Contacts in book"),
        produce(model.subSeq(_.allContacts)) { contacts =>
          val idToName = contacts.map(c => (c.id.value.toString, c)).toMap
          UdashForm(
            UdashForm.checkboxes()(
              selectedStrings,
              idToName.keys.toSeq,
              decorator = { (input, id) =>
                label(BootstrapStyles.Form.checkbox)(input, {
                  val contact = idToName(id)
                  s"${contact.firstName} ${contact.lastName}"
                }).render
              }
            )
          ).render
        }
      ).render
  }

  private val content = div(
    produce(model.subProp(_.loaded)) {
      case false =>
        span(bind(model.subProp(_.loadingText))).render
      case true =>
        div(
          produce(model.subProp(_.isNewBook)) {
            case true => h2("Phone Book creator").render
            case false => h2("Phone Book editor").render
          },

          UdashForm(onFormSubmit _)(
            ComponentId("book-form"),
            UdashForm.group(
              UdashForm.textInput()("Name: ")(model.subProp(_.name))
            ),
            UdashForm.group(
              UdashForm.textInput()("Description: ")(model.subProp(_.description))
            ),
            UdashForm.group(
              saveButton.render
            )
          ).render,

          contactsForm
        ).render
    }
  ).render

  override def getTemplate: Modifier = content
}