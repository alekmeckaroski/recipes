package com.lunatech.goldenalgo.onboarding.components.recipe

import com.lunatech.goldenalgo.onboarding.models.Recipe
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object CreateRecipeModal {
  case class Props(recipe: Recipe)

  def saveChanges: Callback = {
    Callback.alert("Save")
  }

  def discardChanges: Callback = {
    Callback.alert("Discard")
  }

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props): VdomElement = {
      <.div("sdfsf")
    }
  }

  val Component = ScalaComponent.builder[Props]("RecipeModal")
    .renderBackend[Backend]
    .build
}
