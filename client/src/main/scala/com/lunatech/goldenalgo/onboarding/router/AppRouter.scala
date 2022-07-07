package com.lunatech.goldenalgo.onboarding.router

import com.lunatech.goldenalgo.onboarding.diode.{AppCircuit, AppState}
import com.lunatech.goldenalgo.onboarding.models.Recipe
import com.lunatech.goldenalgo.onboarding.pages.{HomePage, RecipePage}
import diode.react.ReactConnectProxy
import japgolly.scalajs.react.extra.router.StaticDsl.Route
import japgolly.scalajs.react.extra.router.{BaseUrl, Resolution, Router, RouterConfigDsl, RouterCtl, RouterWithPropsConfig, SetRouteVia}

object AppRouter {

  sealed trait Page

  object Page {
    case object Login extends Page
    case object Home extends Page
    case class Recipe(id: String) extends Page
    case object NotFound extends Page
  }

  val connection: ReactConnectProxy[AppState] = AppCircuit.connect(_.state)

  val routerConfig: RouterWithPropsConfig[Page, Unit] = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | staticRoute(root, Page.Home) ~> renderR(renderHomePage)
      | dynamicRouteCT("recipes" / string(".*").caseClass[Page.Recipe]) ~> dynRenderR(renderRecipePage)
      )
      .notFound(redirectToPage(Page.Home)(SetRouteVia.HistoryReplace))
//       .renderWith(layout)
  }

  def renderHomePage(ctl: RouterCtl[Page]) = {
    connection(proxy => HomePage.Component(HomePage.Props(proxy, ctl)))
  }

  def renderRecipePage(page: Page.Recipe, ctl: RouterCtl[Page]) = {
     connection(proxy => RecipePage.Component(RecipePage.Props(proxy, page, ctl)))
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
