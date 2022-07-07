package com.lunatech.goldenalgo.onboarding.components.recipe

import com.lunatech.goldenalgo.onboarding.models.RecipeResponse
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object ViewRecipe {

  case class Props(recipe: RecipeResponse,
                   onEdit: Callback,
                   onDelete: Callback)

  class Backend($: BackendScope[Props, Unit]) {
    def openEditModal(recipe: RecipeResponse): Callback = Callback.alert(recipe.name)

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
            ^.onClick --> props.onEdit,
            "Edit"
          ),
          <.button(
            ^.onClick --> props.onDelete,
            "Delete"
          )
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Recipe")
    .renderBackend[Backend]
    .build
}
