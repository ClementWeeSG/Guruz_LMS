package lms.views

import io.udash._
import io.udash.bootstrap.utils.UdashListGroup
import io.udash.css.CssView
import io.udash.properties.PropertyCreator
import lms.models.SingleLoadingModel
import org.scalajs.dom.html.LI
import scalatags.JsDom.all._

object DataList {

  case class DetailsRow(property: String, value: ReadableProperty[String])

  def apply[T: PropertyCreator](model: ModelProperty[SingleLoadingModel[T]], headers: Seq[String], rowFactory: CastableProperty[T] => Seq[ReadableProperty[String]]) =
    new DataList[T](model, headers, rowFactory).render()
}

class DataList[T: PropertyCreator](model: ModelProperty[SingleLoadingModel[T]], headers: Seq[String], rowFactory: CastableProperty[T] => Seq[ReadableProperty[String]]) extends CssView {
  def render(): Modifier = {
    val displayProperty = model.subProp(_.item)
    val rowValues = rowFactory(displayProperty)
    produce(model.subProp(_.loaded)) { loaded =>
      val isError = model.subProp(_.error).get
      if (loaded && !isError) {
        renderRows(SeqProperty(compileDetailsRows(headers, rowValues)))
      } else span(bind(model.subProp(_.loadingText))).render
    }
  }

  private def compileDetailsRows(headers: Seq[String], values: Seq[ReadableProperty[String]]): Seq[DataList.DetailsRow] = {
    headers.zip(values) map {
      case (header, value: _root_.io.udash.CastableProperty[String]) => DataList.DetailsRow(header, value)
    }
  }


  def renderRow(row: DataList.DetailsRow): LI = {
    li(
      strong(s"${row.property}:"),
      span(textDecoration.underline)(bind(row.value))
    ).render
  }

  def renderRows(rows: ReadableSeqProperty[DataList.DetailsRow]) = {
    UdashListGroup(rows)(_.transform(renderRow).get).render
  }
}
