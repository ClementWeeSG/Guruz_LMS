package lms.models

import io.udash.properties.HasModelPropertyCreator
import lms.api.LMSGlobal

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

case class MemberInfo(memberDetails: MemberDetails = MemberDetails(), transactions: List[BookTransactionDetails] = List.empty)

object MemberInfo extends HasModelPropertyCreator[MemberInfo]

object MemberInfoUtils {
  def getInfo(card: String): Future[Option[MemberInfo]] = {
    LMSGlobal.memberAPI.getMemberDetails(card).flatMap {
      case Some(details) =>
        LMSGlobal.memberAPI.getMemberTransactions(card).map {
          transactions => Some(MemberInfo(details, transactions))
        } recover {
          case _: Throwable => Some(MemberInfo(details))
        }
      case None => Future.successful(None)
    }
  }

  def getCards(): Future[List[String]] = LMSGlobal.memberAPI.selectCardNos()
}

case class BookTransactionDetails(kind: String = "book", amountOfBooks: Option[Int] = None, fine: Option[Double] = None)

object BookTransactionDetails extends HasModelPropertyCreator[BookTransactionDetails]

case class MemberDetails(memberName: String = "", memberType: String = "", residencyType: String = "N/A", replacements: Option[Int] = None)

object MemberDetails extends HasModelPropertyCreator[MemberDetails]