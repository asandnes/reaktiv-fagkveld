package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}

/** Holder orden på sidene vi har scannet, starter nye scans ved behov. */
class SideAnsvarlig(bildeAnsvarlig: ActorRef, domain: String) extends Actor with ActorLogging with Tracer {
  traceCreate(s"SideAnsvarlig for $bildeAnsvarlig i $domain")

  def withFoundPages(foundPages: Set[String]): Receive = {
    case scan: ScanMessage =>
      def isANewPage(url: String) = !(foundPages contains url)
      def isInSameDomain(url: String) = url contains domain
      val url = scan.url.replaceAll("[?].*$", "")  // Ta bort queryparam

      if (isANewPage(url) && isInSameDomain(url)) {
        scan.traceReceive(this)
        val reader = context.actorOf(SideLeser.props(bildeAnsvarlig))
        reader ! scan
      }
      else scan.traceReceive("ignoring")

      context become withFoundPages(foundPages + url)
  }

  override def receive = withFoundPages(Set.empty)
}

/** Opprettelse og meldinger. */
object SideAnsvarlig {
  def props(bildeAnsvarlig: ActorRef, domain: String) =
    Props(classOf[SideAnsvarlig], bildeAnsvarlig, domain)
}
