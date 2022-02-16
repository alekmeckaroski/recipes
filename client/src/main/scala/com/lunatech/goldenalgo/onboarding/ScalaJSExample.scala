package com.lunatech.goldenalgo.onboarding


import io.circe.parser.decode
import japgolly.scalajs.react.extra.router.BaseUrl
import org.scalajs.dom
import org.scalajs.dom.raw.Element

import com.lunatech.goldenalgo.onboarding.router.AppRouter

import scala.scalajs.js.annotation.JSExport

object ScalaJSExample {

  @JSExport
  def main(args: Array[String]): Unit = {
    val app = dom.document.getElementById("scalajsShoutOut")
    AppRouter.router().renderIntoDOM(app)
  }
}
