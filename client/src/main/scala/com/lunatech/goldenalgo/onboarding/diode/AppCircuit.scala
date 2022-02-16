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
      user = None,
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
  }
}

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case Login(user) => updated(value.copy(isLoggedIn = true, user = Some(user)))
    case Logout => updated(value.copy(isLoggedIn = false, user = None))
    case SetLoading() => updated(value.copy(isLoading = true))
    case ClearLoading() => updated(value.copy(isLoading = false))
  }
}
