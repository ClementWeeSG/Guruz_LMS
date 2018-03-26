package io.udash.demos.rest.views.contact

import io.udash._
import io.udash.bootstrap.UdashBootstrap.ComponentId
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import org.scalajs.dom.raw.Event

class ContactFormView(model: ModelProperty[ContactEditorModel], presenter: ContactFormPresenter) extends FinalView {
  import scalatags.JsDom.all._

  private def onFormSubmit(ev: Event): Unit = {
    if (model.subProp(_.isNewContact).get) presenter.createContact()
    else presenter.updateContact()
  }

  private val saveButton = UdashButton(buttonStyle = ButtonStyle.Primary)(
    tpe := "submit",
    produce(model.subProp(_.isNewContact)) {
      case true => span("Create").render
      case false => span("Save changes").render
    }
  )

  private val content = div(
    produce(model.subProp(_.loaded)) {
      case false =>
        span(bind(model.subProp(_.loadingText))).render
      case true =>
        div(
          produce(model.subProp(_.isNewContact)) {
            case true => h2("Contact creator").render
            case false => h2("Contact editor").render
          },
          UdashForm(onFormSubmit _)(
            ComponentId("contact-form"),
            UdashForm.group(
              UdashForm.textInput()("First name: ")(model.subProp(_.firstName))
            ),
            UdashForm.group(
              UdashForm.textInput()("Last name: ")(model.subProp(_.lastName))
            ),
            UdashForm.group(
              UdashForm.textInput()("Phone: ")(model.subProp(_.phone))
            ),
            UdashForm.group(
              UdashForm.textInput()("E-mail: ")(model.subProp(_.email))
            ),
            UdashForm.group(
              saveButton.render
            )
          ).render
        ).render
    }
  ).render

  override def getTemplate: Modifier = content
}