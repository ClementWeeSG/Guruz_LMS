package lms.views.wishlist

import io.udash._
import lms.routing.WishListState

class WishListPresenter extends Presenter[WishListState] with ViewFactory[WishListState] {
  val showingSpecific = Property[Boolean](false)
  val schoolsLoadingModel = new SchoolsLoadingModel
  val itemsLoadingModel = new ItemsLoadingModel

  override def handleState(state: WishListState): Unit = {
    state.lib match {
      case None => loadAll()
      case Some(library) => loadForLibrary(library)
    }
  }

  private def loadForLibrary(library: String) = {
    showingSpecific.set(true)
    schoolsLoadingModel.loadForLibrary(library)
    itemsLoadingModel.loadForLibrary(library)
  }

  private def loadAll() = {
    showingSpecific.set(false)
    schoolsLoadingModel.loadAll()
    itemsLoadingModel.loadAll()
  }

  override def create(): (View, Presenter[WishListState]) = (null, this)
}
