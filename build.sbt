lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(serverSettings: _*)
lazy val client = (project in file("client")).settings(clientSettings:_*)

val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.1"
)

val serverSettings = commonSettings ++ Seq(
  name := """socket-janken""",
  libraryDependencies ++= Seq(
    jdbc,
    anorm,
    cache,
    ws
  )
)

lazy val clientSettings = commonSettings ++ Seq(
  name := "socket-jason-client",
  libraryDependencies ++= Seq("org.eclipse.jetty.websocket" % "websocket-client" % "9.2.8.v20150217")
)
