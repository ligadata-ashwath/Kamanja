name := "ZooKeeperClient"

version := "1.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0"

libraryDependencies += "org.apache.logging.log4j" % "log4j-1.2-api" % "2.4.1"

libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.4.1"

libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.4.1"

// libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.5"

libraryDependencies ++= Seq(
"org.apache.commons" % "commons-collections4" % "4.0",
"commons-configuration" % "commons-configuration" % "1.7",
"commons-logging" % "commons-logging" % "1.1.1",
"org.apache.curator" % "curator-client" % "3.1.0",
"org.apache.curator" % "curator-framework" % "3.1.0",
"org.apache.curator" % "curator-recipes" % "3.1.0",
"com.googlecode.json-simple" % "json-simple" % "1.1"
)

scalacOptions += "-deprecation"
