package lms.views.memberinfo

import io.udash._
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import io.udash.properties.single.ReadableProperty
import lms.models.{MemberDetails, SingleLoadingModel}
import lms.views.DataList
import org.scalajs.dom.Element
import scalatags.JsDom.all._


object MemberDetailsPanel {
  def apply(model: ModelProperty[MemberDetails]): Element = new MemberDetailsPanel(model).render
}

class MemberDetailsPanel(detailsModel: ModelProperty[MemberDetails]) extends CssView {

  def render(): Element = {
    UdashPanel(PanelStyle.Info)(
      UdashPanel.heading("Member Information"),
      UdashPanel.body(
        DataList[MemberDetails](
          ModelProperty.apply(new SingleLoadingModel[MemberDetails](item = MemberDetails())),
          Seq("Member Name", "Membership Package", "Residency Type", "Number of Card Replacements"),
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
      details.subProp(_.replacements).transform(_.getOrElse(0).toString)
    )
  }


}
