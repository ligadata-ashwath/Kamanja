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

package com.ligadata.ZooKeeper

import com.ligadata.Serialize._
//import com.ligadata.MetadataAPI._
import com.ligadata.kamanja.metadata._
import org.apache.curator.RetryPolicy
import org.apache.curator.framework._
import org.apache.curator.framework.recipes.cache._
import org.apache.curator.framework.api._
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils._
import java.util.concurrent.locks._
import org.apache.logging.log4j._
import scala.util.control.Breaks._
import org.scalatest.Assertions._

import java.util.Properties
import java.io._
import scala.io._
import java.util.concurrent._
import scala.collection.JavaConverters._
import com.ligadata.KamanjaVersion.KamanjaVersion

class ZooKeeperListener {
  val loggerName = this.getClass.getName
  lazy val logger = LogManager.getLogger(loggerName)
  var zkc: CuratorFramework = null
  var zkc1: CuratorFramework = null
  var nodeCache: NodeCache = _
  var pathChildCache: PathChildrenCache = _

  private def ProcessData(newData: ChildData, ListenCallback: (String) => Unit) = {
    try {
      val data = newData.getData()
      if (data != null) {
        val receivedJsonStr = new String(data)
        logger.debug("New data received => " + receivedJsonStr)
        if (ListenCallback != null)
          ListenCallback(receivedJsonStr)
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
      }
    }
  }

  def CreateListener(zkcConnectString: String, znodePath: String, ListenCallback: (String) => Unit, zkSessionTimeoutMs: Int, zkConnectionTimeoutMs: Int) = {
    try {
      zkc = CreateClient.createSimple(zkcConnectString, zkSessionTimeoutMs, zkConnectionTimeoutMs)
      nodeCache = new NodeCache(zkc, znodePath)
      nodeCache.getListenable.addListener(new NodeCacheListener {
        @Override
        def nodeChanged = {
          try {
            val dataFromZNode = nodeCache.getCurrentData
            ProcessData(dataFromZNode, ListenCallback)
          } catch {
            case ex: Exception => {
              logger.error("Exception while fetching properties from zookeeper ZNode", ex)
            }
          }
        }
      })
      nodeCache.start
      // logger.setLevel(Level.TRACE);
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw new Exception("Failed to start a zookeeper session with(" + zkcConnectString + ")", e)
      }
    }
  }

  // ListenCallback send back the following things
  //  - Current Event Type as String
  //  - Current Event Path
  //  - All Childs Paths & Data.
  def CreatePathChildrenCacheListener(zkcConnectString: String, znodePath: String, getAllChildsData:Boolean, ListenCallback: (String, String,  Array[Byte], Array[(String, Array[Byte])]) => Unit, zkSessionTimeoutMs: Int, zkConnectionTimeoutMs: Int) = {
    try {
      zkc1 = CreateClient.createSimple(zkcConnectString, zkSessionTimeoutMs, zkConnectionTimeoutMs)
      pathChildCache = new PathChildrenCache(zkc1, znodePath, true);

      pathChildCache.getListenable().addListener(new PathChildrenCacheListener {
        @Override
        def childEvent(client: CuratorFramework, event: PathChildrenCacheEvent) = {
          try {
            // val nodePath = ZKPaths.getNodeFromPath(path)
            val childsData = if (getAllChildsData) pathChildCache.getCurrentData.asScala.map(c => { (c.getPath, c.getData) }).toArray else null
            val eventData = event.getData
            ListenCallback(event.getType.toString, eventData.getPath, eventData.getData, childsData)

          } catch {
            case e: Exception => {
              logger.error("Exception while processing event from zookeeper ZNode %s".format(znodePath), e)
            }
          }
        }
      })
      pathChildCache.start();
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw new Exception("Failed to setup a PatchChildrenCacheListener with the node(" + znodePath + ")", e)
      }
    }
  }

  def Shutdown: Unit = {
    nodeCache.close
    pathChildCache.close
    if (zkc != null)
      zkc.close
    if (zkc1 != null)
      zkc1.close
    zkc1=null
    zkc = null
  }
}

object ZooKeeperListenerTest {

  private type OptionMap = Map[Symbol, Any]

  val loggerName = this.getClass.getName
  lazy val logger = LogManager.getLogger(loggerName)
  var firstTime = false

  private def CreatePathChildrenCache(client: CuratorFramework, zNodePath: String) = {
    try {
      val cache = new PathChildrenCache(client, zNodePath, true);

      val childAddedLatch = new CountDownLatch(1);
      val lostLatch = new CountDownLatch(1);
      val reconnectedLatch = new CountDownLatch(1);
      val removedLatch = new CountDownLatch(1);
      cache.getListenable().addListener(new PathChildrenCacheListener {
        @Override
        def childEvent(client: CuratorFramework, event: PathChildrenCacheEvent) = {
          if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
            logger.debug("child_added")
            childAddedLatch.countDown();
          } else if (event.getType() == PathChildrenCacheEvent.Type.CONNECTION_LOST) {
            logger.debug("connection_lost")
            lostLatch.countDown();
          } else if (event.getType() == PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED) {
            logger.debug("connection_reconnected")
            reconnectedLatch.countDown();
          } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
            logger.debug("child_removed")
            removedLatch.countDown();
          }
        }
      })
      cache.start();
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw new Exception("Failed to setup a PatchChildrenCacheListener with the node(" + zNodePath + ")", e)
      }
    }
  }

 /* private def UpdateMetadata(receivedJsonStr: String): Unit = {
    val zkMessage = JsonSerializer.parseZkTransaction(receivedJsonStr, "JSON")
    getMetadataAPI.UpdateMdMgr(zkMessage)
  }*/

  /* def StartLocalListener = {
    val zkListener = new ZooKeeperListener
    try {
      val znodePath = "/ligadata/metadata/metadataupdate"
      val zkcConnectString = "localhost:2181"
    //  JsonSerializer.SetLoggerLevel(Level.TRACE)
      CreateClient.CreateNodeIfNotExists(zkcConnectString, znodePath)
      zkListener.CreateListener(zkcConnectString, znodePath, UpdateMetadata, 250, 30000)
      if( firstTime == true ){
	zkListener.zkc.setData().forPath(znodePath,null)
      }
      CreatePathChildrenCache(zkListener.zkc, znodePath)
      breakable {
        for (ln <- io.Source.stdin.getLines) { // Exit after getting input from console
          zkListener.Shutdown
          println("Exiting")
          break
        }
      }
    } catch {
      case e: Exception => {
        throw new Exception("Failed to start a zookeeper session", e)
      }
    } finally {
      zkListener.Shutdown
    }
  }*/

  private def PrintUsage(): Unit = {
    logger.warn("    --config <configfilename>")
    logger.warn("    --version")
  }

  private def nextOption(map: OptionMap, list: List[String]): OptionMap = {
    def isSwitch(s: String) = (s(0) == '-')
    list match {
      case Nil => map
      case "--config" :: value :: tail =>
        nextOption(map ++ Map('config -> value), tail)
      case "--version" :: tail =>
        nextOption(map ++ Map('version -> "true"), tail)
      case option :: tail => {
        logger.error("Unknown option " + option)
        sys.exit(1)
      }
    }
  }

/*  def main(args: Array[String]) = {
    var databaseOpen = false
    firstTime = true
    var configFile = System.getenv("HOME") + "/MetadataAPIConfig.properties"
    if (args.length == 0) {
      logger.error("Config File defaults to " + configFile)
      logger.error("One Could optionally pass a config file as a command line argument:  --config myConfig.properties")
      logger.error("The config file supplied is a complete path name of a  json file similar to one in github/Kamanja/trunk/MetadataAPI/src/main/resources/MetadataAPIConfig.json")
    } else {
      val options = nextOption(Map(), args.toList)
      val version = options.getOrElse('version, "false").toString
      if (version.equalsIgnoreCase("true")) {
        KamanjaVersion.print
        return
      }
      val cfgfile = options.getOrElse('config, null)
      if (cfgfile == null) {
        logger.error("Need configuration file as parameter")
        throw MissingArgumentException("Usage: configFile  supplied as --config myConfig.properties", null)
      }
      configFile = cfgfile.asInstanceOf[String]
    }
    try {
     // getMetadataAPI.SetLoggerLevel(Level.TRACE)
     // MdMgr.GetMdMgr.SetLoggerLevel(Level.TRACE)
      //JsonSerializer.SetLoggerLevel(Level.TRACE)
      getMetadataAPI.InitMdMgrFromBootStrap(configFile)
      databaseOpen = true
      StartLocalListener
    } catch {
      case e: Exception => {
        throw new Exception("Failed to start a zookeeper session", e)
      }
    } finally {
      if (databaseOpen) {
        getMetadataAPI.CloseDbStore
      }
    }
  } */
}
