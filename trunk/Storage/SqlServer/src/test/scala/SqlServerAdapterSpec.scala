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

package com.ligadata.automation.unittests.sqlserveradapter

import org.scalatest._
import Matchers._

import com.ligadata.Utils._
import util.control.Breaks._
import scala.io._
import java.util.{Date,Calendar,TimeZone}
import java.text.{SimpleDateFormat}
import java.io._

import sys.process._
import org.apache.logging.log4j._

import com.ligadata.keyvaluestore._
import com.ligadata.KvBase._
import com.ligadata.StorageBase._
import com.ligadata.Serialize._
import com.ligadata.Utils.Utils._
import com.ligadata.Utils.{ KamanjaClassLoader, KamanjaLoaderInfo }
import com.ligadata.StorageBase.StorageAdapterFactory
import com.ligadata.keyvaluestore.SqlServerAdapter

import com.ligadata.Exceptions._

case class Customer(name:String, address: String, homePhone: String)

class SqlServerAdapterSpec extends FunSpec with BeforeAndAfter with BeforeAndAfterAll with GivenWhenThen {
  var res : String = null;
  var statusCode: Int = -1;
  var adapter:DataStore = null
  var serializer:Serializer = null

  private val loggerName = this.getClass.getName
  private val logger = LogManager.getLogger(loggerName)

  val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  val dateFormat1 = new SimpleDateFormat("yyyy/MM/dd")
  // set the timezone to UTC for all time values
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  var containerName = ""
  private val kvManagerLoader = new KamanjaLoaderInfo
  private val maxConnectionAttempts = 10

  serializer = SerializerManager.GetSerializer("kryo")
  logger.info("Initialize SqlServerAdapter")
  val dataStoreInfo = """{"StoreType": "sqlserver","hostname": "192.168.56.1","instancename":"KAMANJA","portnumber":"1433","database": "bofa","user":"bofauser","SchemaName":"bofauser","password":"bofauser","jarpaths":"/media/home2/jdbc","jdbcJar":"sqljdbc4-2.0.jar","clusteredIndex":"YES","autoCreateTables":"YES"}"""

  private def CreateAdapter: DataStore = {
    var connectionAttempts = 0
    while (connectionAttempts < maxConnectionAttempts) {
      try {
        adapter = SqlServerAdapter.CreateStorageAdapter(kvManagerLoader, dataStoreInfo, null, null)
        return adapter
      } catch {
        case e: Exception => {
          logger.error("will retry after one minute ...", e)
          Thread.sleep(60 * 1000L)
          connectionAttempts = connectionAttempts + 1
        }
      }
    }
    return null;
  }

  override def beforeAll = {
    logger.info("starting...");
    CreateAdapter
  }

  private def RoundDateToSecs(d:Date): Date = {
    var c = Calendar.getInstance()
    if( d == null ){
      c.setTime(new Date(0))
      c.getTime
    }
    else{
      c.setTime(d)
      c.set(Calendar.MILLISECOND,0)
      c.getTime
    }
  }

  def readCallBack(key:Key, value: Value) {
    logger.info("timePartition => " + key.timePartition)
    logger.info("bucketKey => " + key.bucketKey.mkString(","))
    logger.info("transactionId => " + key.transactionId)
    logger.info("rowId => " + key.rowId)
    logger.info("serializerType => " + value.serializerType)
    logger.info("serializedInfo length => " + value.serializedInfo.length)
    logger.info("----------------------------------------------------")
  }

  def readKeyCallBack(key:Key) {
    logger.info("timePartition => " + key.timePartition)
    logger.info("bucketKey => " + key.bucketKey.mkString(","))
    logger.info("transactionId => " + key.transactionId)
    logger.info("rowId => " + key.rowId)
    logger.info("----------------------------------------------------")
  }

  def deleteFile(path:File):Unit = {
    if(path.exists()){
      if (path.isDirectory){
	for(f <- path.listFiles) {
          deleteFile(f)
	}
      }
      path.delete()
    }
  }

  describe("Unit Tests for all sqlserveradapter operations") {

    // validate property setup
    it ("Validate api operations") {
      containerName = "sys.customer1"

      And("Test drop container")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.DropContainer(containers)
      }

      And("Test auto create of a table")
      noException should be thrownBy {
	adapter.get(containerName,readCallBack _)
      }

      And("Test create container")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.CreateContainer(containers)
      }

      And("Test create container with invalid name")
      containerName = "&&"
      var ex = the [com.ligadata.Exceptions.StorageDDLException] thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.CreateContainer(containers)
      }
      logger.info("", ex)

      And("Resume API Testing")
      containerName = "sys.customer1"
      
      And("Test Put api throwing DML Exception")
      var ex1 = the [com.ligadata.Exceptions.StorageDMLException] thrownBy {
	var keys = new Array[Key](0) // to be used by a delete operation later on
	var currentTime = new Date()
	var keyArray = new Array[String](0)
	// pick a bucketKey values longer than 1024 characters
	var custName = "customerxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	keyArray = keyArray :+ custName
	var key = new Key(currentTime.getTime(),keyArray,1,1)
	var custAddress = "1000"  + ",Main St, Redmond WA 98052"
	var custNumber = "4256667777"
	var obj = new Customer(custName,custAddress,custNumber)
	var v = serializer.SerializeObjectToByteArray(obj)
	var value = new Value(1,"kryo",v)
	adapter.put(containerName,key,value)
      }
      logger.info("", ex1)

      val sqlServerAdapter = adapter.asInstanceOf[SqlServerAdapter]

      And("Make sure rollback happened by doing a row count")
      var cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 0)

      And("Test Put api throwing DDL Exception - use invalid container name")
      var ex2 = the [com.ligadata.Exceptions.StorageDMLException] thrownBy {
	var keys = new Array[Key](0) // to be used by a delete operation later on
	var currentTime = new Date()
	var keyArray = new Array[String](0)
	// pick a bucketKey values longer than 1024 characters
	var custName = "customer1"
	keyArray = keyArray :+ custName
	var key = new Key(currentTime.getTime(),keyArray,1,1)
	var custAddress = "1000"  + ",Main St, Redmond WA 98052"
	var custNumber = "4256667777"
	var obj = new Customer(custName,custAddress,custNumber)
	var v = serializer.SerializeObjectToByteArray(obj)
	var value = new Value(1,"kryo",v)
	adapter.put("&&",key,value)
      }
      logger.info("", ex2)

      And("Test Put api")
      var keys = new Array[Key](0) // to be used by a delete operation later on
      for( i <- 1 to 10 ){
	var currentTime = new Date()
	var keyArray = new Array[String](0)
	var custName = "customer-" + i
	keyArray = keyArray :+ custName
	var key = new Key(currentTime.getTime(),keyArray,i,i)
	keys = keys :+ key
	var custAddress = "1000" + i + ",Main St, Redmond WA 98052"
	var custNumber = "4256667777" + i
	var obj = new Customer(custName,custAddress,custNumber)
	var v = serializer.SerializeObjectToByteArray(obj)
	var value = new Value(1,"kryo",v)
	noException should be thrownBy {
	  adapter.put(containerName,key,value)
	}
      }

      And("Get all the rows that were just added")
      noException should be thrownBy {
	adapter.get(containerName,readCallBack _)
      }


      And("Check the row count after adding a bunch")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 10)

      And("Get all the keys for the rows that were just added")
      noException should be thrownBy {
	adapter.getKeys(containerName,readKeyCallBack _)
      }

      And("Test Del api")
      noException should be thrownBy {
	adapter.del(containerName,keys)
      }

      And("Check the row count after deleting a bunch")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 0)

      for( i <- 1 to 100 ){
	var currentTime = new Date()
	var keyArray = new Array[String](0)
	var custName = "customer-" + i
	keyArray = keyArray :+ custName
	var key = new Key(currentTime.getTime(),keyArray,i,i)
	var custAddress = "1000" + i + ",Main St, Redmond WA 98052"
	var custNumber = "4256667777" + i
	var obj = new Customer(custName,custAddress,custNumber)
	var v = serializer.SerializeObjectToByteArray(obj)
	var value = new Value(1,"kryo",v)
	noException should be thrownBy {
	  adapter.put(containerName,key,value)
	}
      }

      And("Check the row count after adding a hundred rows")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 100)

      And("Test truncate container")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.TruncateContainer(containers)
      }

      And("Check the row count after truncating the container")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 0)

      And("Test Bulk Put api")

      var keyValueList = new Array[(Key, Value)](0)
      var keyStringList = new Array[Array[String]](0)
      for( i <- 1 to 10 ){
	var  cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);    
	var currentTime = cal.getTime()
	var keyArray = new Array[String](0)
	var custName = "customer-" + i
	keyArray = keyArray :+ custName
	// keyStringList is only used to test a del operation later
	keyStringList = keyStringList :+ keyArray
	var key = new Key(currentTime.getTime(),keyArray,i,i)
	var custAddress = "1000" + i + ",Main St, Redmond WA 98052"
	var custNumber = "4256667777" + i
	var obj = new Customer(custName,custAddress,custNumber)
	var v = serializer.SerializeObjectToByteArray(obj)
	var value = new Value(1,"kryo",v)
	keyValueList = keyValueList :+ (key,value)
      }
      var dataList = new Array[(String, Array[(Key,Value)])](0)
      dataList = dataList :+ (containerName,keyValueList)
      noException should be thrownBy {
	adapter.put(dataList)
      }

      And("Get all the rows that were just added")
      noException should be thrownBy {
	adapter.get(containerName,readCallBack _)
      }

      And("Check the row count after adding a bunch")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 10)

      And("Get all the keys for the rows that were just added")
      noException should be thrownBy {
	adapter.getKeys(containerName,readKeyCallBack _)
      }

      And("Test Delete for a time range")
      var  cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -10);    
      var beginTime = cal.getTime()
      logger.info("begin time => " + dateFormat.format(beginTime))
      cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -8);    
      var endTime = cal.getTime()
      logger.info("end time => " + dateFormat.format(endTime))

      var timeRange = new TimeRange(beginTime.getTime(),endTime.getTime())
      noException should be thrownBy {
	adapter.del(containerName,timeRange,keyStringList)
      }

      And("Check the row count after deleting a bunch based on time range")
      cnt = sqlServerAdapter.getRowCount(containerName,null)
      assert(cnt == 8)

      And("Test Get for a time range")
      cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -7);    
      beginTime = cal.getTime()
      logger.info("begin time => " + dateFormat.format(beginTime))
      cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -6);    
      endTime = cal.getTime()
      logger.info("end time => " + dateFormat.format(endTime))

      timeRange = new TimeRange(beginTime.getTime(),endTime.getTime())
      var timeRanges = new Array[TimeRange](0)
      timeRanges = timeRanges :+ timeRange

      noException should be thrownBy {
	adapter.get(containerName,timeRanges,readCallBack _)
      }

      And("Test GetKeys for a time range")
      noException should be thrownBy {
	adapter.getKeys(containerName,timeRanges,readKeyCallBack _)
      }

      And("Test Get for a given keyString Arrays")
      keyStringList = new Array[Array[String]](0)
      for( i <- 1 to 5 ){
	var keyArray = new Array[String](0)
	var custName = "customer-" + i
	keyArray = keyArray :+ custName
	keyStringList = keyStringList :+ keyArray
      }
      noException should be thrownBy {
	adapter.get(containerName,keyStringList,readCallBack _)
      }
      
      And("Test GetKeys for a given keyString Arrays")
      noException should be thrownBy {
	adapter.getKeys(containerName,keyStringList,readKeyCallBack _)
      }


      And("Test Get for a given set of keyStrings and also an array of time ranges")
      keyStringList = new Array[Array[String]](0)
      for( i <- 1 to 5 ){
	var keyArray = new Array[String](0)
	var custName = "customer-" + i
	keyArray = keyArray :+ custName
	keyStringList = keyStringList :+ keyArray
      }
      cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -3);    
      beginTime = cal.getTime()
      logger.info("begin time => " + dateFormat.format(beginTime))
      cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -2);    
      endTime = cal.getTime()
      logger.info("end time => " + dateFormat.format(endTime))

      timeRange = new TimeRange(beginTime.getTime(),endTime.getTime())
      timeRanges = new Array[TimeRange](0)
      timeRanges = timeRanges :+ timeRange

      noException should be thrownBy {
	adapter.get(containerName,timeRanges,keyStringList,readCallBack _)
      }

      And("Test GetKeys for a given set of keyStrings and also an array of time ranges")
      noException should be thrownBy {
	adapter.getKeys(containerName,timeRanges,keyStringList,readKeyCallBack _)
      }

      And("Test the existence of the source container")
      var exists = adapter.isContainerExists(containerName)
      assert(exists == true)

      var srcContainerName = containerName
      var destContainerName = containerName

      And("Test copy container with src and dest are same ")
      var ex3 = the [com.ligadata.Exceptions.StorageDDLException] thrownBy {
	adapter.copyContainer(srcContainerName,destContainerName,false)
      }
      logger.info("Exception => " + ex3.cause)

      srcContainerName = containerName
      destContainerName = containerName + "_bak"

      And("Test copy container")
      noException should be thrownBy {
	adapter.copyContainer(srcContainerName,destContainerName,true)
      }

      And("Test the existence of the destination container")
      exists = adapter.isContainerExists(destContainerName)
      assert(exists == true)

      // in Sqlserver we use sp_rename to backup the table
      // when a table is renamed, we nolonger have any reference to old table
      And("Test the non-existence of the source container")
      exists = adapter.isContainerExists(srcContainerName)
      assert(exists == false)

      And("Test create container after renaming it")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.CreateContainer(containers)
      }

      And("Test copy container without force option")
      ex3 = the [com.ligadata.Exceptions.StorageDDLException] thrownBy {
	adapter.copyContainer(srcContainerName,destContainerName,false)
      }
      logger.info("Exception => " + ex3.cause)

      And("Test copy container with force")
      noException should be thrownBy {
	adapter.copyContainer(srcContainerName,destContainerName,true)
      }

      And("Test the non-existence of the source table")
      var srcTableName = sqlServerAdapter.getTableName(srcContainerName)
      exists = adapter.isTableExists(srcTableName)
      assert(exists == false)

      And("Test the existence of the destination table")
      var destTableName = sqlServerAdapter.getTableName(destContainerName)
      exists = adapter.isTableExists(destTableName)
      assert(exists == true)

      And("Test create container again after renaming it ")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.CreateContainer(containers)
      }

      And("Copy source table to destination table using force option")
      adapter.copyTable(srcTableName,destTableName,true)

      And("get all tables")
      var tbls = new Array[String](0)
      noException should be thrownBy {
	tbls = adapter.getAllTables
      }
      
      And("drop all tables")
      noException should be thrownBy {
	adapter.dropTables(tbls)
      }

      And("Test the existence of the source table after dropTables")
      exists = adapter.isTableExists(srcTableName)
      assert(exists == false)

      And("Test the existence of the destination table after dropTables")
      exists = adapter.isTableExists(destTableName)
      assert(exists == false)

      And("Test drop container again, cleanup")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ srcContainerName
	containers = containers :+ destContainerName
	adapter.DropContainer(containers)
      }

      And("Test the existence of the source container after DropContainer")
      exists = adapter.isContainerExists(srcContainerName)
      assert(exists == false)

      And("Test the existence of the destination container after DropContainer")
      exists = adapter.isContainerExists(destContainerName)
      assert(exists == false)
      
      And("Test create container for backup/restore tests")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.CreateContainer(containers)
      }

      And("Test backup container")
      noException should be thrownBy {
	adapter.backupContainer(containerName)
      }

      And("Test restore container")
      noException should be thrownBy {
	adapter.restoreContainer(containerName)
      }
      
      And("Test drop container again, cleanup")
      noException should be thrownBy {
	var containers = new Array[String](0)
	containers = containers :+ containerName
	adapter.DropContainer(containers)
      }

    }
  }
  override def afterAll = {
    var logFile = new java.io.File("logs")
    if( logFile != null ){
      deleteFile(logFile)
    }
  }
}
