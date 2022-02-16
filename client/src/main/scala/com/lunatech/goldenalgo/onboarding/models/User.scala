package com.lunatech.goldenalgo.onboarding.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class User(
                 id: Long,
                 email: String,
                 recipes: List[Recipe]
               )

object User {
  implicit val codec: Codec[User] = deriveCodec[User]
}
