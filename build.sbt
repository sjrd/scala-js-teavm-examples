lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.7",

  persistLauncher in Compile := true,
  persistLauncher in Test := false
)

name := "scalajs-teavm-examples"

lazy val `scalajs-teavm-calculator` = project.in(file("calculator")).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings).
  settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.2"
    )
  )
