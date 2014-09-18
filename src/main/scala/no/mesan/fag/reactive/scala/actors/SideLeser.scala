package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import no.mesan.fag.reactive.scala.actors.BildeAnsvarlig.FoundImage
import no.mesan.fag.reactive.scala.tags.PageParser

/** Denne actoren scanner en enkelt side, og sender meldinger om sider og bilder den finner. */
class SideLeser(bildeAnsvarlig: ActorRef) extends Actor with ActorLogging with Tracer {
  traceCreate(s"SideLeser hos $bildeAnsvarlig")

  def scanFunction(url: String): (List[String], List[String]) =  PageParser.parseTags(url)

  override def receive = {
    case scan: ScanMessage =>
      scan.traceReceive(this)
      val (pages, images) = scanFunction(scan.url)
      pages.map(page => context.parent ! ScanMessage(page))
      images.map(image => bildeAnsvarlig ! FoundImage(image))
  }
}

/** Opprettelse. */
object SideLeser {
  def props(bildeAnsvarlig: ActorRef) = Props(classOf[SideLeser], bildeAnsvarlig)
}
