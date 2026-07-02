scalaVersion := "3.8.4"

lazy val root = rootProject
  .settings(
    name := "fable-hello-world",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.2.3" % Test
    ),
    // Run in the terminal that launched sbt, so the animation gets a real console.
    run / fork := true,
    run / connectInput := true,
    run / outputStrategy := Some(StdoutOutput)
  )
