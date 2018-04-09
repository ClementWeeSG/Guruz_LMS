package lms.views.components

import io.udash.bootstrap.{BootstrapStyles, BootstrapTags}
import io.udash.css.{CssStyle, CssView}
import io.udash.properties.HasModelPropertyCreator
import lms.ApplicationContext
import lms.config.GuruzSidebarStyles
import lms.routing._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.nav

object GuruzSidebar extends CssView {

  implicit val app = ApplicationContext.applicationInstance

  val expanded = attr("aria-expanded")
  val toggle = BootstrapTags.dataToggle

  case class SidebarItem(icon: CssStyle, tag: String, destinationState: RoutingState)

  object SidebarItem extends HasModelPropertyCreator[SidebarItem]

  val defaultItems = Seq(
    SidebarItem(GuruzSidebarStyles.Icons.Home, "Start", IndexState),
    SidebarItem(GuruzSidebarStyles.Icons.Members, "Qn 5 - Member Details", MemberInfoState(None)),
    SidebarItem(GuruzSidebarStyles.Icons.Books, "Qn 6 - Item Details", ItemTypeInfoState(None)),
    SidebarItem(GuruzSidebarStyles.Icons.BestReads, "Qn 7 - Most Popular Items", ItemPopularityState),
    SidebarItem(GuruzSidebarStyles.Icons.Visits, "Qn 8 - Custom Function: Monitor School Visits", WishListState(None))
  )

  def iconOf(item: SidebarItem) = i(item.icon)()
  def navItem(item: SidebarItem): Modifier = {
    val linkUrl: String = item.destinationState.url
    val currentUrl: String = io.udash.routing.WindowUrlChangeProvider.currentFragment.toString
    li(
      GuruzSidebarStyles.SidebarActive.styleIf(currentUrl.startsWith(linkUrl))
    )(a(
      href := item.destinationState.url
    )(span(iconOf(item), item.tag))).render
  }

  def navHeader(headerName: String): Modifier = {
    div(GuruzSidebarStyles.SidebarHeader)(
      h3(headerName),
      strong(
        i(GuruzSidebarStyles.Icons.HeaderIcon)
      )
    ).render
  }

  def render(items: Seq[SidebarItem] = defaultItems, title: String = "NLB Library Management System"): Modifier = {
    nav(id := "sidebar")(
      navHeader(title),
      ul(BootstrapStyles.List.listUnstyled, GuruzSidebarStyles.Components)(
        (for {item <- items} yield navItem(item)): _*
      )
    )
  }

}
