package lms

import io.udash.rest.{DefaultServerREST, Protocol, REST, RESTName}
import lms.api.{ItemPopularityAPI, MemberInfoAPI}
import org.scalajs.dom

import scala.util.Try

@REST
trait MainServerREST {
  @RESTName("members")
  def members(): MemberInfoAPI

  @RESTName("item-popularity")
  def popularity(): ItemPopularityAPI

}

object MainServerREST {
  lazy val instance: MainServerREST = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  )
}
