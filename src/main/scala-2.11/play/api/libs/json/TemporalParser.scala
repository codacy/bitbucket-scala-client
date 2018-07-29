package play.api.libs.json

import java.time.temporal.Temporal

import play.api.data.validation.ValidationError

import scala.language.implicitConversions

trait TemporalParser[T <: Temporal] {
  def parse(input: String): Option[T]
}

private[json] final class TemporalReads[A, B <: Temporal](
                                                           parsing: A,
                                                           corrector: String => String,
                                                           p: A => TemporalParser[B],
                                                           epoch: Long => B
                                                         ) extends Reads[B] {
  def reads(json: JsValue): JsResult[B] = json match {
    case JsNumber(d) => JsSuccess(epoch(d.toLong))
    case JsString(s) => p(parsing).parse(corrector(s)) match {
      case Some(d) => JsSuccess(d)
      case None => JsError(Seq(JsPath ->
        Seq(ValidationError("error.expected.date.isoformat", parsing))))
    }
    case _ => JsError(Seq(JsPath ->
      Seq(ValidationError("error.expected.date"))))
  }
}
