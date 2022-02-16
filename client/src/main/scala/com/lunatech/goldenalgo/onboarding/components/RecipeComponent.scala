package com.lunatech.goldenalgo.onboarding.components

import com.lunatech.goldenalgo.onboarding.models.Recipe
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object RecipeComponent {

  case class Props(recipe: Recipe)

  class Backend($: BackendScope[Props, Unit]) {
    def openEditModal(recipe: Recipe): Callback = Callback.alert(recipe.name)

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
          ^.onClick --> Callback.alert(props.recipe.name),
          "Delete"
        ),
        <.button(
          ^.onClick --> Callback.alert(props.recipe.name),
          "Edit"
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Recipe")
    .renderBackend[Backend]
    .build
}
