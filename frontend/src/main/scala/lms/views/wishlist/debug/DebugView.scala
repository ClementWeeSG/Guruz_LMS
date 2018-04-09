package lms.views.wishlist.debug

import io.udash._
import org.scalajs.dom.Element
import scalatags.generic.Modifier
import scalatags.JsDom.all._

class DebugView(presenter: DebugPresenter) extends FinalView {
  override def getTemplate: Modifier[Element] = {
    div(
      p(bind(presenter.schools)),
      p(bind(presenter.items))
    )
  }
}
