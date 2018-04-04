package lms.views.popularity

import io.udash._
import lms.api.LMSGlobal
import lms.models.{DataLoadingModel, ItemPopularity}
import lms.routing.ItemPopularityState

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ItemPopularityPagePresenter extends Presenter[ItemPopularityState.type] {
  val popularItems = ModelProperty(new DataLoadingModel[ItemPopularity]())
  val startDate: CastableProperty[String] = Property[String]("")
  val endDate: CastableProperty[String] = Property[String]("")

  object DateValidator extends Validator[String] {
    val date = raw"(\d{4})-(\d{2})-(\d{2})".r

    override def apply(element: String): Future[ValidationResult] = Future.successful {
      element match {
        case date(_, _, _) => Valid
        case _ => Invalid("Date must be in yyyy-mm-dd format!!")
      }
    }
  }

  def setUp(): Unit = {
    startDate.listen(start => reloadTable(start))
    endDate.listen(end => reloadTable(end = end))
    startDate.addValidator(DateValidator)
    endDate.addValidator(DateValidator)
    setStart("1900-01-01")
    setEnd("2099-12-31")
  }

  def setStart(start: String): Unit = {
    startDate.set(start)
  }

  def setEnd(end: String): Unit = {
    endDate.set(end)
  }

  def resetLoadingModel(): Unit = {
    popularItems.subProp(_.loadingText).set("Loading...")
    popularItems.subProp(_.loaded).set(false)
    popularItems.subProp(_.error).set(false)
    popularItems.subSeq(_.elements).clear()
  }

  def successfullyLoad(items: List[ItemPopularity]): Unit = {
    popularItems.subProp(_.loadingText).set("")
    popularItems.subProp(_.loaded).set(true)
    popularItems.subProp(_.error).set(false)
    popularItems.subSeq(_.elements).append(items: _*)
  }

  def handle(ex: Throwable): Unit = {
    popularItems.subProp(_.loadingText).set(s"Error: $ex")
    popularItems.subProp(_.loaded).set(false)
    popularItems.subProp(_.error).set(true)
    popularItems.subSeq(_.elements).clear()
  }

  def reloadTable(start: String = startDate.get, end: String = endDate.get): Unit = {
    resetLoadingModel()
    LMSGlobal.itemPopularityAPI.getTop3Books(startDate.get, end).onComplete {
      case Success(items) => successfullyLoad(items)
      case Failure(ex) => handle(ex)
    }
  }

  override def handleState(state: ItemPopularityState.type): Unit = {
    startDate.set("")
    endDate.set("")
  }

}
