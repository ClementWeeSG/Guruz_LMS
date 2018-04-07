package io.udash.demos.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import io.udash.demos.rest.api.{PhoneBookWebServiceSpec}
import io.udash.logging.CrossLogging

import scala.io.StdIn

object Launcher extends CrossLogging {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val service = PhoneBookWebServiceSpec
    val bindingFuture = Http().bindAndHandle(service.route, "localhost", 8080)

    logger.info(s"Server online at http://localhost:8080/\nPress Enter to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}

       