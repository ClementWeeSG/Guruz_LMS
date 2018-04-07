package lms.views.items

import io.udash.Presenter
import io.udash.core.{View, ViewFactory}
import io.udash.properties.model.ModelProperty
import io.udash.properties.seq.SeqProperty
import io.udash.properties.single.Property
import lms.api.LMSGlobal
import lms.models._
import lms.routing.ItemTypeInfoState

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ItemInfoPagePresenter extends Presenter[ItemTypeInfoState] with ViewFactory[ItemTypeInfoState] {

  val info: ModelProperty[DataLoadingModel[LibraryItemInfo]] = ModelProperty(new DataLoadingModel[LibraryItemInfo]())
  val categories = SeqProperty.empty[String]
  val selectedCategory = Property.empty[String]

  LMSGlobal.itemsAPI.types().map {
    types =>
      categories.clear()
      categories.set(types)
      println(s"Finished loading types: [$types]")
  } recover {
    case t: Throwable =>
      categories.append("<ERROR>")
  }

  override def handleState(state: ItemTypeInfoState): Unit = {
    state.itemType match {
      case None =>
      case Some("<Error>") =>
        info.subProp(_.error).set(true, true)
        info.subProp(_.loaded).set(false, true)
        info.subProp(_.loadingText).set("Error Loading Item Types", true)
      case Some(category) =>
        loadItemDetails(LMSGlobal.itemsAPI.items(category))
    }

    def loadItemDetails(elements: Future[Seq[LibraryItemInfo]]): Unit = {
      info.subProp(_.loaded).set(false)
      info.subProp(_.loadingText).set("Loading ...")

      elements onComplete {
        case Success(elems) =>
          info.subProp(_.loaded).set(true)
          info.subSeq(_.elements).clear()

          elems.foreach { el =>
            info.subSeq(_.elements).append(el)
          }
        case Failure(ex) =>
          info.subProp(_.error).set(true)
          info.subProp(_.loadingText).set(s"Error: $ex")
      }
    }
  }

  def createView() = new ItemInfoView(this)

  override def create(): (View, Presenter[ItemTypeInfoState]) = (createView(), this)
}
