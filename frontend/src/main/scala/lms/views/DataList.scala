package lms.views

import io.udash._
import io.udash.bootstrap.utils.UdashListGroup
import io.udash.css.CssView
import io.udash.properties.PropertyCreator
import lms.models.SingleLoadingModel
import org.scalajs.dom.html.LI
import scalatags.JsDom.all._

object DataList {

  type RowFactory[T] = T => Seq[Modifier]

  case class DetailsRow(property: String, value: Modifier)

  def apply[T: PropertyCreator](model: ModelProperty[SingleLoadingModel[T]], headers: Seq[String], rowFactory: RowFactory[T]) =
    new DataList[T](model, headers, rowFactory).render()
}

class DataList[T: PropertyCreator](model: ModelProperty[SingleLoadingModel[T]], headers: Seq[String], rowFactory: DataList.RowFactory[T]) extends CssView {
  def render(): Modifier = {
    val displayProperty: Property[Option[T]] = model.subProp(_.item)
    val loaded = model.subProp(_.loaded).combine(model.subProp(_.error))(_ && _)
    produce(loaded) { loaded =>
      if (loaded) {
        div(
          produce(displayProperty) { itemAsSeq =>
            itemAsSeq.headOption match {
              case None => span(bind(model.subProp(_.loadingText))).render
              case Some(view) =>
                val rows = compileDetailsRows(headers, rowFactory(view))
                renderRows(SeqProperty(rows)).render
            }
          }
        ).render
      } else span(bind(model.subProp(_.loadingText))).render
    }
  }

  def compileDetailsRows(headers: Seq[String], values: Seq[Modifier]): Seq[DataList.DetailsRow] = {
    headers.zip(values) map {
      case (header, value) => DataList.DetailsRow(header, value)
    }
  }


  def renderRow(row: DataList.DetailsRow): LI = {
    li(
      strong(s"${row.property}:"),
      span(textDecoration.underline)(row.value)
    ).render
  }

  def renderRows(rows: ReadableSeqProperty[DataList.DetailsRow]) = {
    UdashListGroup(rows)(_.transform(renderRow).get).render
  }
}
