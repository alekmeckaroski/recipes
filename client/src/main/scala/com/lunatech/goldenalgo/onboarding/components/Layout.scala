package com.lunatech.goldenalgo.onboarding.components

import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState, GetRecipes}
import com.lunatech.goldenalgo.onboarding.models.Recipe
import com.lunatech.goldenalgo.onboarding.pages.HomePage
import com.lunatech.goldenalgo.onboarding.router.AppRouter.{Page, connection}
import diode.react.ModelProxy
import io.circe.generic.auto._
import io.circe.parser.decode
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

object Layout {
  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[Page],
                    resolution: Resolution[Page])

  class Backend(bs: BackendScope[Props, Unit]) {
    val host: String = "http://localhost:8080"

    def mounted: Callback = loadRecipes()

    def loadRecipes(): Callback = Callback {
      dom.ext.Ajax.get(url=s"$host/recipes").map { xhr =>
        val option = decode[List[Recipe]](xhr.responseText)
        option match {
          case Right(recipes) => AppCircuit.dispatch(GetRecipes(recipes))
          case Left(error) => println(error)
        }
      }
    }

    def render(props: Props): VdomElement = {
      <.div(
        <.div(
          ^.cls := "container",
          connection(proxy => Header(Header.Props(proxy, props.ctl, props.resolution)))
        ),
        <.div(
          ^.cls := "container", props.resolution.render(),
          connection(proxy => HomePage(HomePage.Props(proxy, props.ctl)))
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Layout")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}

