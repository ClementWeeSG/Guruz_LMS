package lms.views.popularity

import io.udash.css.CssView
import io.udash.properties.model.ModelProperty
import lms.models.{DataLoadingModel, ItemPopularity}
import lms.views.DataTable
import scalatags.JsDom.all._

object PopularityBoard {
  def apply(popularityModel: ModelProperty[DataLoadingModel[ItemPopularity]]) = new PopularityBoard(popularityModel).render
}

class PopularityBoard(popularityModel: ModelProperty[DataLoadingModel[ItemPopularity]]) extends CssView {
  def render() = DataTable[ItemPopularity](
    popularityModel,
    Seq("Rank", "Item ID", "Item Title", "N1", "N2", "Popularity Score"),
    (prop) => {
      val data = prop.get
      Seq(
        "#" + data.rank.toString,
        data.itemId,
        data.itemTitle,
        data.n1.toString,
        data.n2.toString,
        data.score.toString
      )
    }
  )
}
