package io.udash.demos.rest.views.index

import io.udash.demos.rest.model.{Contact, PhoneBookId}
import io.udash.properties.{HasModelPropertyCreator, ModelPropertyCreator, PropertyCreator}

class DemoIndexViewModel(
  val books: DataLoadingModel[PhoneBookExtInfo] = new DataLoadingModel[PhoneBookExtInfo](),
  val contacts: DataLoadingModel[Contact] = new DataLoadingModel[Contact]()
)

object DemoIndexViewModel extends HasModelPropertyCreator[DemoIndexViewModel]

class DataLoadingModel[T](
  val loaded: Boolean = false,
  val loadingText: String = "",
  val elements: Seq[T] = Seq.empty
)
object DataLoadingModel {
  implicit def modelPropertyCreator[T : PropertyCreator]: ModelPropertyCreator[DataLoadingModel[T]] =
    ModelPropertyCreator.materialize[DataLoadingModel[T]]
}

case class PhoneBookExtInfo(id: PhoneBookId, name: String, description: String, contactsCount: Int)
object PhoneBookExtInfo extends HasModelPropertyCreator[PhoneBookExtInfo]