package no.mesan.fag.reactive.scala.pictures

import scala.util.Random

/** Dummy utility for å lage thumbnails. */
object ThumbnailCreator {
  def create(url: String): String = {
    // TODO Juks & bedrag!
    Thread.sleep(Random.nextInt(200))
    url
  }

}
