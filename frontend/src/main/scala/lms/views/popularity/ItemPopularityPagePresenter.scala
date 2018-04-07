package lms.views.popularity

import io.udash._
import lms.api.LMSGlobal
import lms.models.{DataLoadingModel, ItemPopularity}
import lms.routing.ItemPopularityState

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ItemPopularityPagePresenter extends Presenter[ItemPopularityState.type] with ViewFactory[ItemPopularityState.type] {
  val popularItemsLoadingModel = ModelProperty(new DataLoadingModel[ItemPopularity]())
  val popularItems = popularItemsLoadingModel.subSeq(_.elements)
  val startDate: CastableProperty[String] = Property[String]("")
  val endDate: CastableProperty[String] = Property[String]("")
  val dateRange: ReadableProperty[(String, String)] = startDate.combine(endDate)((_, _))

  object DateValidator extends Validator[String] {
    val date = raw"(\d{4})-(\d{2})-(\d{2})".r

    override def apply(element: String): Future[ValidationResult] = Future.successful {
      element match {
        case "" => Valid
        case date(_, _, _) => Valid
        case _ => Invalid("Date must be in yyyy-mm-dd format!!")
      }
    }
  }

  def setup(): Unit = {
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
    popularItemsLoadingModel.subProp(_.loadingText).set("Loading...")
    popularItemsLoadingModel.subProp(_.loaded).set(false)
    popularItemsLoadingModel.subProp(_.error).set(false)
    popularItemsLoadingModel.subSeq(_.elements).clear()
  }

  def successfullyLoad(items: List[ItemPopularity]): Unit = {
    popularItemsLoadingModel.subProp(_.loadingText).set("")
    popularItemsLoadingModel.subProp(_.loaded).set(true)
    popularItemsLoadingModel.subProp(_.error).set(false)
    popularItemsLoadingModel.subSeq(_.elements).set(items)
    println(s"Most Popular Items successfully loaded: ${popularItemsLoadingModel.get.elements}")
  }

  def handle(ex: Throwable): Unit = {
    popularItemsLoadingModel.subProp(_.loadingText).set(s"Error: $ex")
    popularItemsLoadingModel.subProp(_.loaded).set(false)
    popularItemsLoadingModel.subProp(_.error).set(true)
    popularItemsLoadingModel.subSeq(_.elements).clear()
    println(s"ItemPopularity: Encountered Error while loading: $ex")
  }

  def reloadTable(start: String = startDate.get, end: String = endDate.get)(implicit executionContext: ExecutionContext): Unit = {
    resetLoadingModel()
    println("ItemPopularity: Reloading Data for Table...")
    LMSGlobal.itemPopularityAPI.getTop3Books(startDate.get, end).onComplete {
      case Success(items) => successfullyLoad(items)
      case Failure(ex) => handle(ex)
    }
  }

  override def handleState(state: ItemPopularityState.type): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    startDate.set("")
    endDate.set("")
    println("ItemPopularity: Setting up Popularity View")
    setup()
    println("ItemPopularity: Initialized Popularity View Successfully")
    reloadTable()
    println("ItemPopularity: Data loaded successfully.")
    dateRange.listen { case (start, end) => reloadTable(start, end) }
    println("ItemPopularity: Set up listeners")
  }

  override def create(): (View, Presenter[ItemPopularityState.type]) = (new ItemPopularityView(this), this)
}
