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
        <.h1(props.recipe.name),
        <.div(
          <.label("Ingredients"),
          <.ol(
            props.recipe.ingredients.map(ingredient =>
              <.li(ingredient)
            ).toVdomArray
          ),
        ),
        <.div(
            <.label("Instructions"),
            <.ol(
              props.recipe.instructions.map(instruction =>
                <.li(instruction)
              ).toVdomArray
            ),
          ),
        <.br,
        <.div(
          <.button(
            ^.onClick --> Callback.alert(props.recipe.name),
            "Edit"
          )
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Recipe")
    .renderBackend[Backend]
    .build
}
