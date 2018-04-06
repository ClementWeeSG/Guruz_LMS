package lms.api

import io.udash.rest.{DefaultServerREST, Protocol}
import lms.MainServerREST
import org.scalajs.dom

import scala.scalajs.js
import scala.util.Try



@js.native
@js.annotation.JSGlobalScope
object Global extends js.Object {
  def lms: LMSAPI = js.native
}

@js.native
trait LMSAPI extends js.Object {
  def debug: Boolean = js.native
}

object LMSGlobal {
  import scala.concurrent.ExecutionContext.Implicits.global

  lazy val server = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  )
  val memberAPI: MemberInfoAPI = if (Global.lms.debug) DummyMemberInfoAPI else server.members()
  val itemPopularityAPI = if (Global.lms.debug) DummyItemPopularityAPI else server.popularity()
  val itemsAPI = if (Global.lms.debug) DummyItemTypeInfoAPI else server.series()
}
