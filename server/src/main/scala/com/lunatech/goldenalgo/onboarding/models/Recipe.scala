package com.lunatech.goldenalgo.onboarding.models

import com.sksamuel.elastic4s.{Hit, HitReader}
import io.circe._
import io.circe.generic.semiauto._

import scala.util.Try

case class Recipe(name: String,
                  ingredients: Seq[String],
                  instructions: Seq[String],
                 )//picture: String)

object Recipe {
  implicit val encoder: Encoder[Recipe] = deriveEncoder
  implicit val decoder: Decoder[Recipe] = deriveDecoder
}

case class RecipeResponse(id: String,
                          name: String,
                          ingredients: String,
                          instructions: String)

object RecipeResponse {
  implicit object RecipeResponseHitReader extends HitReader[RecipeResponse] {
    override def read(hit: Hit): Try[RecipeResponse] = {
      val source = hit.sourceAsMap
      Try(RecipeResponse(
        hit.id,
        source("name").toString,
        source("ingredients").toString,
        source("instructions").toString))
    }
  }
}


