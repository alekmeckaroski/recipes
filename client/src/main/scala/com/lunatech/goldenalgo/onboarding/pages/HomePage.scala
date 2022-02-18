package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.components.RecipeComponent
import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, GetRecipes, RemoveRecipe}
import com.lunatech.goldenalgo.onboarding.models.{Recipe}
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.react.ModelProxy
import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, ScalaComponent}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HomePage {

  case class Props(proxy: ModelProxy[AppState], ctl: RouterCtl[Page])

  case class State(isLoading: Boolean,
                   searchQuery: String,
                   recipes: List[Recipe])

  class Backend($: BackendScope[Props, State]) {
    val host = "http://localhost:8080"

    def loadRecipes: Callback = {
      val url = "http://localhost:8080/recipes"

      def getData(): Future[List[Recipe]] = {
        dom.ext.Ajax.get(url=s"$url").map(xhr => {
          val option = decode[List[Recipe]](xhr.responseText)
          option match {
            case Left(failure) => {
              println(failure)
              List.empty[Recipe] }
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

    def onDelete(name: String): Callback = {
      val url = "http://localhost:8080/recipes"
      dom.ext.Ajax.delete(url=s"$url/$name").map(xhr => {
        val option = decode[String](xhr.responseText)
        option match {
          case Left(failure) => "Fail"
          case Right(data) => {
            println(data)
            AppCircuit.dispatch(RemoveRecipe(name))
          }
        }
      })

      loadRecipes
    }

    def onSearchChange(e: ReactEventFromInput): Callback = {
      val value = e.target.value
      $.modState(s => s.copy(searchQuery = value))
    }

    def onSearchClick(query: String): Callback = {
      def searchResult = dom.ext.Ajax.get(url=s"$host/search/$query").map(xhr => {
        val option = decode[List[Recipe]](xhr.responseText)
        option match {
          case Left(failure) => {
            println(failure)
            List.empty[Recipe] }
          case Right(data) => data
        }
      })

      def updateResults = {
        searchResult.map {recipes =>
          $.modState(s => s.copy(
            recipes = recipes
          ))
        }
      }

      Callback.future(updateResults)
    }

    def render(props: Props, state: State) = {
      <.div(
        <.h2("Welcome"),
        <.h3("Your Recipes"),
        <.div(
          <.input.text(
            ^.onChange ==> onSearchChange
          ),
          <.button(
            "Search",
            ^.onClick --> onSearchClick(state.searchQuery)
          )
        ),
        <.ul(
          state.recipes.map(recipe =>
            <.li(
              RecipeComponent.Component(RecipeComponent.Props(recipe))
            ),
          ).toVdomArray
        ),
        <.div(
          <.button(
            ^.onClick --> openCreateModal(),
            "Create new Recipe"
          )
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("HomePage")
    .initialState(State(
      isLoading = true,
      searchQuery = "",
      recipes = List.empty[Recipe]))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.loadRecipes)
    .build

  def apply(props: Props) = Component(props)
}
