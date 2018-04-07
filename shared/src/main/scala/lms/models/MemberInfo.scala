package lms.models

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.{HasModelPropertyCreator, ModelPropertyCreator}

case class MemberInfo(memberDetails: MemberDetails = MemberDetails("", "", "N/A", 0), transactions: Seq[BookTransactionDetails] = List.empty)

object MemberInfo extends HasModelPropertyCreator[MemberInfo]

case class BookTransactionDetails(dateTime: String, kind: String, numLent: Int, numRenewed: Int, fine: Double)

object BookTransactionDetails extends HasGenCodec[BookTransactionDetails]

case class MemberDetails(memberName: String, memberType: String, residencyType: String, replacements: Int)

object MemberDetails extends HasGenCodec[MemberDetails]

trait MemberInfoCreation {
  implicit val detailsCreator: ModelPropertyCreator[MemberDetails] = ModelPropertyCreator.materialize[MemberDetails]
}