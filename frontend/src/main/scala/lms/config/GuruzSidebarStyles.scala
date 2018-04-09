package lms.config

import io.udash.bootstrap.utils.UdashIcons
import io.udash.css.{CssStyle, CssStyleName}

object GuruzSidebarStyles {

  val SidebarHeader = CssStyleName("sidebar-header")
  val SidebarActive = CssStyleName("active")
  val SidebarCTAs = CssStyleName("CTAs")
  val Article = CssStyleName("article")
  val Download = CssStyleName("download")
  val Wrapper = CssStyleName("wrapper")
  val Components = CssStyleName("components")

  object Icons {
    private val g = UdashIcons.Glyphicon
    private val fa = UdashIcons.FontAwesome
    val HeaderIcon: CssStyle = g.search
    val Home = g.home
    val Members = fa.idBadge
    val Books = g.book
    val BestReads = g.book
    val TopReader = fa.trophy
    val Visits = fa.checkSquare
  }

}
