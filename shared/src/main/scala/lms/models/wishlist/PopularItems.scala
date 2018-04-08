package lms.models.wishlist

import com.avsystem.commons.serialization.{HasGenCodec, transparent}

object PopularItems {

  case class ByLibrary(title: String, series: SyntheticOption[String], order: SyntheticOption[Int], numCopies: Int)

  object ByLibrary extends HasGenCodec[ByLibrary]

  case class All(library: String, title: String, series: SyntheticOption[String], order: SyntheticOption[Int], numCopies: Int)

}

@transparent
case class SyntheticOption[T](value: T) {
  implicit def asOption: Option[T] = Option(value)
}
