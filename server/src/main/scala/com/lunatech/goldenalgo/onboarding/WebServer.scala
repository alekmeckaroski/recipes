package com.lunatech.goldenalgo.onboarding

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.lunatech.goldenalgo.onboarding.controllers.RecipeController
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("server-system")
    implicit val ec: ExecutionContext = system.dispatcher

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")

//    val service = new WebService()
    val recipeController = new RecipeController()

    Http().newServerAt(interface, port).bind(recipeController.routes)

    println(s"Server online at http://$interface:$port")
  }
}
