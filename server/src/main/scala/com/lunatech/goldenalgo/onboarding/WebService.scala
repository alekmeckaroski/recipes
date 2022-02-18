//package com.lunatech.goldenalgo.onboarding
//
//import akka.http.scaladsl.server.Directive
//import com.lunatech.goldenalgo.onboarding.twirl.Implicits._
//
//class WebService() extends Directives {
//
////  val route = {
////    pathSingleSlash {
////      get {
////        complete {
////          com.lunatech.goldenalgo.onboarding.html.index.render("Hello")
////        }
////      }
////    } ~
////      pathPrefix("assets" / Remaining) { file =>
////        // optionally compresses the response with Gzip or Deflate
////        // if the client accepts compressed responses
////        encodeResponse {
////          getFromResource("public/" + file)
////        }
////      }
////  }
//}
