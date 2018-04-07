package lms.models

import com.avsystem.commons.serialization.HasGenCodec

case class ItemPopularity(itemId: String, itemTitle: String, n1: Int, n2: Int) {
  def score = n1 + n2
}

object ItemPopularity extends HasGenCodec[ItemPopularity]