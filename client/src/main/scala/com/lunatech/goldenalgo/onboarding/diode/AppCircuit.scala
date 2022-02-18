package com.lunatech.goldenalgo.onboarding.diode

import com.lunatech.goldenalgo.onboarding.models.Recipe
import diode._
import diode.data.Pot
import diode.react.ReactConnector
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      isLoggedIn = false,
      isLoading = false,
      recipes = List.empty[Recipe]
    )
  )

//  def getRecipesEffect()= Effect(Ajax.get("/recipes").map(r => GetRecipes(r.asInstanceOf[List[Recipe]])))

  override protected def actionHandler = composeHandlers(
    new RecipePageHandler(zoomTo(_.state)),
    new AppHandler(zoomTo(_.state))
  )
}

class RecipePageHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case GetRecipes(recipes) => updated(value.copy(recipes = recipes))
    case RemoveRecipe(recipeName: String) => updated(value.copy(recipes = value.recipes.filterNot(_.name != recipeName)))
    case AddRecipe(recipe: Recipe) => updated(value.copy(recipes = value.recipes :+ recipe))
    case EditRecipe(recipe: Recipe) => {
      val oldRecipe = value.recipes.find(r => r.name == recipe.name).get
      updated(value.copy(recipes = value.recipes.updated(value.recipes.indexOf(oldRecipe), recipe)))
    }
  }
}

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetLoading() => updated(value.copy(isLoading = true))
    case ClearLoading() => updated(value.copy(isLoading = false))
  }
}
