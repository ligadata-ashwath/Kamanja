package com.ligadata.MetadataAPI.utility.test

/**
 * Created by will on 6/8/15.
 */
object MetadataDefaults {

  val scalaVersionFull = scala.util.Properties.versionNumberString
  val scalaVersion = scalaVersionFull.substring(0, scalaVersionFull.lastIndexOf('.'))
  println("scala version: "+scalaVersionFull)
  def jarResourceDir = getClass.getResource("/jars/lib/system").getPath

  def nodeClassPath: String = ".:" + jarResourceDir + "/metadata_2.11-1.0.jar:" + jarResourceDir + "/basefunctions_2.11-0.1.0.jar:" + jarResourceDir + "/messagedef_2.11-1.0.jar:" + jarResourceDir + "/methodextractor_2.11-1.0.jar:" + jarResourceDir + "/pmmlcompiler_2.11-1.0.jar:" + jarResourceDir + "/kamanjabase_2.11-1.0.jar:" + jarResourceDir + "/bootstrap_2.11-1.0.jar:" + jarResourceDir + "/joda-time-2.9.1.jar:" + jarResourceDir + "/joda-convert-1.6.jar:" + jarResourceDir + "/basetypes_2.11-0.1.0.jar:" + jarResourceDir + "/pmmludfs_2.11-1.0.jar:" + jarResourceDir + "/pmmlruntime_2.11-1.0.jar:" + jarResourceDir + "/json4s-native_2.11-3.2.9.jar:" + jarResourceDir + "/json4s-core_2.11-3.2.9.jar:" + jarResourceDir + "/json4s-ast_2.11-3.2.9.jar:" + jarResourceDir + "/jackson-databind-2.3.1.jar:" + jarResourceDir + "/jackson-annotations-2.3.0.jar:" + jarResourceDir + "/json4s-jackson_2.11-3.2.9.jar:" + jarResourceDir + "/jackson-core-2.3.1.jar:" + jarResourceDir + "/log4j-api-2.4.1.jar:" + jarResourceDir + "/log4j-core-2.4.1.jar"

  //def metadataClasspath: String = s"$jarResourceDir/basefunctions_2.11-0.1.0.jar:$jarResourceDir/basetypes_2.11-0.1.0.jar:$jarResourceDir/bootstrap_2.11-1.0.jar:$jarResourceDir/datadelimiters_2.11-1.0.jar:$jarResourceDir/guava-14.0.1.jar:$jarResourceDir/jackson-annotations-2.3.0.jar:$jarResourceDir/jackson-core-2.3.1.jar:$jarResourceDir/jackson-databind-2.3.1.jar:$jarResourceDir/joda-convert-1.6.jar:$jarResourceDir/joda-time-2.8.2.jar:$jarResourceDir/json4s-ast_2.11-3.2.9.jar:$jarResourceDir/json4s-core_2.11-3.2.9.jar:$jarResourceDir/json4s-jackson_2.11-3.2.9.jar:$jarResourceDir/json4s-native_2.11-3.2.9.jar:$jarResourceDir/jsr305-3.0.0.jar:$jarResourceDir/kamanjabase_2.11-1.0.jar:$jarResourceDir/kvbase_2.11-0.1.0.jar:$jarResourceDir/log4j-api-2.4.1.jar:$jarResourceDir/log4j-core-2.4.1.jar:$jarResourceDir/commons-math3-3.6.jar:$jarResourceDir/customudflib_2.11-1.0.jar:$jarResourceDir/paranamer-2.6.jar:$jarResourceDir/pmml-agent-1.2.9.jar:$jarResourceDir/pmml-evaluator-1.2.9.jar:$jarResourceDir/pmml-model-1.2.9.jar:$jarResourceDir/pmml-schema-1.2.9.jar:$jarResourceDir/pmmlcompiler_2.11-1.0.jar:$jarResourceDir/pmmlruntime_2.11-1.0.jar:$jarResourceDir/pmmludfs_2.11-1.0.jar:$jarResourceDir/scala-compiler-2.11.0.jar:$jarResourceDir/scala-library-2.11.7.jar:$jarResourceDir/scala-reflect-2.11.7.jar:$jarResourceDir/scalap-2.11.0.jar:$jarResourceDir/metadata_2.11-1.0.jar:$jarResourceDir/messagedef_2.11-1.0.jar:$jarResourceDir/exceptions_2.11-1.0.jar:$jarResourceDir/kamanjautils_2.11-1.0.jar:$jarResourceDir/auditadapters_2.11-1.0.jar:$jarResourceDir/simpleapacheshiroadapter_2.11-1.0.jar"

  def metadataClasspath: String = jarResourceDir + s"/metadata_$scalaVersion-1.0.jar:" + jarResourceDir + s"/basefunctions_$scalaVersion-0.1.0.jar:" + jarResourceDir + s"/messagedef_$scalaVersion-1.0.jar:" + jarResourceDir + s"/pmmlcompiler_$scalaVersion-1.0.jar:" + jarResourceDir + s"/kamanjabase_$scalaVersion-1.0.jar:" + jarResourceDir + s"/bootstrap_$scalaVersion-1.0.jar:" + jarResourceDir + "/joda-time-2.9.1.jar:" + jarResourceDir + "/joda-convert-1.6.jar:" + jarResourceDir + s"/basetypes_$scalaVersion-0.1.0.jar:" + jarResourceDir + s"/pmmludfs_$scalaVersion-1.0.jar:" + jarResourceDir + s"/pmmlruntime_$scalaVersion-1.0.jar:" + jarResourceDir + s"/json4s-native_$scalaVersion-3.2.9.jar:" + jarResourceDir + s"/json4s-core_$scalaVersion-3.2.9.jar:" + jarResourceDir + s"/json4s-ast_$scalaVersion-3.2.9.jar:" + jarResourceDir + "/jackson-databind-2.3.1.jar:" + jarResourceDir + "/jackson-annotations-2.3.0.jar:" + jarResourceDir + s"/json4s-jackson_$scalaVersion-3.2.9.jar:" + jarResourceDir + "/jackson-core-2.3.9.jar:" + jarResourceDir + "/log4j-1.2.17.jar:" + jarResourceDir + "/guava-14.0.1.jar:" + jarResourceDir + s"/exceptions_$scalaVersion-1.0.jar:" + jarResourceDir + s"/scala-reflect-$scalaVersionFull.jar:"  + jarResourceDir + s"/scala-library-$scalaVersionFull.jar:" + jarResourceDir + s"/basetypes_$scalaVersion-0.1.0.jar:" + jarResourceDir + s"/metadata_$scalaVersion-1.0.jar:" + jarResourceDir + s"/kvbase_$scalaVersion-0.1.0.jar:" + jarResourceDir + s"/datadelimiters_$scalaVersion-1.0.jar:" + jarResourceDir + "/jsr305-3.0.0.jar:" + jarResourceDir + "/log4j-1.2-api-2.4.1.jar:" + jarResourceDir + "/log4j-api-2.4.1.jar:" + jarResourceDir + "/log4j-core-2.4.1.jar:" + jarResourceDir + s"/heartbeat_$scalaVersion-0.1.0.jar" + jarResourceDir +"/commons-io-2.4.jar"
  //println(metadataClasspath)
  def databaseLocation: String=getClass.getResource("/storage").getPath
}