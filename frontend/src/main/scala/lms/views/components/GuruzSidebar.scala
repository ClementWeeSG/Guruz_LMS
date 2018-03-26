package lms.views.components

import io.udash.bootstrap.{BootstrapStyles, BootstrapTags}
import io.udash.css.{CssStyle, CssStyleName, CssView}
import io.udash.properties.HasModelPropertyCreator
import lms.config.GuruzSidebarStyles
import lms.routing._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2._

object GuruzSidebar extends CssView {

  val expanded = attr("aria-expanded")
  val toggle = BootstrapTags.dataToggle

  case class SidebarItem(icon: CssStyle, tag: String, destinationState: RoutingState)

  object SidebarItem extends HasModelPropertyCreator[SidebarItem]

  val defaultItems = Seq(
    SidebarItem(GuruzSidebarStyles.Icons.Home, "Start", IndexState),
    SidebarItem(GuruzSidebarStyles.Icons.Members, "Browse Members", MemberInfoState(None)),
    SidebarItem(GuruzSidebarStyles.Icons.Books, "Browse Collections", ItemTypeInfoState(None)),
    SidebarItem(GuruzSidebarStyles.Icons.BestReads, "View Top Reads", ItemPopularityState(None))
  )

  def iconOf(item: SidebarItem) = i(item.icon)()

  val posts = Seq(
    ("alice", "i like pie"),
    ("bob", "pie is evil i hate you"),
    ("charlie", "i like pie and pie is evil, i hat myself")
  )

  def navItem(item: SidebarItem): Modifier = {
    val linkUrl: String = item.destinationState.url
    val currentUrl: String = io.udash.routing.WindowUrlChangeProvider.currentFragment.toString
    li(
      (if (currentUrl.startsWith(linkUrl)) GuruzSidebarStyles.SidebarActive else CssStyleName("")),
      toggle := "collapse",
      expanded := "false"
    )(a(
      href := item.destinationState.url
    )(iconOf(item), item.tag)).render
  }

  def navHeader(headerName: String): Modifier = {
    div(GuruzSidebarStyles.SidebarHeader)(
      h3(headerName),
      strong(
        i(GuruzSidebarStyles.Icons.HeaderIcon)
      )
    ).render
  }

  def render(items: Seq[SidebarItem] = defaultItems, title: String = "Explore Libraries"): Modifier = {
    nav(id := "sidebar")(
      navHeader(title),
      ul(BootstrapStyles.List.listUnstyled, GuruzSidebarStyles.Components)(
        (for {item <- items} yield navItem(item)): _*
      )
    )
  }

}
