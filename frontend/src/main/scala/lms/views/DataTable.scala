package lms.views

import io.udash._
import io.udash.bootstrap.table.UdashTable
import io.udash.css.CssView
import io.udash.properties.PropertyCreator
import io.udash.properties.model.ModelProperty
import lms.models.DataLoadingModel
import scalatags.JsDom.all._

object DataTable {
  def apply[T: PropertyCreator](model: ModelProperty[DataLoadingModel[T]], headers: Seq[String], tableElementsFactory: CastableProperty[T] => Seq[Modifier]) =
    new DataTable(model, headers, tableElementsFactory).render
}

class DataTable[T: PropertyCreator](model: ModelProperty[DataLoadingModel[T]], headers: Seq[String], tableElementsFactory: CastableProperty[T] => Seq[Modifier]) extends CssView {
  def render(): Modifier = {
    val loadSuccess: ReadableProperty[Boolean] = model.subProp(_.loaded).combine(model.subProp(_.error).transform(b => !b))(_ && _)
    produce(loadSuccess) { loaded =>
      //println(s"Item Popularity is loaded: $loaded")
      if (loaded) {
        UdashTable(hover = Property(true), bordered = Property(true))(model.subSeq(_.elements))(
          rowFactory = (p) => tr(tableElementsFactory(p).map(name => td(name))).render,
          headerFactory = Some(() => tr(headers.map((name) => th(name))).render)
        ).render
      } else span(bind(model.subProp(_.loadingText))).render
    }
  }
}
