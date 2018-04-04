package lms.views.popularity

import io.udash._
import lms.api.LMSGlobal
import lms.models.ItemPopularity
import lms.routing.ItemPopularityState

class ItemPopularityPagePresenter extends Presenter[ItemPopularityState.type] {
  val popularItems = Property[List[ItemPopularity]](List.empty)
  val startDate: CastableProperty[String] = Property[String]("")
  val endDate: CastableProperty[String] = Property[String]("")

  startDate.listen(start =>
    LMSGlobal.itemPopularityAPI.getTop3Books(start, endDate.get).map(items => popularItems.set(items))
  )

  endDate.listen(
    end =>
      LMSGlobal.itemPopularityAPI.getTop3Books(startDate.get, end).map(items => popularItems.set(items))
  )

  def setStart(start: String): Unit = {
    startDate.set(start)
  }

  def setEnd(end: String): Unit = {
    endDate.set(end)
  }

  override def handleState(state: ItemPopularityState.type): Unit = {
    startDate.set("")
    endDate.set("")
  }

}
