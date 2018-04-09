package lms.models

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.ModelPropertyCreator
import com.avsystem.commons.misc.Opt

case class MemberInfo(memberDetails: MemberDetails, transactions: List[BookTransactionDetails])

case class BookTransactionDetails(dateTime: String, kind: String, numLent: Int, numRenewed: Int, fine: Double)

object BookTransactionDetails extends HasGenCodec[BookTransactionDetails]

case class MemberDetails(memberName: String, memberType: String, residencyType: String, replacements: Opt[Int])

object MemberDetails extends HasGenCodec[MemberDetails]

trait MemberInfoCreation {
  implicit val detailsCreator: ModelPropertyCreator[MemberDetails] = ModelPropertyCreator.materialize[MemberDetails]
}