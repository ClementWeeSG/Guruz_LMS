package lms.models

import io.udash.properties.{ModelPropertyCreator, PropertyCreator}

class DataLoadingModel[T](
                           val loaded: Boolean = false,
                           val loadingText: String = "Loading ...",
                           val elements: Seq[T] = Seq.empty,
                           val error: Boolean = false
                         )

object DataLoadingModel {
  implicit def modelPropertyCreator[T: PropertyCreator]: ModelPropertyCreator[DataLoadingModel[T]] =
    ModelPropertyCreator.materialize[DataLoadingModel[T]]
}

class SingleLoadingModel[T](
                             val item: T,
                             val loaded: Boolean = false,
                             val loadingText: String = "Loading ...",
                             val error: Boolean = false
                           )

object SingleLoadingModel {
  implicit def modelPropertyCreator[T: PropertyCreator]: ModelPropertyCreator[SingleLoadingModel[T]] =
    ModelPropertyCreator.materialize[SingleLoadingModel[T]]
}

