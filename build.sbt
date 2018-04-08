name := "guruz_lms"

inThisBuild(Seq(
  version := "0.6.0-SNAPSHOT",
  scalaVersion := "2.12.4",
  organization := "io.udash",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-language:implicitConversions",
    "-language:existentials",
    "-language:dynamics",
    "-Xfuture",
    "-Xfatal-warnings",
    "-Xlint:_,-missing-interpolator,-adapted-args"
  ),
))

// Custom SBT tasks
val copyAssets = taskKey[Unit]("Copies all assets to the target directory.")
val compileStatics = taskKey[File](
  "Compiles JavaScript files and copies all assets to the target directory."
)
val compileAndOptimizeStatics = taskKey[File](
  "Compiles and optimizes JavaScript files and copies all assets to the target directory."
)
//val copyToServer = taskKey[Unit]("Copies all assets to the server")
val deploy = taskKey[File]("Compiles and Deploys files to Apache")
val apacheServer = settingKey[File]("Where the remote site is")

//val deploymentType = settingKey[TaskKey[File]]("What sort of deployment we want")

lazy val `guruz_lms` = project.in(file("."))
  .aggregate(sharedJS, sharedJVM, frontend, backend)
  .dependsOn(backend)
  .settings(
    publishArtifact := false,
    Compile / mainClass := Some("io.udash.demos.rest.Launcher"),
  )

lazy val shared = crossProject
  .crossType(CrossType.Pure).in(file("shared"))
  .settings(
    libraryDependencies ++= Dependencies.crossDeps.value,
  )

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js

lazy val backend = project.in(file("backend"))
  .dependsOn(sharedJVM)
  .settings(
    libraryDependencies ++= Dependencies.backendDeps.value,
    Compile / mainClass := Some("io.udash.demos.rest.Launcher"),
  )

val frontendWebContent = "UdashStatics/WebContent"
lazy val frontend = project.in(file("frontend")).enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJS)
  .settings(
    libraryDependencies ++= Dependencies.frontendDeps.value,

    // Make this module executable in JS
    Compile / mainClass := Some("lms.JSLauncher"),
    scalaJSUseMainModuleInitializer := true,

    // Implementation of custom tasks defined above
    copyAssets := {
      IO.copyDirectory(
        sourceDirectory.value / "main/assets",
        target.value / frontendWebContent
      )
      IO.copyFile(
        sourceDirectory.value / "main/assets/index.html",
        target.value / frontendWebContent / "index.html"
      )
    },

    // Compiles JS files without full optimizations
    compileStatics := { (Compile / fastOptJS / target).value / "UdashStatics" },
    compileStatics := compileStatics.dependsOn(
      Compile / fastOptJS, Compile / copyAssets
    ).value,

    // Compiles JS files with full optimizations
    compileAndOptimizeStatics := { (Compile / fullOptJS / target).value / "UdashStatics" },
    compileAndOptimizeStatics := compileAndOptimizeStatics.dependsOn(
      Compile / fullOptJS, Compile / copyAssets
    ).value,

    //Deploys to Apache Server

    deploy := {
      IO.copyDirectory(
        target.value / frontendWebContent,
        apacheServer.value
      )
      apacheServer.value
    },
    deploy := deploy.dependsOn(Compile / compileStatics).value,

    //Deployment Settings
    //deploymentType := compileStatics,
    apacheServer := file("C:\\wamp64\\www\\guruz_lms"),

    // Target files for Scala.js plugin
    Compile / fastOptJS / artifactPath :=
      (Compile / fastOptJS / target).value /
        frontendWebContent / "scripts" / "frontend.js",
    Compile / fullOptJS / artifactPath :=
      (Compile / fullOptJS / target).value /
        frontendWebContent / "scripts" / "frontend.js",
    Compile / packageJSDependencies / artifactPath :=
      (Compile / packageJSDependencies / target).value /
        frontendWebContent / "scripts" / "frontend-deps.js",
    Compile / packageMinifiedJSDependencies / artifactPath :=
      (Compile / packageMinifiedJSDependencies / target).value /
        frontendWebContent / "scripts" / "frontend-deps.js"
  )