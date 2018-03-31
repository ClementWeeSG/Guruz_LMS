package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.bootstrap.utils.UdashListGroup
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import lms.models.MemberDetails
import lms.views.memberinfo.MemberDetailsPanel.DetailsRow
import org.scalajs.dom.Element
import org.scalajs.dom.html.LI
import scalatags.JsDom.all._


object MemberDetailsPanel {

  case class DetailsRow(property: String, value: ReadableProperty[String])

  def apply(model: ModelProperty[MemberDetails]): Element = new MemberDetailsPanel(model).render
}

class MemberDetailsPanel(detailsModel: ModelProperty[MemberDetails]) extends CssView {

  val rows = SeqProperty(Seq(
    DetailsRow("Member Name", detailsModel.subProp(_.memberName)),
    DetailsRow("Membership Package", detailsModel.subProp(_.memberType)),
    DetailsRow("Residency Type", detailsModel.subProp(_.residencyType)),
    DetailsRow("Replacements", detailsModel.subProp(_.replacements).transform(_.getOrElse(0).toString))
  ))


  def render(): Element = {
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading("Member Information"),
      UdashPanel.body(renderRows(rows))
    ).render
  }

  def renderRow(row: DetailsRow): LI = {
    li(
      strong(s"${row.property}:"),
      span(textDecoration.underline)(bind(row.value))
    ).render
  }

  def renderRows(rows: ReadableSeqProperty[DetailsRow]) = {
    UdashListGroup(rows)(_.transform(renderRow).get).render
  }


}
