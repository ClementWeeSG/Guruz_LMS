package lms

import com.avsystem.commons.serialization.{HasGenCodec, transparent}

trait SyntheticOption[T]{
  def inner: T
  def toOption(): Option[T] = Option(inner)
}


@transparent
case class SyntheticStringOption(inner: String) extends SyntheticOption[String]
object SyntheticStringOption extends HasGenCodec[SyntheticStringOption]

@transparent
case class SyntheticIntOption(inner: Int) extends SyntheticOption[Int]
object SyntheticIntOption extends HasGenCodec[SyntheticIntOption]
