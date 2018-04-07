package lms.api

import io.udash.rest._
import lms.models.{BookTransactionDetails, MemberDetails}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@REST
trait MemberInfoAPI {
  @GET
  @SkipRESTName
  def selectCardNos(): Future[List[String]]

  @GET
  @RESTName("details")
  def getMemberDetails(@URLPart cardId: String): Future[MemberDetails]

  @GET
  @RESTName("transactions")
  def getMemberTransactions(@URLPart cardId: String): Future[List[BookTransactionDetails]]
}

object DummyMemberInfoAPI extends MemberInfoAPI {

  private val dummyMembers: Map[String, (MemberDetails, List[BookTransactionDetails])] = Map(
    "h1134" -> (MemberDetails("Clement", "BRONZE", "", 5), List()),
    "k56yu" -> (MemberDetails("Ben", "SILVER", "", 2), List()),
    "lll0987f" -> (MemberDetails("Jan", "GOLD", "", 0), List()),
    "po56k32" -> (MemberDetails("Wilson", "PLATINUM", "", 0), List())
  )
  override def selectCardNos() = Future.successful(dummyMembers.keySet.toList)
  override def getMemberDetails(cardId: String) = Future(dummyMembers.get(cardId).map(_._1).get)
  override def getMemberTransactions(cardId: String) = Future.successful(dummyMembers.get(cardId).toList.flatMap(_._2))
}



