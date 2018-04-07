package lms.views.memberinfo

import io.udash.properties.model.ModelProperty
import lms.models.{BookTransactionDetails, DataLoadingModel}
import lms.views._
import scalatags.JsDom.all._

object MemberTransactions {
  def apply(transactions: ModelProperty[DataLoadingModel[BookTransactionDetails]]) = new MemberTransactions(transactions).render
}

class MemberTransactions(transactions: ModelProperty[DataLoadingModel[BookTransactionDetails]]) {
  def render() = DataTable[BookTransactionDetails](
    transactions,
    Seq("Date", "Transaction Type", "Number of Books Borrowed", "Number of Books Renewed", "Fine Paid"),
    (prop) => {
      val data = prop.get
      Seq(
        data.dateTime,
        data.kind,
        data.numLent.toString,
        data.numRenewed.toString,
        data.fine.toString
      )
    }
  )
}
