package lms.views.wishlist

import io.udash.properties.model.ModelProperty
import lms.api.LMSGlobal
import lms.models._

class ItemsLoadingModel extends WishListModel {

  val allModel = ModelProperty(new DataLoadingModel[wishlist.PopularItems.All]())
  val singleLibraryModel = ModelProperty(new DataLoadingModel[wishlist.PopularItems.All]())

  lazy val api = LMSGlobal.server.wishlist().popularItems()

  override def loadAll(): Unit = loadModel(allModel, api.all())

  override def loadForLibrary(lib: String): Unit = loadModel(singleLibraryModel, api.byLibrary(lib))
}
