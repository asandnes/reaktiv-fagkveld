package no.mesan.fag.reactive.scala.actors

import unfiltered.netty.websockets.WebSocket

trait ApplicationMessage extends Tracer {
  def traceReceive(who: Any): Unit = traceReceive(who, this)
}

/** Den initielle meldingen i systemet. */
case class ScanRequest(socket: WebSocket, url: String) extends ApplicationMessage
/** Scan en gitt side. */
case class ScanMessage(url: String) extends ApplicationMessage
/** Avbryt scanning. */
case class StopRequest(socket: WebSocket) extends ApplicationMessage
