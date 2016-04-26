name := """twitter-sentiment-play"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws,
  filters,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

  , "org.twitter4j" % "twitter4j-stream" % "4.0.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
doc in Compile <<= target.map(_ / "none")