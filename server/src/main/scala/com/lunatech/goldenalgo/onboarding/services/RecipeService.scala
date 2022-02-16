package com.lunatech.goldenalgo.onboarding.services

import com.lunatech.goldenalgo.onboarding.models.{Recipe, RecipeResponse}
import com.lunatech.goldenalgo.onboarding.repositories.RecipeRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

class RecipeService()(implicit ex: ExecutionContext) {
  private val recipeRepository = new RecipeRepositoryImpl()

  def getAllRecipes: Future[Seq[Recipe]] = recipeRepository.findAll()

  def getRecipeById(id: String): Future[Seq[RecipeResponse]] = recipeRepository.findById(id)

  def getRecipeByName(name: String): Future[Seq[Recipe]] = recipeRepository.findByName(name)

  def deleteRecipe(id: String): Future[String] = recipeRepository.delete(id)

  def saveRecipe(recipe: Recipe): Future[Recipe] = recipeRepository.persist(recipe)

  def searchRecipes(query: String): Future[Seq[Recipe]] = recipeRepository.searchRecipe(query)
}
