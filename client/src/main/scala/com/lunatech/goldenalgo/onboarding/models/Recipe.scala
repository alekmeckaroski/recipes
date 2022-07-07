package com.lunatech.goldenalgo.onboarding.models

import io.circe._
import io.circe.generic.auto._

case class Recipe(name: String, ingredients: Seq[String], instructions: Seq[String])

case class RecipeResponse(id: String, name: String, ingredients: Seq[String], instructions: Seq[String])
