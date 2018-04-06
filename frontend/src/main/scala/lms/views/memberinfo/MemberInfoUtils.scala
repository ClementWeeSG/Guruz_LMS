package lms.views.memberinfo

import lms.api.LMSGlobal
import lms.models.MemberInfo

import scala.concurrent.{ExecutionContext, Future}

object MemberInfoUtils {
  def getInfo(card: String)(implicit ec: ExecutionContext): Future[Option[MemberInfo]] = {
    //val test: Future[MemberDetails] = LMSGlobal.memberAPI.getMemberDetails(card)
    LMSGlobal.memberAPI.getMemberDetails(card).flatMap {
      details =>
        LMSGlobal.memberAPI.getMemberTransactions(card).map {
          transactions => Some(MemberInfo(details, transactions))
        } recover {
          case _: Throwable => Some(MemberInfo(details))
        }
    }
  }

  def getCards(): Future[List[String]] = LMSGlobal.memberAPI.selectCardNos()
}
