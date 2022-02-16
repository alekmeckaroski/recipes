package com.lunatech.goldenalgo.onboarding.pages

import com.lunatech.goldenalgo.onboarding.router.AppRouter
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, ScalaComponent}

object LoginPage {

  case class Props(ctl: RouterCtl[AppRouter.Page])

  var loginState = false;

  def executeLogin(props: Props): Callback = {
    loginState = true
    props.ctl.set(AppRouter.Page.Home)
  }

  private val login = ScalaComponent
    .builder[Props]("login")
    .initialState(loginState)
    .render_P { props =>
      <.div(
        <.h1(props => "Login"),
        <.input(^.`type` := "text", ^.placeholder := "Username"),
        <.input(^.`type` := "password", ^.placeholder := "Password"),
        <.button(^.onClick --> executeLogin(props), "Login")
      )
    }
    .build

  def apply(ctl: RouterCtl[AppRouter.Page]): VdomElement = login(Props(ctl))
}
