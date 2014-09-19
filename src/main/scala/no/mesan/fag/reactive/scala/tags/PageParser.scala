package no.mesan.fag.reactive.scala.tags

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._

object PageParser {

  var errNo= 0

  private def parseAttrs(prefix: String, tagName: String, attrName: String, doc: Document) =
    doc.getElementsByTag(tagName)
       .iterator()
       .map(_.attr(attrName))
       .map(r=> if (r startsWith "/" ) prefix + r else r)
       .toList

  def parseTags(url: String): (List[String], List[String]) = {
    try {
      val doc= Jsoup.connect(url).timeout(10000).get()
      val aHrefs= parseAttrs(url, "a", "abs:href", doc)
      val imgSrc=  parseAttrs(url, "img", "abs:src", doc)
      (aHrefs, imgSrc)
    }
    catch {
       // Litt av en feilhåndtering... :(
      case e: Throwable =>
        errNo +=1
        println(s"ERR $errNo: ${e.getLocalizedMessage}")
        (List.empty, List.empty)
    }
  }
}
