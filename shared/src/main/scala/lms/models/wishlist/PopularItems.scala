package lms.models.wishlist

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.ModelPropertyCreator
import com.avsystem.commons.misc.Opt

object PopularItems {

  case class ByLibrary(series: Opt[String], order: Opt[Int], title: String, numCopies: Int)

  object ByLibrary extends HasGenCodec[ByLibrary] {
    implicit def modelPropertyCreator = ModelPropertyCreator.materialize[ByLibrary]
  }

  case class All(library: String, series: Opt[String], order: Opt[Int], title: String, numCopies: Int)

  object All extends HasGenCodec[All] {
    implicit def modelPropertyCreator = ModelPropertyCreator.materialize[All]
  }

}


