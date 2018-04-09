package lms.views.wishlist.debug

import io.udash.Presenter
import io.udash.core.{View, ViewFactory}
import io.udash.properties.single.Property
import lms.api.LMSGlobal
import lms.routing.DebugURLState

import scala.util.{Failure, Success}

class DebugURLPresenter extends Presenter[DebugURLState] with ViewFactory[DebugURLState] {
  val display = Property.empty[String]

  override def handleState(state: DebugURLState): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    state.wrapped.lib match {
      case None => display.set("There is no param URL")
      case Some(part) =>
        LMSGlobal.server.url(part) onComplete {
          case Success(changed) => display.set(s"Param [$part] changed to [$changed] by URL Encoding/Decoding Process")
          case Failure(ex) => display.set(s"Error sending to server: $ex")
        }
    }
  }

  override def create(): (View, Presenter[DebugURLState]) = (new DebugURLView(this), this)
}
