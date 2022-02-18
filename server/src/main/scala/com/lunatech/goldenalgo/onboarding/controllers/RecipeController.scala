package com.lunatech.goldenalgo.onboarding.controllers

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import com.lunatech.goldenalgo.onboarding.models.Recipe
import com.lunatech.goldenalgo.onboarding.services.RecipeService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import com.lunatech.goldenalgo.onboarding.twirl.Implicits._

import scala.concurrent.ExecutionContext
import org.slf4j.{Logger, LoggerFactory}

class RecipeController()(implicit val ec: ExecutionContext) extends Directives {

  private lazy val log: Logger = LoggerFactory.getLogger(getClass)
  private val recipeService: RecipeService = new RecipeService()

  val entryRoute: Route = path("") {
    get {
      complete {
        com.lunatech.goldenalgo.onboarding.html.index.render()
      }
    }
  } ~ pathPrefix("assets" / Remaining) { file => {
    encodeResponse {
      getFromResource("public/" + file)
    }}
  }

  val topLevel: Route = path("recipes") {
    concat(
      get {
        onComplete(recipeService.getAllRecipes) {
          case scala.util.Success(recipes) =>
            complete(HttpEntity(ContentTypes.`application/json`, recipes.asJson.noSpaces))
          case scala.util.Failure(ex) =>
            complete(ex.getMessage)
        }
      },
      post {
        decodeRequest {
          entity(as[Recipe]) { recipe =>
            onComplete(recipeService.saveRecipe(recipe)) {
              case scala.util.Success(recipe) =>
                complete(HttpEntity(ContentTypes.`application/json`, recipe.asJson.noSpaces))
              case scala.util.Failure(ex) =>
                complete(ex.getMessage)
            }
          }
        }
      })
  }

  val recipe: Route = path("recipes" / Segment) { id =>
    concat(
      get {
        onComplete(recipeService.getRecipeById(id)) {
          case scala.util.Success(recipe) =>
            complete(HttpEntity(ContentTypes.`application/json`, recipe.asJson.noSpaces))
          case scala.util.Failure(ex) =>
            complete(ex.getMessage)
        }
      },
      delete {
        onComplete(recipeService.deleteRecipe(id)) {
          case scala.util.Success(recipe) =>
            complete(HttpEntity(ContentTypes.`application/json`, recipe.asJson.noSpaces))
          case scala.util.Failure(ex) =>
            complete(ex.getMessage)
        }
      }
    )
  }

  val search: Route = path("search" / Segment) { query =>
    get {
      onComplete(recipeService.searchRecipes(query)) {
        case scala.util.Success(recipes) =>
          complete(HttpEntity(ContentTypes.`application/json`, recipes.asJson.noSpaces))
        case scala.util.Failure(ex) =>
          complete(ex.getMessage)
      }
    }
  }

  val routes: Route = entryRoute ~ topLevel ~ recipe ~ search
}
