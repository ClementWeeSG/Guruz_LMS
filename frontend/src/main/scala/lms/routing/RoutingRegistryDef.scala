package lms.routing

import io.udash.{RoutingRegistry, _}

class RoutingRegistryDef extends RoutingRegistry[RoutingState] {
  def matchUrl(url: Url): RoutingState =
    url2State.applyOrElse(url.value.stripSuffix("/"), (x: String) => ErrorState)

  def matchState(state: RoutingState): Url =
    Url(state2Url.apply(state))

  private val url2State: PartialFunction[String, RoutingState] = {
    case "" => IndexState
    case "/members" => MemberInfoState(None)
    case "/members" / card => MemberInfoState(Some(card))
    case "/popularity" => ItemPopularityState
    case "/series" => ItemTypeInfoState(None)
    case "/series" / category => ItemTypeInfoState(Some(category))
    case "/wishlist" => WishListState(None)
    case "/wishlist" / lib => WishListState(Some(lib))
    case "/debug/books" => DebugState(true)
    case "/debug/schools" => DebugState(false)
    case "/debug/url" => DebugURLState(WishListState(None))
    case "/debug/url" / frag => DebugURLState(WishListState(Some(frag)))
    case _ => ErrorState
  }

  private val state2Url: PartialFunction[RoutingState, String] = {
    case IndexState => ""
    case MemberInfoState(None) => "/members"
    case MemberInfoState(Some(card)) => s"/members/$card"
    case ItemPopularityState => "/popularity"
    case ItemTypeInfoState(Some(cat)) => s"/series/$cat"
    case ItemTypeInfoState(None) => "/series"
    case WishListState(None) => "/wishlist"
    case WishListState(Some(lib)) => s"/wishlist/$lib"
    case DebugState(true) => "/debug/books"
    case DebugState(false) => "/debug/schools"
    case DebugURLState(WishListState(None)) => "/debug/url"
    case DebugURLState(WishListState(Some(lib))) => s"/debug/url/$lib"
    case _ => "/error"
  }
}