package lms.models

import com.avsystem.commons.serialization.HasGenCodec

case class ItemPopularity(itemId: String, itemTitle: String, n1: Int, n2: Int, score: Int, rank: Int)

object ItemPopularity extends HasGenCodec[ItemPopularity]