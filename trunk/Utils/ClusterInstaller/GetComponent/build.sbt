import sbtassembly.AssemblyPlugin.defaultShellScript
import sbt._
import Keys._

shellPrompt := { state => "sbt (%s)> ".format(Project.extract(state).currentProject.id) }
val kamanjaVersion = "1.4.0"

assemblyOption in assembly ~= {
  _.copy(prependShellScript = Some(defaultShellScript))
}

assemblyJarName in assembly := {
  s"${name.value}-${version.value}"
}
assemblyJarName in assembly := {
  s"${name.value}_${scalaBinaryVersion.value}-${kamanjaVersion}.jar"
}

// for some reason the merge strategy for non ligadata classes are not working and thus added those conflicting jars in exclusions
// this may result some run time errors

assemblyMergeStrategy in assembly := {
  // case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  // case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList("META-INF", "maven", "jline", "jline", ps) if ps.startsWith("pom") => MergeStrategy.discard
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case x if x endsWith "google/common/annotations/GwtCompatible.class" => MergeStrategy.first
  case x if x endsWith "google/common/annotations/GwtIncompatible.class" => MergeStrategy.first
  case x if x endsWith "/apache/commons/beanutils/BasicDynaBean.class" => MergeStrategy.first
  case x if x endsWith "com\\ligadata\\kamanja\\metadataload\\MetadataLoad.class" => MergeStrategy.first
  case x if x endsWith "com/ligadata/kamanja/metadataload/MetadataLoad.class" => MergeStrategy.first
  case x if x endsWith "org/apache/commons/beanutils/BasicDynaBean.class" => MergeStrategy.last
  case x if x endsWith "com\\esotericsoftware\\minlog\\Log.class" => MergeStrategy.first
  case x if x endsWith "com\\esotericsoftware\\minlog\\Log$Logger.class" => MergeStrategy.first
  case x if x endsWith "com/esotericsoftware/minlog/Log.class" => MergeStrategy.first
  case x if x endsWith "com/esotericsoftware/minlog/Log$Logger.class" => MergeStrategy.first
  case x if x endsWith "com\\esotericsoftware\\minlog\\pom.properties" => MergeStrategy.first
  case x if x endsWith "com/esotericsoftware/minlog/pom.properties" => MergeStrategy.first
  case x if x contains "com.esotericsoftware.minlog\\minlog\\pom.properties" => MergeStrategy.first
  case x if x contains "com.esotericsoftware.minlog/minlog/pom.properties" => MergeStrategy.first
  case x if x contains "org\\objectweb\\asm\\" => MergeStrategy.last
  case x if x contains "org/objectweb/asm/" => MergeStrategy.last
  case x if x contains "org/apache/commons/collections" => MergeStrategy.last
  case x if x contains "org\\apache\\commons\\collections" => MergeStrategy.last
  case x if x contains "com.fasterxml.jackson.core" => MergeStrategy.first
  case x if x contains "com/fasterxml/jackson/core" => MergeStrategy.first
  case x if x contains "com\\fasterxml\\jackson\\core" => MergeStrategy.first
  case x if x contains "commons-logging" => MergeStrategy.first
  case "log4j.properties" => MergeStrategy.first
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)

}

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val excludes = Set("commons-beanutils-1.7.0.jar", "google-collections-1.0.jar", "commons-collections4-4.0.jar")
  cp filter { jar => excludes(jar.data.getName) }
}

test in assembly := {}

name := "GetComponent"

version := "1.4.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.0.2"
libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.0.2"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.1"
libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6"
libraryDependencies += "org.apache.kafka" %% "kafka" % "0.8.2.2"
libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"
// libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.7"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3"
libraryDependencies += "log4j" % "log4j" % "1.2.17"
//libraryDependencies += "org.eclipse.debug" % "ui" % "3.3.0-v20070607-1800"

scalacOptions += "-deprecation"

coverageMinimum := 80

coverageFailOnMinimum := false

coverageExcludedPackages := "com.ligadata.kamanja.CheckerComponent.*"
