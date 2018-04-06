package lms.api

import lms.MainServerREST

import scala.scalajs.js

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
  private lazy val server = MainServerREST.instance
  val memberAPI: MemberInfoAPI = if (Global.lms.debug) DummyMemberInfoAPI else server.members()
  val itemPopularityAPI = if (Global.lms.debug) DummyItemPopularityAPI else server.popularity()
  val itemsAPI = if (Global.lms.debug) DummyItemTypeInfoAPI else server.series()
}
