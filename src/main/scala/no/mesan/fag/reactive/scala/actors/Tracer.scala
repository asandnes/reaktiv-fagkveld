package no.mesan.fag.reactive.scala.actors

/** Kommenter bort den debuggingen du ikke vil ha... */
trait Tracer {
  private def trace(s: String) = println(s)

  def traceReceive(who: Any, msg: Any) = trace(s"\t$who received: $msg")
  def traceResult(s: String) = trace(s)
  def traceCreate(s: String) = trace(s"\t$s created")
  def traceDebug(s: String) = trace(s"\t$s")
}

