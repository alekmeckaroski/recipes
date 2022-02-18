addSbtPlugin("com.vmunier"  % "sbt-web-scalajs"    % "1.2.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs"        % "1.8.0")
addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")
//addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.20.0")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

// Fast development turnaround when using sbt ~reStart
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("com.typesafe.sbt"          % "sbt-twirl"                 % "1.5.1")
addSbtPlugin("org.portable-scala"        % "sbt-scalajs-crossproject"  % "1.1.0")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.7")
addSbtPlugin("com.github.sbt" % "sbt-release"         % "1.1.0")
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.2.2")
//addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.0.0+1-b3702414-SNAPSHOT")
