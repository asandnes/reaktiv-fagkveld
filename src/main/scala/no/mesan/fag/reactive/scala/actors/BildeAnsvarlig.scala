package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}

/** Holder orden på bildene vi har funnet. Lager nye thumbnails ved behov. */
class BildeAnsvarlig(klientAnsvarlig: ActorRef) extends Actor with ActorLogging with Tracer {
  import BildeAnsvarlig.FoundImage
  traceCreate(s"Bildeansvarlig for $klientAnsvarlig")

  def withFoundImages(foundImages: Set[String]): Receive = {
    case found: FoundImage =>
      def isANewImage(url: String) = !(foundImages contains url)
      if (isANewImage(found.url)) {
        found.traceReceive(this)
        val reader = context.actorOf(BildeLager.props(klientAnsvarlig))
        reader ! found
      }
      else
        found.traceReceive("ignored")
      context become withFoundImages(foundImages + found.url)
  }

  override def receive = withFoundImages(Set.empty)
}

/** Opprettelse og meldinger. */
object BildeAnsvarlig {
  /** Melding om et bilde som skal scannes. */
  case class FoundImage(url: String) extends ApplicationMessage

  /** Props for ny forekomst. */
  def props(klientAnsvarlig: ActorRef) = Props(classOf[BildeAnsvarlig], klientAnsvarlig)
}
