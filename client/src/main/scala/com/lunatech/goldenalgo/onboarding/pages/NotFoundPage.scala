package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<

object NotFoundPage {

  case class Props(ctl: RouterCtl[AppRouter.Page])

  private val login = ScalaComponent
    .builder[Props]("notFound")
    .render_P { props =>
      <.div(props => "NOT FOUND")
    }
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = login(Props(ctl))
}
