package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, ActorRef, Actor}
import unfiltered.netty.websockets.WebSocket

/**
 * Toppnivå i løsningen.
 * Denne actoren mottar eksterne kall og sender dem til en klientspesifikk actor.
 */
class TjenesteSjef extends Actor with ActorLogging with Tracer {
  traceCreate(s"Tjenestesjef")

  var handlers: Map[WebSocket, ActorRef] = Map.empty

  def receive = {
    case scan: ScanRequest =>
      scan.traceReceive(this)
      val domain = UrlyBurd.getHostPart(scan.url)
      val handler = context.actorOf(KlientAnsvarlig.props(scan.socket, domain))
      handler ! ScanMessage(scan.url)
      handlers += scan.socket -> handler

    case stop: StopRequest =>
      stop.traceReceive(this)
      handlers.get(stop.socket).foreach(_ ! stop)
  }
}

object UrlyBurd { // Fra http://stackoverflow.com/questions/7586605/scala-pattern-matching-against-urls
  def unapply(in: java.net.URL) = Some((
    in.getProtocol,
    in.getHost,
    in.getPort,
    in.getPath
  ))

  def getHostPart(url: String) = {
    val u = new java.net.URL(url)
    val h= u match {
      case UrlyBurd(_, host, _, _) => host
    }
    // Oversett www.mesan.no til mesan.no
    h.replaceAll("^.*?([^.]+[.][^.]+)$", "$1")
  }
}
