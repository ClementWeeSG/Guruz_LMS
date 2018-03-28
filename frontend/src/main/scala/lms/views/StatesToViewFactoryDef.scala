package lms.views

import io.udash._
import io.udash.demos.rest.views._
import io.udash.demos.rest.views.contact.ContactFormViewFactory
import io.udash.demos.rest.views.index.DemoIndexViewFactory
import lms.routing._
import lms.views.book.PhoneBookFormViewFactory
import lms.views.memberinfo.MemberInfoPagePresenter

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] = state match {
    case RootState => RootViewFactory
    case IndexState => DemoIndexViewFactory
    case ContactFormState(contactId) => ContactFormViewFactory(contactId)
    case PhoneBookFormState(contactId) => PhoneBookFormViewFactory(contactId)
    case MemberInfoState(cardId) => new MemberInfoPagePresenter
    case _ => ErrorViewFactory
  }
}