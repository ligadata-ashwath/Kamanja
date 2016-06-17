/*
 * Copyright 2015 ligaDATA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ligadata.queryutility

import java.util.Properties

import com.ligadata.MetadataAPI.MetadataAPIImpl
import com.ligadata.Utils.{KamanjaLoaderInfo, Utils}
import com.ligadata.kamanja.metadata.MdMgr._
import com.ligadata.kamanja.metadata.{AdapterInfo, ContainerDef, MessageDef, ModelDef}
import org.apache.logging.log4j._
import java.sql.Connection

import shapeless.option

trait LogTrait {
  val loggerName = this.getClass.getName()
  val logger = LogManager.getLogger(loggerName)
}


object QueryGenerator extends App with LogTrait{

  def usage: String = {
    """
Usage:  bash $KAMANJA_HOME/bin/QueryGenerator.sh --metadataconfig $KAMANJA_HOME/config/Engine1Config.properties --databaseconfig $KAMANJA_HOME/config/file.json
    """
  }

  private type OptionMap = Map[Symbol, Any]

  private def nextOption(map: OptionMap, list: List[String]): OptionMap = {
    def isSwitch(s: String) = (s.charAt(0) == '-')
    list match {
      case Nil => map
      case "--metadataconfig" :: value :: tail =>
        nextOption(map ++ Map('metadataconfig -> value), tail)
      case "--databaseconfig" :: value :: tail =>
        nextOption(map ++ Map('databaseconfig -> value), tail)
      case option :: tail => {
        logger.error("Unknown option " + option)
        logger.warn(usage)
        sys.exit(1)
      }
    }
  }

   override def main(args: Array[String]) {

    logger.debug("QueryGenerator.main begins")

    if (args.length == 0) {
      logger.error("Please pass the Engine1Config.properties file after --metadataconfig option and database config file after --databaseconfig")
      logger.warn(usage)
      sys.exit(1)
    }
    val options = nextOption(Map(), args.toList)

    val metadataConfig = options.getOrElse('metadataconfig, null).toString.trim
    if (metadataConfig == null || metadataConfig.toString().trim() == "") {
      logger.error("Please pass the Engine1Config.properties file after --metadataconfig option")
      logger.warn(usage)
      sys.exit(1)
    }

     val databaseConfig = options.getOrElse('databaseconfig, null).toString.trim
     if (databaseConfig == null || databaseConfig.toString().trim() == "") {
       logger.error("Please pass the database config file file after --databaseconfig option")
       logger.warn(usage)
       sys.exit(1)
     }

     KamanjaConfiguration.configFile = metadataConfig.toString

     val (loadConfigs, failStr) = Utils.loadConfiguration(KamanjaConfiguration.configFile, true)

     if (failStr != null && failStr.size > 0) {
       logger.error(failStr)
       sys.exit(1)
     }
     if (loadConfigs == null) {
       logger.error("Failed to load configurations from configuration file")
       sys.exit(1)
     }

    // KamanjaConfiguration.allConfigs = loadConfigs

     MetadataAPIImpl.InitMdMgrFromBootStrap(KamanjaConfiguration.configFile, false)

     val mdMgr = GetMdMgr

     val msgDefs: Option[scala.collection.immutable.Set[MessageDef]] = mdMgr.Messages(true, true)
     val containerDefs: Option[scala.collection.immutable.Set[ContainerDef]] = mdMgr.Containers(true, true)
     val ModelDefs: Option[scala.collection.immutable.Set[ModelDef]] = mdMgr.Models(true, true)
     val adapterDefs: Map[String, AdapterInfo]= mdMgr.Adapters

     val fileObj: FileUtility = new FileUtility
     if(fileObj.FileExist(databaseConfig) == false){
       logger.error("This file %s does not exists".format(databaseConfig))
       logger.warn(usage)
       sys.exit(1)
     }

     val databasefileContent = fileObj.ReadFile(databaseConfig)

     if (databasefileContent == null  || databasefileContent.size == 0) { // check if config file includes data
       logger.error("This file %s does not include data. Check your file please.".format(databaseConfig))
       logger.warn(usage)
       sys.exit(1)
     }

     val parsedConfig = fileObj.ParseFile(databasefileContent) //Parse config file
     val extractedInfo = fileObj.extractInfo(parsedConfig) //Extract information from parsed file
     val configBeanObj = fileObj.createConfigBeanObj(extractedInfo)// create a config object that store the result from extracting config file

     val queryObj: QueryBuilder = new QueryBuilder

     val conn: Connection = queryObj.getDBConnection(configBeanObj) //this used to fet a connection for orientDB

     if(adapterDefs.isEmpty){
       logger.info("There are no adapter in metadata")
       println("There are no adapter in metadata")
     } else {
       for (adapter <- adapterDefs) {
         var adapterType: String = ""
         val setQuery = queryObj.createSetCommand(adapter = Option(adapter._2))
         if(adapter._2.typeString.equalsIgnoreCase("input")) adapterType = "inputs" else adapterType = "outputs"
         val query: String = queryObj.createQuery(elementType = "vertex", className = adapterType, elementName = adapter._2.Name)
         queryObj.executeQuery(conn, query)
       }
     }

     if(containerDefs.isEmpty){
       logger.info("There are no container in metadata")
       println("There are no container in metadata")
     } else {
       for (container <- containerDefs.get) {
         val setQuery = queryObj.createSetCommand(contianer = Option(container))
         val query: String = queryObj.createQuery(elementType = "vertex", className = "container", elementName = container.Name)
         queryObj.executeQuery(conn, query)
       }
     }

     if(ModelDefs.isEmpty){
       logger.info("There are no model in metadata")
       println("There are no model in metadata")
     } else {
       for (model <- ModelDefs.get) {
         val setQuery = queryObj.createSetCommand(model = Option(model))
         val query: String = queryObj.createQuery(elementType = "vertex", className = "model",elementName=  model.Name)
         queryObj.executeQuery(conn, query)
       }
     }

     if(msgDefs.isEmpty){
       logger.info("There are no messages in metadata")
       println("There are no messages in metadata")
     } else{
       for (message <- msgDefs.get){
         val setQuery = queryObj.createSetCommand(message = Option(message))
         val query: String = queryObj.createQuery(elementType = "vertex", className = "Message", elementName = message.Name)
         queryObj.executeQuery(conn, query)
         // create an edge
       }
     }

     conn.close()
   }
}

object KamanjaConfiguration {
  var configFile: String = _
//  var allConfigs: Properties = _
//  var nodeId: Int = _
//  var clusterId: String = _
//  var nodePort: Int = _
//  // Debugging info configs -- Begin
//  var waitProcessingSteps = collection.immutable.Set[Int]()
//  var waitProcessingTime = 0
//  // Debugging info configs -- End
//
//  var shutdown = false
//  var participentsChangedCntr: Long = 0
//  var baseLoader = new KamanjaLoaderInfo
//
//  def Reset: Unit = {
//    configFile = null
//    allConfigs = null
//    nodeId = 0
//    clusterId = null
//    nodePort = 0
//    // Debugging info configs -- Begin
//    waitProcessingSteps = collection.immutable.Set[Int]()
//    waitProcessingTime = 0
//    // Debugging info configs -- End
//
//    shutdown = false
//    participentsChangedCntr = 0
//  }
}



