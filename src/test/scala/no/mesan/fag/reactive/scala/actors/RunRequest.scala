package no.mesan.fag.reactive.scala.actors

import akka.actor.{PoisonPill, Props, ActorSystem}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/** For å prøvekjøre systemet :) */
object RunRequest extends App {

//  val system = ActorSystem("Fagkveld")
//  val service = system.actorOf(Props[TjenesteSjef], "Sjefen")
//  private val replyTo = "url.til.klient"
//  service ! ScanRequest(replyTo, "http://www.mesan.no")
//  system.scheduler.scheduleOnce(2 seconds) {
//    service ! StopRequest(replyTo)
//    system.scheduler.scheduleOnce(1 second) {
//      service ! PoisonPill
//      system.scheduler.scheduleOnce(1 second) {
//        system.shutdown
//      }
//    }
//  }

}
