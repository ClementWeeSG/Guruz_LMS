package lms.views.wishlist

import lms.views.DataTable
import org.scalajs.dom.Node
import io.udash._
import lms.models.wishlist.{SchoolTag, SchoolsByLibraryRow}
import scalatags.JsDom.all._

case class SchoolsPanel(model: SchoolsLoadingModel) extends WishListPanel("Schools Not Yet Visited") {
  override def allPanel(): Modifier = DataTable[SchoolsByLibraryRow](
    model.allModel,
    Seq("Library", "School"),
    prop => {
      val data = prop.get
      Seq(
        data.library,
        data.school
      )
    }
  )

  override def specificPanel(): Modifier = DataTable[SchoolTag](
    model.singleLibraryModel,
    Seq("Schools Not Visited"),
    prop => Seq(prop.get.value))
}
