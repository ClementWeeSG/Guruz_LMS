package lms.routing

import io.udash._
import io.udash.demos.rest.model.{ContactId, PhoneBookId}

import scala.util.Try

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
    case _ => ErrorState
  }

  private val state2Url: PartialFunction[RoutingState, String] = {
    case IndexState => ""
    case MemberInfoState(None) => "/members"
    case MemberInfoState(Some(card)) => s"/members/$card"
    case ItemPopularityState => "/popularity"
    case ItemTypeInfoState(Some(cat)) => s"/series/$cat"
    case ItemTypeInfoState(None) => s"/series"
    case _ => "/error"
  }
}