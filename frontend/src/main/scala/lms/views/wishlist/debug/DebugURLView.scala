package lms.views.wishlist.debug

import io.udash._
import scalatags.JsDom.all._

class DebugURLView(presenter: DebugURLPresenter) extends FinalView {
  override def getTemplate: Modifier = {
    div(bind(presenter.display))
  }
}
