package com.codacy.client.bitbucket.client

sealed trait RequestResponse[+A] {

  @deprecated("This should be removed or re-implemented to support pagination", "12.0.0")
  def map[B](f: A => B): RequestResponse[B] = {
    flatMap(a => RequestResponse.success(f(a)))
  }

  @deprecated("This should be removed or re-implemented to support pagination", "12.0.0")
  def flatMap[B](f: A => RequestResponse[B]): RequestResponse[B] = {
    this match {
      case SuccessfulResponse(a, _, _, _, _, _) => f(a)
      case e: FailedResponse => e
    }
  }

}

case class SuccessfulResponse[A](
    value: A,
    size: Option[Int] = None,
    pageLen: Option[Int] = None,
    page: Option[Int] = None,
    next: Option[String] = None,
    previous: Option[String] = None
) extends RequestResponse[A]

case class FailedResponse(message: String) extends RequestResponse[Nothing]

object RequestResponse {

  @deprecated("Build a SuccessfulResponse object instead of using this", "12.0.0")
  def success[A](a: A): RequestResponse[A] = SuccessfulResponse(a)

  @deprecated("Build a FailedResponse object instead of using this", "12.0.0")
  def failure[A](message: String): RequestResponse[A] = FailedResponse(message: String)

  @deprecated("This should be either removed or re-implemented to support pagination", "12.0.0")
  def applyDiscardingPaginationInfo[A](
      r1: RequestResponse[Seq[A]],
      r2: RequestResponse[Seq[A]]
  ): RequestResponse[Seq[A]] = {
    r1 match {
      case SuccessfulResponse(v1, _, _, _, _, _) =>
        r2 match {
          case SuccessfulResponse(v2, _, _, _, _, _) =>
            SuccessfulResponse(v1 ++ v2)
          case f @ FailedResponse(_) => f
        }
      case f @ FailedResponse(_) => f
    }
  }

}
