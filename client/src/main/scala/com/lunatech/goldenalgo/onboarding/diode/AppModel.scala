package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import diode.Action
import diode.data.{Pot, PotAction}

case class SetLoading() extends Action
case class ClearLoading() extends Action

case class LoadRecipes() extends Action
case class FilterRecipes(query: String) extends Action
case class InitRemoveRecipe(recipeId: String) extends Action
case class CreateRecipe(recipe: Recipe) extends Action
case class ModifyRecipe(recipe: RecipeResponse) extends Action
case class LoadRecipe(recipeId: String) extends Action

case class GetRecipes(recipes: Seq[RecipeResponse]) extends Action
case class AddRecipe(recipe: RecipeResponse) extends Action
case class EditRecipe(recipe: RecipeResponse) extends Action
case class RemoveRecipe(recipeId: String) extends Action
case class GetRecipe(recipe: RecipeResponse) extends Action
