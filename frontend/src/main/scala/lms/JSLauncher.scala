package lms

import io.udash.logging.CrossLogging
import io.udash.wrappers.jquery._
import lms.ApplicationContext.applicationInstance
import org.scalajs.dom.Element

import scala.scalajs.js.annotation.JSExport

object JSLauncher extends CrossLogging {

  @JSExport
  def main(args: Array[String]): Unit = {
    jQ((_: Element) => {
      jQ("#application").get(0) match {
        case None =>
          logger.error("Application root element not found! Check your index.html file!")
        case Some(root) =>
          try {
            applicationInstance.run(root)
          } catch {
            case ex: Exception =>
              ex.getStackTrace.foreach(elem => println(elem.toString))
              ex.getCause.getStackTrace.foreach(elem => println(elem.toString))
              ex.printStackTrace()
              ex.getCause.printStackTrace()
          }

      }
    })
  }
}