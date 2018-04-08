package lms.api

import io.udash.rest.{DefaultServerREST, Protocol}
import lms.MainServerREST
import org.scalajs.dom

import scala.scalajs.js
import scala.util.Try



@js.native
@js.annotation.JSGlobalScope
object Global extends js.Object {
  def lms: js.UndefOr[LMSAPI] = js.native
}

@js.native
trait LMSAPI extends js.Object {
  def debug: js.UndefOr[Boolean] = js.native

  val base: String = js.native
}

object LMSGlobal {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val isDebug = Global.lms.flatMap(_.debug).getOrElse(false)

  if (isDebug) println("Running in Debug Mode") else println("Running in Live mode")

  lazy val server = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), s"${Global.lms.map(_.base).getOrElse("")}/api/"
  )
  val memberAPI: MemberInfoAPI = if (isDebug) DummyMemberInfoAPI else server.members()
  val itemPopularityAPI = if (isDebug) DummyItemPopularityAPI else server.popularity()
  val itemsAPI = if (isDebug) DummyItemTypeInfoAPI else server.series()
}
