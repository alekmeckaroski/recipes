package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.components.recipe.ViewRecipe
import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, GetRecipes, InitRemoveRecipe}
import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.react.ModelProxy
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object RecipePage {

  case class Props(proxy: ModelProxy[AppState], recipePage: Page.Recipe, ctl: RouterCtl[Page])
  case class State(recipe: RecipeResponse)

  class Backend($: BackendScope[Props, State]) {

    def initRecipe(id: String) = {
      def loadRecipe(id: String) =
        dom.ext.Ajax.get(url = s"https://localhost/recipes/$id")
          .map(xhr => {
            val option = decode[RecipeResponse](xhr.responseText)
            option match {
              case Right(data) => data
            }
          })

      def updateState(id: String): Future[Callback] = {
        loadRecipe(id).map(data => {
          $.modState(s => s.copy(recipe = data))
        })
      }

      Callback.future(updateState(id))
    }

    def render(props: Props, state: State): VdomElement = {
      <.div(
        ViewRecipe.Component(ViewRecipe.Props(
          state.recipe,
          onEdit = props.ctl.set(Page.Recipe(state.recipe.id))
        ))
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Recipe Page")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.initRecipe(scope.props.recipePage.id))
    .build
}
