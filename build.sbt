name := """twitter-sentiment-play"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .enablePlugins(JDebPackaging)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

  , "org.twitter4j" % "twitter4j-stream" % "4.0.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
doc in Compile <<= target.map(_ / "none")

maintainer in Linux := "Raghav Narula <raghav.narula@outlook.com>"

packageSummary in Linux := "Twitter follow sentiment API"

packageDescription := "..."

//import AssemblyKeys._
//
//assemblySettings
//
mainClass in assembly := Some("play.core.server.ProdServerStart")
//
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}