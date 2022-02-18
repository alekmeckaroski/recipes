package com.lunatech.goldenalgo.onboarding.repositories

import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import com.sksamuel.elastic4s.ElasticApi.indexInto
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, RequestFailure, RequestSuccess, Response}
import io.circe.syntax._
import io.circe.generic.auto._
import com.sksamuel.elastic4s.http.JavaClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait RecipeRepository {

  def persist(recipe: Recipe): Future[Recipe]

  def delete(id: String): Future[String]

  def findAll(): Future[Seq[RecipeResponse]]

  def findById(id: String): Future[Seq[RecipeResponse]]

  def findByName(name: String): Future[Seq[RecipeResponse]]

  def searchRecipe(query: String): Future[Seq[RecipeResponse]]
}

class RecipeRepositoryImpl() extends RecipeRepository {

  val props: ElasticProperties = ElasticProperties("http://localhost:9200")
  val client: ElasticClient = ElasticClient(JavaClient(props))
  val index: String = "recipes"

  client.execute {
    createIndex(index)
  }.await

  override def persist(recipe: Recipe): Future[Recipe] =
    client.execute {
      indexInto(index).doc(recipe.asJson.noSpaces)
    }.flatMap {
      case RequestSuccess(_, _, _, _) => Future.successful(recipe)
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }

  override def delete(id: String): Future[String] =
    client.execute {
        deleteById(index, id)
      }.flatMap {
      case RequestSuccess(_, _, _, result) => Future.successful(result.result)
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }

  override def findAll(): Future[Seq[RecipeResponse]] = {
    client.execute {
      search(index)
    }.flatMap {
      case RequestSuccess(_, _, _, result) => Future.successful(result.to[RecipeResponse])
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }
  }

  override def findByName(name: String): Future[Seq[RecipeResponse]] =
    client.execute {
      search(index).matchQuery("name", name)
    }.flatMap {
      case RequestSuccess(_, _, _, result) => Future.successful(result.to[RecipeResponse])
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }

  override def findById(id: String): Future[Seq[RecipeResponse]] =
    client.execute {
      search(index).matchQuery("_id", id)
    }.flatMap {
      case RequestSuccess(_, _, _, result) => Future.successful(result.to[RecipeResponse])
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }

  override def searchRecipe(query: String): Future[Seq[RecipeResponse]] =
    client.execute {
      search(index).query(query)
    }.flatMap {
      case RequestSuccess(_, _, _, result) => Future.successful(result.to[RecipeResponse])
      case RequestFailure(_, _, _, error) => Future.failed(error.asException)
    }
}
