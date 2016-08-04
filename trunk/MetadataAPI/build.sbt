import sbtassembly.AssemblyPlugin.defaultShellScript
import sbt._
import Keys._
import sbt.Tests.{Group, SubProcess}

shellPrompt := { state => "sbt (%s)> ".format(Project.extract(state).currentProject.id) }



mainClass in assembly := Some("com.ligadata.MetadataAPI.StartMetadataAPI")

assemblyOption in assembly ~= {
  _.copy(prependShellScript = Some(defaultShellScript))
}

assemblyJarName in assembly := {
  s"${name.value}_${scalaBinaryVersion.value}-${version.value}.jar"
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
  val excludes = Set("commons-beanutils-1.7.0.jar", "google-collections-1.0.jar", "commons-collections4-4.0.jar", "log4j-1.2.17.jar", "log4j-1.2.16.jar")
  cp filter { jar => excludes(jar.data.getName) }
}

test in assembly := {}

name := "MetadataAPI"

//libraryDependencies += "org.joda" % "joda-convert" % "1.6"
//
//libraryDependencies += "joda-time" % "joda-time" % "2.8.2"
//
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0"
//
//libraryDependencies += "org.apache.logging.log4j" % "log4j-1.2-api" % "2.4.1"
//
//libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.4.1"
//
//libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.4.1"
//
//libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.9"
//
//libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.9"
//
//libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6"
//
//libraryDependencies += "org.apache.curator" % "apache-curator" % "2.0.0-incubating"
//
//libraryDependencies += "com.google.guava" % "guava" % "16.0.1"
//
//libraryDependencies += "org.jpmml" % "pmml-evaluator" % "1.2.4"
//
//libraryDependencies += "org.jpmml" % "pmml-model" % "1.2.5"
//
//libraryDependencies += "org.jpmml" % "pmml-schema" % "1.2.5"
//
//dependencyOverrides += "com.google.guava" % "guava" % "16.0.1"
//
//libraryDependencies += "commons-codec" % "commons-codec" % "1.10"
//
//libraryDependencies += "commons-io" % "commons-io" % "2.4"
//
//libraryDependencies ++= Seq(
//"com.twitter" %% "chill" % "0.5.0",
// "org.apache.shiro" % "shiro-core" % "1.2.3",
// "org.apache.shiro" % "shiro-root" % "1.2.3"
//)
//
scalacOptions += "-deprecation"

retrieveManaged := true

parallelExecution := false

coverageMinimum := 80

coverageFailOnMinimum := false

coverageExcludedPackages := "com.ligadata.MetadataAPI.SampleData;com.ligadata.MetadataAPI.TestMetadataAPI"

// The following block forces each test suite into a new JVM
def singleTests(tests: Seq[TestDefinition]): Seq[Group] =
  tests map { test =>
    new Group(name = test.name,
      tests = Seq(test),
      runPolicy = SubProcess(javaOptions = Seq.empty[String]))
  }

testGrouping in Test <<= definedTests in Test map singleTests