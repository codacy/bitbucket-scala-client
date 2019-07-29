package com.codacy.client.bitbucket.v2

sealed trait Role {
  def value: String
}

case object Member extends Role {
  override def value: String = "member"
}

case object Contributor extends Role {
  override def value: String = "contributor"
}

case object Admin extends Role {
  override def value: String = "admin"
}

case object Owner extends Role {
  override def value: String = "owner"
}