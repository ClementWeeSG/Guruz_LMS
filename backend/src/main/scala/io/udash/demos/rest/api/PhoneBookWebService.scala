package io.udash.demos.rest.api

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import com.avsystem.commons.serialization.GenCodec
import io.udash.rpc.DefaultUdashSerialization


object PhoneBookWebServiceSpec extends DefaultUdashSerialization {
  private val staticsDir = "frontend/target/UdashStatics/WebContent"

  implicit def optionMarshaller[T](implicit codec: GenCodec[T]): ToResponseMarshaller[Option[T]] =
    gencodecMarshaller[Option[T]](GenCodec.optionCodec(codec))

  implicit def gencodecMarshaller[T](implicit codec: GenCodec[T]): ToEntityMarshaller[T] =
    Marshaller.withFixedContentType(MediaTypes.`application/json`) { value =>
      var string: String = null
      val output = outputSerialization((serialized) => string = serialized)
      codec.write(output, value)
      HttpEntity(MediaTypes.`application/json`, string)
    }

  implicit def gencodecUnmarshaller[T](implicit codec: GenCodec[T]): FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(MediaTypes.`application/json`).map { data =>
      val input = inputSerialization(data)
      val out: T = codec.read(input)
      out
    }

  val route = {
    path("") {
      getFromFile(s"$staticsDir/index.html")
    } ~
      pathPrefix("scripts") {
        getFromDirectory(s"$staticsDir/scripts")
      } ~
      pathPrefix("assets") {
        getFromDirectory(s"$staticsDir/assets")
      }
  }
}
