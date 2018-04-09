package lms.views.wishlist

import io.udash._
import lms.ApplicationContext
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
        println("Wish List: Loading Data")
        loadAll()
        selectedLibrary.listen(lib => ApplicationContext.applicationInstance.goTo(WishListState(Option(lib).filter(_.nonEmpty).filter(_ != "(All Libraries)"))))
      case Some("(All Libraries)") =>
        println("Wish List: Loading Data")
        selectedLibrary.set("(All Libraries)")
        loadAll()
        selectedLibrary.listen(lib => ApplicationContext.applicationInstance.goTo(WishListState(Option(lib).filter(_.nonEmpty).filter(_ != "(All Libraries)"))))
      case Some(library) =>
        println("Wish List: Loading Data")
        selectedLibrary.set(library)
        loadForLibrary(library)
        selectedLibrary.listen(lib => ApplicationContext.applicationInstance.goTo(WishListState(Option(lib).filter(_.nonEmpty).filter(_ != "(All Libraries)"))))
    }
  }

  def loadLibraries() = {
    libraries.clear()
    LMSGlobal.server.libraries() onComplete {
      case Success(libs) =>
        libraries.append("(All Libraries)")
        libraries.append(libs: _*)
        println("Wishlist: Loaded Library List successfully")
      case Failure(ex) =>
        libraries.append("[Error]")
    }
  }

  private def loadForLibrary(library: String) = {
    showingSpecific.set(true)
    println("Wishlist: Loading per library data")
    schoolsLoadingModel.loadForLibrary(library)
    itemsLoadingModel.loadForLibrary(library)
    println("Wishlist: Loaded per library data successfully")
  }

  private def loadAll() = {
    showingSpecific.set(false)
    println("Wishlist: Loading Global Data")
    schoolsLoadingModel.loadAll()
    itemsLoadingModel.loadAll()
    println("Wishilist: Completed Loading data")
  }

  override def create(): (View, Presenter[WishListState]) = (new WishlistView((this)), this)
}
