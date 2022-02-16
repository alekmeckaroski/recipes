package com.lunatech.goldenalgo.onboarding.components

import com.lunatech.goldenalgo.onboarding.models.Recipe
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object Modal {
  case class Props(recipe: Recipe)

  def saveChanges: Callback = {
    Callback.alert("Save")
  }

  def discardChanges: Callback = {
    Callback.alert("Discard")
  }

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props): VdomElement = {
      <.div(
        <.h1(props.recipe.name)
      )
      <.div(
        <.label("Ingredients"),
        <.ol(
          props.recipe.ingredients.toVdomArray
        )
      )
      <.div(
        <.label("Instructions"),
        <.ol(
          props.recipe.instructions.toVdomArray
        )
      )
      <.div(
        <.button(
          ^.onClick --> discardChanges,
          "Cancel"
        ),
        <.button(
          ^.onClick --> saveChanges,
          "Confirm"
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("RecipeModal")
    .renderBackend[Backend]
    .build
}
