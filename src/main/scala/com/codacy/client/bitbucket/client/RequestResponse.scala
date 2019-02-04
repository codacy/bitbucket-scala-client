package com.codacy.client.bitbucket.client

sealed trait RequestResponse[+A] {

  def map[B](f: A => B): RequestResponse[B] = {
    flatMap(a => RequestResponse.success(f(a)))
  }

  def flatMap[B](f: A => RequestResponse[B]): RequestResponse[B] = {
    this match {
      case SuccessfulResponse(a) => f(a)
      case e: FailedResponse => e
    }
  }

}

case class SuccessfulResponse[A](value: A) extends RequestResponse[A]

case class FailedResponse(message: String) extends RequestResponse[Nothing]

object RequestResponse {

  def success[A](a: A): RequestResponse[A] = SuccessfulResponse(a)

  def failure[A](message: String): RequestResponse[A] = FailedResponse(message: String)

  def apply[A](r1: RequestResponse[Seq[A]], r2: RequestResponse[Seq[A]]): RequestResponse[Seq[A]] = {
    r1 match {
      case SuccessfulResponse(v1) =>
        r2 match {
          case SuccessfulResponse(v2) =>
            SuccessfulResponse(v1 ++ v2)
          case f @ FailedResponse(_) => f
        }
      case f @ FailedResponse(_) => f
    }
  }

}
