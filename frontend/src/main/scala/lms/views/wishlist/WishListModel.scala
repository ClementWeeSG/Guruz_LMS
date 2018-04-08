package lms.views.wishlist

import io.udash.ModelProperty
import io.udash.properties.ModelPropertyCreator
import lms.models.DataLoadingModel

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait WishListModel {

  import scala.concurrent.ExecutionContext.Implicits.global

  def loadAll(): Unit

  def loadForLibrary(lib: String): Unit

  def loadModel[T: ModelPropertyCreator](model: ModelProperty[DataLoadingModel[T]], result: Future[List[T]]): Unit = {
    resetModel(model)
    result onComplete {
      case Success(r) => successfullyLoad(model, r)
      case Failure(ex) => handleError(model, ex)
    }
  }

  def successfullyLoad[T: ModelPropertyCreator](model: ModelProperty[DataLoadingModel[T]], elems: List[T]): Unit = {
    model.subProp(_.loadingText).set("")
    model.subProp(_.loaded).set(true)
    model.subProp(_.error).set(false)
    model.subSeq(_.elements).set(elems)
    println(s"Wishlist: Loaded schools data successfully")
  }

  def handleError[T: ModelPropertyCreator](model: ModelProperty[DataLoadingModel[T]], ex: Throwable): Unit = {
    model.subProp(_.loadingText).set(s"Error: $ex")
    model.subProp(_.loaded).set(false)
    model.subProp(_.error).set(true)
    model.subSeq(_.elements).clear()
    println(s"Wishlist: Encountered Error while loading: $ex")
  }

  def resetModel[T: ModelPropertyCreator](model: ModelProperty[DataLoadingModel[T]]): Unit = {
    model.subProp(_.loadingText).set("Loading Wishlist schools ...")
    model.subProp(_.loaded).set(false)
    model.subProp(_.error).set(false)
    model.subSeq(_.elements).clear()
  }
}
