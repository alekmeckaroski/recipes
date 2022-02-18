package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.models.{Recipe}
import diode.Action

case class AppState(isLoggedIn: Boolean,
                    isLoading: Boolean,
                    recipes: List[Recipe])

case class AppModel(state: AppState)

case class SetLoading() extends Action
case class ClearLoading() extends Action

case class GetRecipes(recipes: List[Recipe]) extends Action
case class GetRecipe(recipeId: String) extends Action
case class AddRecipe(recipe: Recipe) extends Action
case class EditRecipe(recipe: Recipe) extends Action
case class RemoveRecipe(recipeName: String) extends Action
