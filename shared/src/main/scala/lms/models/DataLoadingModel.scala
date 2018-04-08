package lms.models

import io.udash.properties.{ModelPropertyCreator, PropertyCreator}

class DataLoadingModel[T](
                           val loaded: Boolean = false,
                           val loadingText: String = "Loading ...",
                           val elements: Seq[T] = Seq.empty,
                           val error: Boolean = false
                         ) {
  def reset() = new DataLoadingModel[T]()

  def transformElems[U](change: T => U): DataLoadingModel[U] = {
    val newContents = elements.map(change)
    new DataLoadingModel[U](loaded = loaded, loadingText = loadingText, elements = newContents, error = error)
  }

  def transformSeq[U](change: Seq[T] => Seq[U]) = {
    new DataLoadingModel[U](loaded = loaded, loadingText = loadingText, elements = change(elements), error = error)
  }
}

object DataLoadingModel {
  implicit def modelPropertyCreator[T: PropertyCreator]: ModelPropertyCreator[DataLoadingModel[T]] =
    ModelPropertyCreator.materialize[DataLoadingModel[T]]
}

class SingleLoadingModel[T](
                             val item: Option[T] = None,
                             val loaded: Boolean = false,
                             val loadingText: String = "Loading ...",
                             val error: Boolean = false
                           )

object SingleLoadingModel {
  implicit def modelPropertyCreator[T: PropertyCreator]: ModelPropertyCreator[SingleLoadingModel[T]] =
    ModelPropertyCreator.materialize[SingleLoadingModel[T]]
}

