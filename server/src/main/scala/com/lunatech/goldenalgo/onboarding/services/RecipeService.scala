package com.lunatech.goldenalgo.onboarding.services

import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import com.lunatech.goldenalgo.onboarding.repositories.RecipeRepositoryImpl
import com.sksamuel.elastic4s.Response
import com.sksamuel.elastic4s.requests.delete.DeleteByQueryResponse
import com.sksamuel.elastic4s.requests.task.CreateTaskResponse

import scala.concurrent.{ExecutionContext, Future}

class RecipeService()(implicit ex: ExecutionContext) {
  private val recipeRepository = new RecipeRepositoryImpl()

  def saveRecipe(recipe: Recipe): Future[Recipe] = recipeRepository.persist(recipe)

  def deleteRecipe(id: String): Future[String] = recipeRepository.delete(id)

  def getAllRecipes: Future[Seq[RecipeResponse]] = recipeRepository.findAll()

  def getRecipeById(id: String): Future[Seq[RecipeResponse]] = recipeRepository.findById(id)

  def getRecipeByName(name: String): Future[Seq[RecipeResponse]] = recipeRepository.findByName(name)

  def searchRecipes(query: String): Future[Seq[RecipeResponse]] = recipeRepository.searchRecipe(query)
}
