package com.lunatech.goldenalgo.onboarding.components

import com.lunatech.goldenalgo.onboarding.diode.AppState
import com.lunatech.goldenalgo.onboarding.router.AppRouter.Page
import diode.react.ModelProxy
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._

object Header {
  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[Page],
                    resolution: Resolution[Page]
                  )

  val Component = ScalaFnComponent[Props](props => {
    <.div(
      ^.display := "flex",
      ^.justifyContent := "space-between",
      <.h1("Recipes App")
    )
  })

  def apply(props: Props) = Component(props)
}
