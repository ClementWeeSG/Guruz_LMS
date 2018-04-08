package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
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
          details => getDetailValues(details)
        )
      )
    ).render
  }

  def getDetailValues(details: MemberDetails): Seq[Modifier] = {
    Seq(
      span(details.memberName).render
    )
  }


}
