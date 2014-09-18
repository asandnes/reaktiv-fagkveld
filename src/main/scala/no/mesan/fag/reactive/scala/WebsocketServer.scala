package no.mesan.fag.reactive.scala

import akka.actor.{Props, ActorSystem}
import no.mesan.fag.reactive.scala.actors.{StopRequest, ScanRequest, TjenesteSjef}
import unfiltered.netty.websockets._
import unfiltered.request._
import unfiltered.response._
import java.net.URL

import org.json4s._
import org.json4s.native.Serialization.{read, write}

case class Input(action: String, url: String)

object WebsocketServer {
  def main(args: Array[String]) {
    var sockets = new scala.collection.mutable.ListBuffer[WebSocket]()

    val system = ActorSystem("Fagkveld")
    val service = system.actorOf(Props[TjenesteSjef], "Sjefen")

    val redirect = unfiltered.netty.cycle.Planify {
      case GET(Path("/")) => Redirect("index.html")
      case GET(Path("/test")) => {
        sockets foreach (_.send( """{"images":["http://messier45.com/img/halebopp.jpg"]}"""))
        Ok
      }
    }

    val hello = unfiltered.netty.websockets.Planify {
      case GET(Path("/pageparser")) => {
        case Open(socket) => {
          sockets += socket
        }
        case Message(socket, Text(str)) => {
          implicit val formats = DefaultFormats
          val input = read[Input](str)
          if ( input.action == "stop" )
            service ! StopRequest(socket)
          else {
            service ! ScanRequest(socket, input.url)
          }
        }
        case Close(socket) => {
          sockets -= socket
        }
        case Error(socket, e) => {
          println("error %s" format e.getMessage)
        }
      }
    }

    unfiltered.netty.Server.http(8080)
            .resources(new URL(getClass().getResource("/public/"), "."))
            .plan(redirect)
            .plan(hello)
            .run()
  }
}
