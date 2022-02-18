package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, GetRecipes}
import com.lunatech.goldenalgo.onboarding.models.Recipe
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.react.ModelProxy
import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom

import scala.concurrent.Future

object RecipePage {

  case class Props(proxy: ModelProxy[AppState], recipePage: Page.Recipe, ctl: RouterCtl[Page])
  case class State(recipe: Recipe)

  class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement = {
      <.div("sfsdf")
    }
  }

  val Component = ScalaComponent.builder[Props]("Recipe Page")
    .renderBackend[Backend]
//    .componentDidMount(scope => scope.backend)
    .build
}
