package lms

import io.udash.Application
import lms.routing.{RoutingRegistryDef, RoutingState}
import lms.views.StatesToViewFactoryDef

object ApplicationContext {
  private val routingRegistry: RoutingRegistryDef = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewFactoryDef

  val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry)
}