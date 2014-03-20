name := "social-networking"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

// MySQL Dependencies
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.29"

// Commons Validator
libraryDependencies += "commons-validator" % "commons-validator" % "1.4.0"

play.Project.playJavaSettings
