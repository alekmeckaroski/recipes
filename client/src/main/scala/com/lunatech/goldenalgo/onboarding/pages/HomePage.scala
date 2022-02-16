package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.components.RecipeComponent
import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, GetRecipes}
import com.lunatech.goldenalgo.onboarding.models.{Recipe, User}
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.react.ModelProxy
import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HomePage {

  case class Props(proxy: ModelProxy[AppState], ctl: RouterCtl[Page])

  case class State(var isLoading: Boolean,
                   var user: User,
                   var recipes: List[Recipe])

  class Backend($: BackendScope[Props, State]) {

    def loadRecipes: Callback = {
      val url = "http://localhost:8080/recipes"

      def getData(): Future[List[Recipe]] = {
        dom.ext.Ajax.get(url=s"$url").map(xhr => {
          val option = decode[List[Recipe]](xhr.responseText)
          option match {
            case Left(failure) => List.empty[Recipe]
            case Right(data) => data
          }
        })
      }

      def updateState: Future[Callback] = {
        getData().map {recipes =>
          AppCircuit.dispatch(GetRecipes(recipes))
          $.modState(s => s.copy(
            isLoading =  false,
            recipes = recipes
          ))
        }
      }

      Callback.future(updateState)
    }

    def openCreateModal(): Callback = Callback.alert("Will create")

    def render(props: Props, state: State) = {
      <.div(
        <.h1("Home Page"),
        <.h2("Welcome"),
        <.h3("Your Recipes"),
        <.ul(
          state.recipes.map(recipe =>
            <.li(
              RecipeComponent.Component(RecipeComponent.Props(recipe))
            )
          ).toVdomArray
        ),
      )
      <.div(
        <.button(
          ^.onClick --> openCreateModal(),
          "Create new Recipe"
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("HomePage")
    .initialState(State(isLoading = true,
      user = User(123, "john.doe@example.com", List.empty[Recipe]),
      recipes = List.empty[Recipe]))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.loadRecipes)
    .build

  def apply(props: Props) = Component(props)
}
