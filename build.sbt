name := "social-networking"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

libraryDependencies ++= Seq(
  // MySQL Dependencies
  "mysql" % "mysql-connector-java" % "5.1.29",
  // Commons Validator
  "commons-validator" % "commons-validator" % "1.4.0",
  // Emailer plugin
  "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2"
)

play.Project.playJavaSettings
