package lms.api

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
  val memberAPI: MemberInfoAPI = if (Global.lms.debug) DummyMemberInfoAPI else RemoteMemberInfoAPI
}
