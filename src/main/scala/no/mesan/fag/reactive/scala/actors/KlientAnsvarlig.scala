package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, Actor}
import unfiltered.netty.websockets.WebSocket

/** Håndterer kommunikasjon med 1 klient */
class KlientAnsvarlig(socket: WebSocket, domain: String) extends Actor with ActorLogging with Tracer {
  traceCreate(s"KlientAnsvarlig for $socket i $domain")

  override def receive: Receive = {
    // FIXME Her overtar du...
    //  Hint 1: Koden for TjenesteSjef avslører at vi trenger å agere på ScanMessage
    //  Hint 2; Ditto at den kan forwarde en StopRequest
    //  Hint 3: for å oppdatere klienten må du ha kode som ser ganske akkurat slik ut:
    //            socket.send(s"""{"images":["${<her skal URL til bildet inn>}"]}""")
    case slettDenneLinjen => println(slettDenneLinjen /*sa jeg jo*/)
  }
}

/** Opprettelse. */
object KlientAnsvarlig {
  def props(socket: WebSocket, domain: String) = Props(classOf[KlientAnsvarlig], socket, domain)
}
