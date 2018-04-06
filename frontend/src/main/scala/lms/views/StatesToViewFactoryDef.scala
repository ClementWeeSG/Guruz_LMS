package lms.views

import io.udash._
import io.udash.demos.rest.views._
import io.udash.demos.rest.views.index.DemoIndexViewFactory
import lms.routing._
import lms.views.items.ItemInfoPagePresenter
import lms.views.memberinfo.MemberInfoPagePresenter
import lms.views.popularity.ItemPopularityPagePresenter

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] = state match {
    case RootState => RootViewFactory
    case IndexState => DemoIndexViewFactory
    case MemberInfoState(cardId) => new MemberInfoPagePresenter
    case ItemPopularityState => new ItemPopularityPagePresenter
    case ItemTypeInfoState(typeId) => new ItemInfoPagePresenter
    case _ => ErrorViewFactory
  }
}