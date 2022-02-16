ThisBuild / organization := "com.lunatech.goldenalgo.onboarding"
ThisBuild / scalaVersion := "2.13.7"
ThisBuild / version      := "0.1.0-SNAPSHOT"
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.FastOptStage
import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import sbtcrossproject.CrossPlugin.autoImport.crossProject
import sbtcrossproject.CrossType

lazy val root = (project in file("."))
  .aggregate(server, client)

lazy val server = (project in file("server"))
  .settings(
    scalaJSProjects := Seq(client),
    Assets / pipelineStages := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest),
    // triggers scalaJSPipeline when using compile or continuous compilation
    Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.2.7",
      "com.typesafe.akka" %% "akka-stream" % "2.6.18",
      "de.heikoseeberger" %% "akka-http-circe"  % "1.39.2",
      "com.vmunier" %% "scalajs-scripts" % "1.2.0",

      "io.circe"          %% "circe-core"       % "0.14.1",
      "io.circe"          %% "circe-generic"    % "0.14.1",
      "io.circe"          %% "circe-parser"     % "0.14.1",

      "ch.megard" %% "akka-http-cors" % "1.1.3",
      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % "7.16.3",
      "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "7.16.3" % "test",
      "com.sksamuel.elastic4s" %% "elastic4s-circe" % "6.7.8",
      "org.mdedetrich"         %% "sbt-digest-reverse-router" % "0.2.0"
    ),
    libraryDependencies += "net.sf.py4j" % "py4j" % "0.10.9.3",
    Assets / WebKeys.packagePrefix := "public/",
    Runtime / managedClasspath += (Assets / packageBin).value,
  )
  .enablePlugins(SbtWeb, SbtTwirl, JavaAppPackaging)
//  .dependsOn(shared.jvm)

def toPathMapping(f: File): (File, String) = f -> f.getName

lazy val library = new Object {

  val version = new Object {
    val react                      = "17.0.1"
    val reactMarkdown          = "4.3.1"
  }

  // js dependencies
  private val npm = "org.webjars.npm"
  private def distOnly(name: String, version: String): ModuleID =
    npm % name % version exclude (npm, "*")
  val react =
    distOnly("react", version.react) /
      "umd/react.development.js" minified "umd/react.production.min.js"
  val reactDom =
    distOnly("react-dom", version.react) /
      "umd/react-dom.development.js" minified "umd/react-dom.production.min.js" dependsOn react.jsDep.resourceName
  val reactDomServer =
    distOnly("react-dom", version.react) /
      "umd/react-dom-server.browser.development.js" minified "umd/react-dom-server.browser.production.min.js" dependsOn reactDom.jsDep.resourceName

  val reactMarkdown =
    distOnly("react-markdown", version.reactMarkdown) /
      "umd/react-markdown.js" dependsOn react.jsDep.resourceName
}

lazy val client = (project in file("client"))
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSWeb,
    //    ScalaJSBundlerPlugin,
    JSDependenciesPlugin)
  .settings(
//    wartremoverExcluded += baseDirectory.value / "src",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0",
      "com.github.japgolly.scalajs-react" %%% "core"          % "1.7.7",
      "com.github.japgolly.scalajs-react" %%% "extra"         % "1.7.7",
      "io.suzaku"                         %%% "diode-core"    % "1.1.13",
      "io.suzaku"                         %%% "diode-react"   % "1.1.13",
      "io.circe"                          %%% "circe-core"    % "0.13.0",
      "io.circe"                          %%% "circe-generic" % "0.13.0",
      "io.circe"                          %%% "circe-parser"  % "0.13.0",
    ),
    packageJSDependencies / skip := false,
    Compile / fullLinkJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
    Compile / fastLinkJS / jsMappings += toPathMapping((Compile / packageJSDependencies).value),
    Compile / fullLinkJS / jsMappings += toPathMapping((Compile / packageMinifiedJSDependencies).value),
    jsDependencies ++= Seq(
      library.react,
      library.reactDom,
      library.reactDomServer,
      library.reactMarkdown)
  )
//  .dependsOn(shared.js)

val useFastOptJs = sys.env.exists(envVar => envVar._1 == "FAST_OPT_JS" && envVar._2 == "true")
Global / scalaJSStage := (if (useFastOptJs) FastOptStage else FullOptStage)

//lazy val shared = crossProject(JSPlatform, JVMPlatform)
//  .crossType(CrossType.Pure)
//  .in(file("shared"))
//  .jsConfigure(_.enablePlugins(ScalaJSPlugin,
//    ScalaJSWeb,
//    JSDependenciesPlugin))
//  .jsSettings(packageJSDependencies / skip := false)
