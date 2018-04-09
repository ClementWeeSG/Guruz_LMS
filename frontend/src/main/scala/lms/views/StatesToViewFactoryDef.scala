package lms.views

import io.udash._
import io.udash.demos.rest.views._
import io.udash.demos.rest.views.index.IndexViewFactory
import lms.routing._
import lms.views.items.ItemInfoPagePresenter
import lms.views.memberinfo.MemberInfoPagePresenter
import lms.views.popularity.ItemPopularityPagePresenter
import lms.views.wishlist.WishListPresenter
import lms.views.wishlist.debug.DebugPresenter

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] = state match {
    case RootState => RootViewFactory
    case IndexState => IndexViewFactory
    case MemberInfoState(cardId) => new MemberInfoPagePresenter
    case ItemPopularityState => new ItemPopularityPagePresenter
    case ItemTypeInfoState(typeId) => new ItemInfoPagePresenter
    case WishListState(_) => new WishListPresenter
    case DebugState(_) => new DebugPresenter
    case _ => ErrorViewFactory
  }
}