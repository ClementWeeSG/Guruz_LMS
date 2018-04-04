package lms.models

case class ItemPopularity(itemId: String, itemTitle: String, n1: Int, n2: Int) {
  def score = n1 + n2
}
