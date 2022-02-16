package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.models.{Recipe, User}
import diode.Action

case class AppState(isLoggedIn: Boolean,
                    user: Option[User],
                    isLoading: Boolean,
                    recipes: List[Recipe])

case class AppModel(state: AppState)

case class Login(user: User) extends Action
case class Logout() extends Action
case class CreateUser(user: User) extends Action
case class GetUser(userId: String) extends Action

case class SetLoading() extends Action
case class ClearLoading() extends Action

case class GetRecipes(recipes: List[Recipe]) extends Action
case class GetRecipe(recipeId: String) extends Action
case class AddRecipe(recipe: Recipe) extends Action
case class EditRecipe(recipe: Recipe) extends Action
case class RemoveRecipe(recipeId: String) extends Action
