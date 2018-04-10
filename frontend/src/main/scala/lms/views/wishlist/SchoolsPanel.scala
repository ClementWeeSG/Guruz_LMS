package lms.views.wishlist

import lms.models.wishlist.SchoolTag
import lms.views.DataTable
import scalatags.JsDom.all._

case class SchoolsPanel(presenter: WishListPresenter) extends WishListPanel("Schools Not Yet Visited") {
  override def allPanel(): Modifier = DataTable[SchoolTag](
    presenter.schoolsLoadingModel.allModel,
    Seq("School", "Address"),
    prop => {
      val data = prop.get
      Seq(
        data.school,
        data.addr
      )
    }
  )

  override def specificPanel(): Modifier = DataTable[SchoolTag](
    presenter.schoolsLoadingModel.singleLibraryModel,
    Seq("School", "Address"),
    prop => {
      val data = prop.get
      Seq(
        data.school,
        data.addr
      )
    })
}
