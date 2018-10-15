package com.codacy.client.util

import play.api.libs.json.Format

trait JsonEnumeration extends Enumeration {
  self: Enumeration =>

  implicit lazy val format = Format(Implicits.enumReads(self), Implicits.enumWrites)

  def findByName(name: String): Option[Value] = {
    values.find(p => p.toString.toLowerCase == name.toLowerCase)
  }

}