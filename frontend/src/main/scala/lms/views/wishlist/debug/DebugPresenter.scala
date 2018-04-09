package lms.views.wishlist.debug

import io.udash.Presenter
import io.udash.core.{View, ViewFactory}
import io.udash.properties.single.Property
import lms.api.LMSGlobal
import lms.routing.DebugState

import scala.util.{Failure, Success}

class DebugPresenter extends Presenter[DebugState] with ViewFactory[DebugState] {

  import scala.concurrent.ExecutionContext.Implicits.global

  val schools = Property("Schools Not Loaded!")
  val items = Property("Books Not Loaded!")


  override def handleState(state: DebugState): Unit = {
    if (state.items) {
      loadItems()
    } else {
      loadSchools()
    }
  }

  def loadSchools(): Unit = {
    LMSGlobal.server.wishlist().neglectedSchools().byLibrary("National Library") onComplete {
      case Success(elems) => schools.set(elems.toString())
      case Failure(ex) => schools.set(s"Exception: $ex")
    }
  }

  def loadItems(): Unit = {
    LMSGlobal.server.wishlist().popularItems().byLibrary("National Library") onComplete {
      case Success(elems) => items.set(elems.toString())
      case Failure(ex) => items.set(s"Exception: $ex")
    }
  }

  override def create(): (View, Presenter[DebugState]) = (new DebugView(this), this)
}
