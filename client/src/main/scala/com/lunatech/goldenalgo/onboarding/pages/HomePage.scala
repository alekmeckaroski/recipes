package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.components.recipe.ViewRecipe
import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, FilterRecipes, GetRecipes, InitRemoveRecipe, LoadRecipes}
import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.Action
import diode.react.ModelProxy
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, ScalaComponent}
import diode.react.ReactPot._

object HomePage {

  case class Props(proxy: ModelProxy[AppState], ctl: RouterCtl[Page])

  case class State(searchQuery: String)

  class Backend($: BackendScope[Props, State]) {
    def openCreateModal(): Callback = Callback.alert("Will create")

    def onSearchChange(e: ReactEventFromInput): Callback = {
      val value = e.target.value
      $.modState(s => s.copy(searchQuery = value))
    }

    def search(query: String, dispatch: Action => Callback): Callback = {
      dispatch(FilterRecipes(query))
    }

    def render(props: Props, state: State) = {
      val proxy = props.proxy()
      val dispatch: Action => Callback = props.proxy.dispatchCB

      <.div(
        <.h2("Welcome"),
        <.h3("Your Recipes"),
        <.div(
          <.input.text(
            ^.onChange ==> onSearchChange
          ),
          <.button(
            "Search",
            ^.onClick --> search(state.searchQuery, dispatch)
          )
        ),
        proxy.recipes.renderPending(_ > 100, _ => <.p("Loading...")),
        proxy.recipes.renderReady(recipes => {
          <.ul(
            recipes.items.map(recipe =>
              <.li(
                ViewRecipe.Component(ViewRecipe.Props(
                  recipe,
                  onEdit = props.ctl.set(Page.Recipe(recipe.id)),
                  onDelete = dispatch(InitRemoveRecipe(recipe.id))
                ))
              ),
            ).toVdomArray
          )
        }),
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
    .initialState(State( searchQuery = ""))
    .renderBackend[Backend]
    .componentDidMount(scope => Callback.when(scope.props.proxy.value.recipes.isEmpty)(scope.props.proxy.dispatchCB(LoadRecipes())))
    .build

  def apply(props: Props) = Component(props)
}
