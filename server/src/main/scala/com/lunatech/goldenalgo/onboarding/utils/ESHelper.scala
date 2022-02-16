package com.lunatech.goldenalgo.onboarding.utils

import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}

trait ESHelper {

  val props: ElasticProperties = ElasticProperties("http://localhost:9200")
  val client: ElasticClient = ElasticClient(JavaClient(props))

  def prefixIndex(name: String): String

  val recipeIndex: String = prefixIndex("recipes")
}
