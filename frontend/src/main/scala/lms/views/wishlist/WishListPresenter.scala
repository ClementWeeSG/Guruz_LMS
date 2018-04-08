package lms.views.wishlist

import io.udash._
import lms.api.LMSGlobal
import lms.routing.WishListState

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class WishListPresenter extends Presenter[WishListState] with ViewFactory[WishListState] {
  val showingSpecific = Property[Boolean](false)
  val schoolsLoadingModel = new SchoolsLoadingModel
  val itemsLoadingModel = new ItemsLoadingModel
  val libraries = SeqProperty.empty[String]
  val selectedLibrary = Property.empty[String]

  override def handleState(state: WishListState): Unit = {
    loadLibraries()
    state.lib match {
      case None =>
        selectedLibrary.set("(All Libraries)")
        loadAll()
      case Some("(All Libraries)") =>
        selectedLibrary.set("(All Libraries)")
        loadAll()
      case Some(library) =>
        selectedLibrary.set(library)
        loadForLibrary(library)
    }
  }

  def loadLibraries() = {
    libraries.clear()
    LMSGlobal.server.libraries() onComplete {
      case Success(libs) =>
        libraries.append("(All Libraries)")
        libraries.append(libs: _*)
      case Failure(ex) =>
        libraries.append("[Error]")
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

  override def create(): (View, Presenter[WishListState]) = (new WishlistView((this)), this)
}
