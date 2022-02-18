package com.lunatech.goldenalgo.onboarding.models

import scala.util.Try
import com.sksamuel.elastic4s.{Hit, HitReader}
import io.circe._
import io.circe.generic.auto._

case class Recipe(name: String,
                  ingredients: Seq[String],
                  instructions: Seq[String])

case class RecipeResponse(id: String,
                          name: String,
                          ingredients: Seq[String],
                          instructions: Seq[String])

object RecipeResponse {
  implicit object RecipeResponseHitReader extends HitReader[RecipeResponse] {
    override def read(hit: Hit): Try[RecipeResponse] = {
      val source = hit.sourceAsMap
      Try(RecipeResponse(
        hit.id,
        source("name").toString,
        source("ingredients").asInstanceOf[Seq[String]],
        source("instructions").asInstanceOf[Seq[String]]
      ))
    }
  }
}


