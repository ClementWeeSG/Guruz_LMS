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
    Seq("Transaction Type", "Number Of Books", "Fine Owed"),
    (prop) => {
      val data = prop.get
      Seq(
        data.kind,
        data.amountOfBooks.getOrElse(0).toString,
        data.fine.getOrElse(0.00).toString
      )
    }
  )
}
