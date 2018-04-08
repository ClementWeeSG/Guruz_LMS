package lms.models.wishlist

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.ModelPropertyCreator

object PopularItems {

  case class ByLibrary(title: String, series: Option[String], order: Option[Int], numCopies: Int)

  object ByLibrary extends HasGenCodec[ByLibrary] {
    implicit def modelPropertyCreator = ModelPropertyCreator.materialize[ByLibrary]
  }

  case class All(library: String, title: String, series: Option[String], order: Option[Int], numCopies: Int)

  object All extends HasGenCodec[All] {
    implicit def modelPropertyCreator = ModelPropertyCreator.materialize[All]
  }

}

object SyntheticOption {

  implicit class FixedOption[T](op: Option[T]) {
    def fix() = Option(op.asInstanceOf[T])
  }
}
