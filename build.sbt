lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(rootSettings:_*)

lazy val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.1",
  libraryDependencies := Seq(
    jdbc,
    anorm,
    cache,
    ws
  )
)

lazy val rootSettings = commonSettings ++ Seq(
  name := """socket-janken"""
)

//lazy val client = (project in file("./client")).settings(clientSettings:_*)
//lazy val clientSettings = commonSettings ++ Seq(
//  libraryDependencies := Seq("org.eclipse.jetty.websocket" % "websocket-client" % "9.2.6.v20141205")
//)
