package lms.models

import io.udash.properties.{ModelPropertyCreator, PropertyCreator}

class DataLoadingModel[T](
                           val loaded: Boolean = false,
                           val loadingText: String = "Loading ...",
                           val elements: Seq[T] = Seq.empty
                         )

object DataLoadingModel {
  implicit def modelPropertyCreator[T: PropertyCreator]: ModelPropertyCreator[DataLoadingModel[T]] =
    ModelPropertyCreator.materialize[DataLoadingModel[T]]
}