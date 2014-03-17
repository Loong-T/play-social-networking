name := "social-networking"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

// MySQL Dependencies
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.29"

play.Project.playJavaSettings
