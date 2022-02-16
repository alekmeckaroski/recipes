package com.lunatech.goldenalgo.onboarding.models

import io.circe._
import io.circe.generic.semiauto._

case class Recipe(name: String, ingredients: Seq[String], instructions: Seq[String])

object Recipe {
  implicit val codec: Codec[Recipe] = deriveCodec[Recipe]
}
