package lms

import io.udash.Application
import io.udash.rest.{DefaultServerREST, Protocol}
import lms.routing.{RoutingRegistryDef, RoutingState}
import lms.views.StatesToViewFactoryDef
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object ApplicationContext {
  private val routingRegistry = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewFactoryDef

  val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry)
}