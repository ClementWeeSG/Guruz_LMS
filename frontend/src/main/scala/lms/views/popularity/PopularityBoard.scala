package lms.views.popularity

import io.udash.css.CssView
import lms.models.DataLoadingModel
import io.udash.properties.model.ModelProperty
import lms.models.ItemPopularity
import lms.views.DataTable
import scalatags.JsDom.all._

object PopularityBoard {
  def apply(popularityModel: ModelProperty[DataLoadingModel[ItemPopularity]]) = new PopularityBoard(popularityModel).render
}

class PopularityBoard(popularityModel: ModelProperty[DataLoadingModel[ItemPopularity]]) extends CssView {
  val orderedPopularity = ModelProperty.empty[DataLoadingModel[(ItemPopularity, Int)]]

  setup()

  def setup(): Unit = {
    popularityModel.listen { (dlm: DataLoadingModel[ItemPopularity]) =>
      orderedPopularity.subProp(_.error).set(dlm.error)
      val loadingStatus = dlm.loaded
      orderedPopularity.subProp(_.loaded).set(loadingStatus)
      orderedPopularity.subProp(_.loadingText).set(dlm.loadingText)
      orderedPopularity.subProp(_.elements).set(dlm.elements.zipWithIndex)
    }
    println("Item Popularity: Popularity Board: Set up all listeners properly")
  }


  def render() = DataTable[(ItemPopularity, Int)](
    orderedPopularity,
    Seq("Rank", "Item ID", "Item Title", "N1", "N2", "Score"),
    (prop) => {
      val data: (ItemPopularity, Int) = prop.get
      Seq(
        "#" + (data._2 + 1).toString,
        data._1.itemId,
        data._1.itemTitle,
        data._1.n1.toString,
        data._1.n2.toString,
        data._1.score.toString
      )
    }
  )
}
