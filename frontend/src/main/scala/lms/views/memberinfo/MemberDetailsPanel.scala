package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import io.udash.properties.single.ReadableProperty
import lms.models.{MemberDetails, MemberInfoCreation, SingleLoadingModel}
import lms.views.DataList
import org.scalajs.dom.Element
import scalatags.JsDom.all._


object MemberDetailsPanel {
  def apply(model: ModelProperty[SingleLoadingModel[MemberDetails]]): Element = new MemberDetailsPanel(model).render
}

class MemberDetailsPanel(detailsModel: ModelProperty[SingleLoadingModel[MemberDetails]]) extends CssView with MemberInfoCreation {

  def render(): Element = {
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading("Member Information"),
      UdashPanel.body(
        DataList[MemberDetails](
          detailsModel,
          Seq("Member Name", "Membership Type", "Residency Type", "Total Times Card Replaced"),
          prop => getDetailValues(prop.asModel)
        )
      )
    ).render
  }

  def getDetailValues(details: ModelProperty[MemberDetails]): Seq[ReadableProperty[String]] = {
    Seq(
      details.subProp(_.memberName),
      details.subProp(_.memberType),
      details.subProp(_.residencyType),
      details.subProp(_.replacements).transform(_.toString)
    )
  }


}
