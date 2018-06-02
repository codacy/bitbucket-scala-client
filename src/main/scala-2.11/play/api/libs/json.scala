package play.api.libs

import java.time.{Instant, LocalDateTime, ZoneOffset}

import play.api.data.validation.ValidationError

package object json {
  val JsonValidationError: ValidationError.type = play.api.data.validation.ValidationError
  val __ : JsPath.type = play.api.libs.json.JsPath

  implicit class ReadsMethods(json: Reads.type) {
    def localDateTimeReads[T](parsing: T, corrector: String => String = identity)
                             (implicit p: T => TemporalParser[LocalDateTime]): Reads[LocalDateTime] =
      new TemporalReads[T, LocalDateTime](parsing, corrector, p, { millis: Long =>
        LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)
      })
  }
}
