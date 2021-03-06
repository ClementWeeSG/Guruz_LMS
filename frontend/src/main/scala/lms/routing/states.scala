package lms.routing

import io.udash._
import io.udash.demos.rest.model.{ContactId, PhoneBookId}

sealed abstract class RoutingState(val parentState: Option[ContainerRoutingState]) extends State {
  type HierarchyRoot = RoutingState

  def url(implicit application: Application[RoutingState]): String =
    s"#${application.matchState(this).value}"
}

sealed abstract class ContainerRoutingState(parentState: Option[ContainerRoutingState]) extends RoutingState(parentState) with ContainerState
sealed abstract class FinalRoutingState(parentState: Option[ContainerRoutingState]) extends RoutingState(parentState) with FinalState

object RootState extends ContainerRoutingState(None)
object ErrorState extends FinalRoutingState(Some(RootState))

//Demo
case object DemoIndexState extends FinalRoutingState(Some(RootState))
case class ContactFormState(id: Option[ContactId] = None) extends FinalRoutingState(Some(RootState))
case class PhoneBookFormState(id: Option[PhoneBookId] = None) extends FinalRoutingState(Some(RootState))

//Actual
case object IndexState extends FinalRoutingState(Some(RootState))

case class MemberInfoState(card: Option[String]) extends FinalRoutingState(Some(RootState))

case class ItemTypeInfoState(itemType: Option[String]) extends FinalRoutingState(Some(RootState))

case object ItemPopularityState extends FinalRoutingState(Some(RootState))

case class WishListState(lib: Option[String]) extends FinalRoutingState(Some(RootState))

case class DebugState(items: Boolean) extends FinalRoutingState(Some(RootState))

case class DebugURLState(wrapped: WishListState) extends FinalRoutingState(Some(RootState))