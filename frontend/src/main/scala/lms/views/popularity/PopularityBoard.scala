package lms.views.popularity

import io.udash.css.CssView
import lms.models.DataLoadingModel
import io.udash.properties.model.ModelProperty
import lms.models.ItemPopularity
import lms.views.DataTable
import scalatags.JsDom.all._

class PopularityBoard(popularityModel: ModelProperty[DataLoadingModel[ItemPopularity]]) extends CssView {
  val orderedPopularity = popularityModel.transform { unordered =>
    val orderedElems = unordered.elements.sortBy(_.score * (-1)).zipWithIndex
    new DataLoadingModel[(ItemPopularity, Int)](unordered.loaded, unordered.loadingText, orderedElems, unordered.error)
  }

  def render() = DataTable[(ItemPopularity, Int)](
    ModelProperty(orderedPopularity.get),
    Seq("Rank", "Item ID", "Item Title", "N1", "N2", "Score"),
    (prop) => {
      val data: (ItemPopularity, Int) = prop.get
      Seq(
        "#" + data._2.toString,
        data._1.itemId,
        data._1.itemTitle,
        data._1.n1.toString,
        data._1.n2.toString,
        data._1.score.toString
      )
    }
  )
}
