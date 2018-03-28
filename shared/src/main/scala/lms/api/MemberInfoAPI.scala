package lms.api

import lms.models.{BookTransactionDetails, MemberDetails}

import scala.concurrent.Future

trait MemberInfoAPI {
  def selectCardNos(): Future[List[String]]

  def getMemberDetails(cardId: String): Future[Option[MemberDetails]]

  def getMemberTransactions(cardId: String): Future[List[BookTransactionDetails]]
}

object DummyMemberInfoAPI extends MemberInfoAPI {

  private val dummyMembers: Map[String, (MemberDetails, List[BookTransactionDetails])] = Map(
    "h1134" -> (MemberDetails("Clement", "BRONZE"), List()),
    "k56yu" -> (MemberDetails("Ben", "SILVER"), List()),
    "lll0987f" -> (MemberDetails("Jan", "GOLD"), List()),
    "po56k32" -> (MemberDetails("Wilson", "PLATINUM"), List())
  )

  override def selectCardNos() = Future.successful(dummyMembers.keySet.toList)

  override def getMemberDetails(cardId: String) = Future.successful(dummyMembers.get(cardId).map(_._1))

  override def getMemberTransactions(cardId: String) = Future.successful(dummyMembers.get(cardId).toList.flatMap(_._2))
}

object RemoteMemberInfoAPI extends MemberInfoAPI {
  override def selectCardNos() = ???

  override def getMemberDetails(cardId: String) = ???

  override def getMemberTransactions(cardId: String) = ???
}
