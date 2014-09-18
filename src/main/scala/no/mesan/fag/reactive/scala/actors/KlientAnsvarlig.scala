package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, Actor}
import no.mesan.fag.reactive.scala.actors.BildeLager.ImageResult
import unfiltered.netty.websockets.WebSocket

/** Håndterer kommunikasjon med 1 klient */
class KlientAnsvarlig(socket: WebSocket, domain: String) extends Actor with ActorLogging with Tracer {
  traceCreate(s"KlientAnsvarlig for $socket i $domain")

  val bildeAnsvarlig = context.actorOf(BildeAnsvarlig.props(self))
  val sideAnsvarlig = context.actorOf(SideAnsvarlig.props(bildeAnsvarlig, domain))

  override def receive: Receive = {
    case scan: ScanMessage =>
      scan.traceReceive(this)
      sideAnsvarlig.forward(scan)

    case stop: StopRequest =>
      stop.traceReceive(this)
      context stop self

    case image: ImageResult =>
      traceResult(s"Kjære $socket - vi har nå funnet ${image.url} som har thumbnail ${image.thumbnail}")
      socket.send(s"""{"images":["${image.url}"]}""") // http://messier45.com/img/halebopp.jpg
  }
}

/** Opprettelse. */
object KlientAnsvarlig {
  def props(socket: WebSocket, domain: String) = Props(classOf[KlientAnsvarlig], socket, domain)
}
