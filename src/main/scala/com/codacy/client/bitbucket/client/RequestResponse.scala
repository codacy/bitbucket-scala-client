package com.codacy.client.bitbucket.client

sealed trait RequestResponse[+A] {

  def map[B](f: A => B): RequestResponse[B] = {
    flatMap(a => RequestResponse.success(f(a)))
  }

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

  def success[A](a: A): RequestResponse[A] = SuccessfulResponse(a)

  def failure[A](message: String): RequestResponse[A] = FailedResponse(message: String)
}
