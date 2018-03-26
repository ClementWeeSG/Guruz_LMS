package io.udash.demos.rest.views.components

import io.udash._
import io.udash.bootstrap.navs.{UdashNav, UdashNavbar}
import io.udash.demos.rest.config.ExternalUrls
import lms.ApplicationContext
import lms.routing.IndexState
import org.scalajs.dom.raw.Element
import scalatags.JsDom.all._

object Header {
  class NavItem(val href: String, val imageSrc: String, val name: String)

  private val brand = a(href := IndexState.url(ApplicationContext.applicationInstance))(
    Image("udash_logo_m.png", "Udash Framework", style := "height: 44px; margin-top: 10px;")
  ).render

  private val navItems = SeqProperty(
    new NavItem(ExternalUrls.udashGithub, "icon_github.png", "Github"),
    new NavItem(ExternalUrls.stackoverflow, "icon_stackoverflow.png", "StackOverflow"),
    new NavItem(ExternalUrls.avsystem, "icon_avsystem.png", "Proudly made by AVSystem")
  )

  private val nav = UdashNav.navbar(navItems)(
    (prop) => {
      val item = prop.get
      a(href := item.href, target := "_blank")(
        Image(item.imageSrc, item.name)
      ).render
    }
  )

  private val header = UdashNavbar.inverted(
    brand = brand,
    nav = nav
  )

  val getTemplate: Element =
    header.render
}