package lms.views.wishlist

import io.udash._
import lms.api.LMSGlobal
import lms.models.DataLoadingModel
import lms.models.wishlist.{SchoolTag, SchoolsByLibraryRow}

class SchoolsLoadingModel extends WishListModel {
  val allModel = ModelProperty(new DataLoadingModel[SchoolsByLibraryRow]())
  val singleLibraryModel = ModelProperty(new DataLoadingModel[SchoolTag]())

  lazy val api = LMSGlobal.server.wishlist().neglectedSchools()

  override def loadAll(): Unit = loadModel(allModel, api.all())

  override def loadForLibrary(lib: String) = loadModel(singleLibraryModel, api.byLibrary(lib))

}
