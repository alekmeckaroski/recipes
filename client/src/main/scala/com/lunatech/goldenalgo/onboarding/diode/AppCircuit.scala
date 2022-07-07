package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import diode.AnyAction.aType
import diode.{ActionHandler, Circuit, Effect, ModelRW}
import diode.data.{Empty, Pending, Pot, Ready}
import diode.react.ReactConnector
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser.decode
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData
import io.circe.syntax._

import scala.:+
import scala.concurrent.ExecutionContext.Implicits.global

case class AppState(isLoading: Boolean,
                    recipes: Pot[Recipes])

case class AppModel(state: AppState)

case class Recipes(items: Seq[RecipeResponse]) {
  def updated(newItem: RecipeResponse) = {
    items.indexWhere(_.id == newItem.id) match {
      case -1 =>
        // add new
        Recipes(items :+ newItem)
      case idx =>
        // replace old
        Recipes(items.updated(idx, newItem))
    }
  }
  def remove(item: RecipeResponse) = Recipes(items.filterNot(_ == item))
}

class RecipePageHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  val host = "http://localhost:8080"

  def loadRecipesEffect() =
    Effect(Ajax.get(url = s"$host/recipes").map(xhr => {
      val option = decode[Seq[RecipeResponse]](xhr.responseText)
      option match {
        case Left(failure) => {
          println(failure)
          GetRecipes(List.empty[RecipeResponse])
        }
        case Right(data) => GetRecipes(data)
      }
    }))

  def loadRecipeEffect(recipeId: String) =
    Effect(Ajax.get(url = s"$host/recipes/$recipeId").map(xhr => {
      val option = decode[RecipeResponse](xhr.responseText)
      option match {
        case Left(failure) => {
          println(failure)
        }
        case Right(data) => GetRecipe(data)
      }
    }))

  def filterRecipesEffect(query: String) =
    Effect(Ajax.get(url = s"$host/search/$query").map(xhr => {
      val option = decode[Seq[RecipeResponse]](xhr.responseText)
      option match {
        case Left(failure) => {
          println(failure)
          GetRecipes(List.empty[RecipeResponse])
        }
        case Right(data) => GetRecipes(data)
      }
    }))

  def deleteRecipeEffect(recipeId: String) =
    Effect(Ajax.delete(url = s"$host/recipes/$recipeId").map(xhr => {
      val option = decode[String](xhr.responseText)
      option match {
        case Left(failure) => println(failure)
        case Right(data) => RemoveRecipe(recipeId)
      }
    }))

  def createRecipeEffect(recipe: Recipe) =
    Effect(Ajax.post(url = s"$host/recipes",
      data = recipe.asJson.noSpaces
    ).map(xhr => {
      val option = decode[RecipeResponse](xhr.responseText)
      option match {
        case Left(failure) => println(failure)
        case Right(data) => AddRecipe(data)
      }
    }))

  def modifyRecipeEffect(recipe: RecipeResponse) =
    Effect(Ajax.post(url = s"$host/recipes/${recipe.id}",
      data = recipe.asJson.noSpaces
    ).map(xhr => {
      val option = decode[RecipeResponse](xhr.responseText)
      option match {
        case Left(failure) => println(failure)
        case Right(data) => EditRecipe(data)
      }
    }))

  override def handle = {
    case FilterRecipes(query) => effectOnly(filterRecipesEffect(query))
    case LoadRecipes() => effectOnly(loadRecipesEffect())
    case InitRemoveRecipe(recipeId: String) => effectOnly(deleteRecipeEffect(recipeId))
    case CreateRecipe(recipe: Recipe) => effectOnly(createRecipeEffect(recipe))
    case ModifyRecipe(recipe: RecipeResponse) => effectOnly(modifyRecipeEffect(recipe))

    case GetRecipes(recipes) => updated(value.copy(recipes = Ready(Recipes(recipes))))
    case RemoveRecipe(recipeId: String) => updated(value.copy(recipes = Ready(Recipes(value.recipes.get.items.filterNot(_.id == recipeId)))))
    case AddRecipe(recipe: RecipeResponse) => updated(value.copy(recipes = Ready(Recipes(value.recipes.get.items :+ recipe))))
    case EditRecipe(recipe: RecipeResponse) => {
      val oldRecipe = value.recipes.get.items.find(r => r.id == recipe.id).get
      val recipeIndex = value.recipes.get.items.indexOf(oldRecipe)
      updated(value.copy(recipes = Ready(Recipes(value.recipes.get.items.updated(recipeIndex, recipe)))))
    }

    case GetRecipe(recipe: RecipeResponse) => {

    }
  }
}

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetLoading() => updated(value.copy(isLoading = true))
    case ClearLoading() => updated(value.copy(isLoading = false))
  }
}

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      isLoading = false,
      recipes = Pot.empty[Recipes]
    )
  )

  override protected def actionHandler = composeHandlers(
    new RecipePageHandler(zoomTo(_.state)),
    new AppHandler(zoomTo(_.state))
  )
}
