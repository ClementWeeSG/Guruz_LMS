package io.udash.demos.rest

import io.udash._
import io.udash.demos.rest.views._
import io.udash.demos.rest.views.book.PhoneBookFormViewFactory
import io.udash.demos.rest.views.contact.ContactFormViewFactory
import io.udash.demos.rest.views.index.IndexViewFactory

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] = state match {
    case RootState => RootViewFactory
    case IndexState => IndexViewFactory
    case ContactFormState(contactId) => ContactFormViewFactory(contactId)
    case PhoneBookFormState(contactId) => PhoneBookFormViewFactory(contactId)
    case _ => ErrorViewFactory
  }
}