package lms

import io.udash.Application
import io.udash.demos.rest.MainServerREST
import lms.routing.{RoutingRegistryDef, RoutingState}
import lms.views.StatesToViewFactoryDef
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object ApplicationContext {
  private val routingRegistry = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewFactoryDef

  val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry)

  import io.udash.rest._
  val restServer: MainServerREST = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  )
}