package lms.models

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.HasModelPropertyCreator

case class MemberInfo(memberDetails: MemberDetails = MemberDetails(), transactions: Seq[BookTransactionDetails] = List.empty)

object MemberInfo extends HasModelPropertyCreator[MemberInfo]

case class BookTransactionDetails(kind: String, amountOfBooks: Option[Int], fine: Option[Double])

object BookTransactionDetails extends HasGenCodec[BookTransactionDetails]

case class MemberDetails(memberName: String = "", memberType: String = "", residencyType: String = "N/A", replacements: Option[Int] = None)

object MemberDetails extends HasModelPropertyCreator[MemberDetails]