package com.ligadata.HbaseInputOutputAdapters

import com.ligadata.Exceptions.KamanjaException
import com.ligadata.HeartBeat.MonitorComponentInfo
import com.ligadata.InputOutputAdapterInfo.{PartitionUniqueRecordValue, StartProcPartInfo, _}
import com.ligadata.KamanjaBase.{EnvContext, NodeContext}
import com.ligadata.Utils.ClusterStatus
import com.ligadata.adapterconfiguration.{HbaseAdapterConfiguration, HbasePartitionUniqueRecordKey, HbasePartitionUniqueRecordValue}
import org.apache.hadoop.hbase.client.Connection
import org.json4s.jackson.Serialization

import scala.actors.threadpool.ExecutorService
import scala.collection.mutable.ArrayBuffer

/**
  * Created by Yousef on 8/14/2016.
  */
case class HbaseExceptionInfo (Last_Failure: String, Last_Recovery: String)

object KamanjaHbaseConsumer  extends InputAdapterFactory {
  val INITIAL_SLEEP = 500
  val MAX_SLEEP = 30000
  val POLLING_INTERVAL = 100

  // Statistics Keys
  val ADAPTER_DESCRIPTION = "Hbase Consumer Client"

  def CreateInputAdapter(inputConfig: AdapterConfiguration, execCtxtObj: ExecContextFactory, nodeContext: NodeContext): InputAdapter = new KamanjaHbaseConsumer(inputConfig, execCtxtObj, nodeContext)
}
class KamanjaHbaseConsumer(val inputConfig: AdapterConfiguration, val execCtxtObj: ExecContextFactory, val nodeContext: NodeContext) extends InputAdapter{
  val input = this
  //  lazy val loggerName = this.getClass.getName
  private var isQuiese = false
  private var sleepDuration = 500
  lazy val LOG = logger
  private var lastSeen: String = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis))
  private val lock = new Object()
  private var readExecutor: ExecutorService = _
  private var metrics: collection.mutable.Map[String,Any] = collection.mutable.Map[String,Any]()
  private val adapterConfig = HbaseAdapterConfiguration.getAdapterConfig(inputConfig)
  private var isShutdown = false
  private var isQuiesced = false
  private var startTime: Long = 0
  private var msgCount = 0
  private var _ignoreFirstMsg : Boolean = _
  private var startHeartBeat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis))
  private var partitonCounts: collection.mutable.Map[String,Long] = collection.mutable.Map[String,Long]()
  private var partitonDepths: collection.mutable.Map[String,Long] = collection.mutable.Map[String,Long]()
  private var partitionExceptions: collection.mutable.Map[String,HbaseExceptionInfo] = collection.mutable.Map[String,HbaseExceptionInfo]()
  private var initialFilesHandled = false
  private var envContext : EnvContext = nodeContext.getEnvCtxt()
  private var clusterStatus : ClusterStatus = null
  metrics(com.ligadata.adapterconfiguration.KamanjaHbaseAdapterConstants.PARTITION_COUNT_KEYS) = partitonCounts
  metrics(com.ligadata.adapterconfiguration.KamanjaHbaseAdapterConstants.EXCEPTION_SUMMARY) = partitionExceptions
  metrics(com.ligadata.adapterconfiguration.KamanjaHbaseAdapterConstants.PARTITION_DEPTH_KEYS) = partitonDepths

  override def StartProcessing(partitionIds: Array[StartProcPartInfo], ignoreFirstMsg: Boolean): Unit = {
    isShutdown = false

    _ignoreFirstMsg = ignoreFirstMsg
    var lastHb: Long = 0
    startHeartBeat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis))

    LOG.info("Hbase_ADAPTER - START_PROCESSING CALLED")

    // Check to see if this already started
    if (startTime > 0) {
      LOG.error("Hbase_ADAPTER: already started, or in the process of shutting down")
    }
    startTime = System.nanoTime

    if (partitionIds == null || partitionIds.size == 0) {
      LOG.error("Hbase_ADAPTER: Cannot process the file adapter request, invalid parameters - number")
      return
    }

    partitionIds.foreach(part => {
      val partitionId = part._key.asInstanceOf[HbasePartitionUniqueRecordKey].PartitionId
      // Initialize the monitoring status
      partitonCounts(partitionId.toString) = 0
      partitonDepths(partitionId.toString) = 0
      partitionExceptions(partitionId.toString) = new HbaseExceptionInfo("n/a","n/a")
    })

    val partitionInfo = partitionIds.map(quad => {
      (quad._key.asInstanceOf[HbasePartitionUniqueRecordKey],
        quad._val.asInstanceOf[HbasePartitionUniqueRecordValue],
        quad._validateInfoVal.asInstanceOf[HbasePartitionUniqueRecordValue])
    })

    initialFilesHandled = false

 //////////////////////////////////   initializeNode//register the callbacks


    //(1,file1,0,true)~(2,file2,0,true)~(3,file3,1000,true)
    val myPartitionInfo = partitionIds.map(pid => (pid._key.asInstanceOf[HbasePartitionUniqueRecordKey].PartitionId,
      pid._val.asInstanceOf[HbasePartitionUniqueRecordValue].FileName,
      pid._val.asInstanceOf[HbasePartitionUniqueRecordValue].Offset, ignoreFirstMsg)).mkString("~")

//    val SendStartInfoToLeaderPath = sendStartInfoToLeaderParentPath + "/" + clusterStatus.nodeId  // Should be different for each Nodes
//    LOG.info("Hbase; Consumer - Node {} is sending start info to leader. path is {}, value is {} ",
//      clusterStatus.nodeId, SendStartInfoToLeaderPath, myPartitionInfo)
//    envContext.setListenerCacheKey(SendStartInfoToLeaderPath, myPartitionInfo) // => Goes to Leader
//
//    if(clusterStatus.isLeader)
//      handleStartInfo()  ///////fix this
  }

  override def Shutdown: Unit = lock.synchronized {
    StopProcessing
  }

  override def DeserializeKey(k: String): PartitionUniqueRecordKey = {
    val key = new HbasePartitionUniqueRecordKey
    try {
      LOG.debug("Deserializing Key:" + k)
      key.Deserialize(k)
    } catch {
      case e: Exception => {
        externalizeExceptionEvent(e)
        LOG.error("Failed to deserialize Key:%s.".format(k), e)
        throw e
      }
      case e: Throwable => {
        externalizeExceptionEvent(e)
        LOG.error("Failed to deserialize Key:%s.".format(k), e)
        throw e
      }
    }
    key
  }

  override def DeserializeValue(v: String): PartitionUniqueRecordValue = {
    val vl = new HbasePartitionUniqueRecordValue
    if (v != null) {
      try {
        LOG.debug("Deserializing Value:" + v)
        vl.Deserialize(v)
      } catch {
        case e: Exception => {
          externalizeExceptionEvent(e)
          LOG.error("Failed to deserialize Value:%s.".format(v), e)
          throw e
        }
        case e: Throwable => {
          externalizeExceptionEvent(e)
          LOG.error("Failed to deserialize Value:%s.".format(v), e)
          throw e
        }
      }
    }
    vl
  }

  private def getKeyValuePairs(): Array[(PartitionUniqueRecordKey, PartitionUniqueRecordValue)] = {
    val infoBuffer = ArrayBuffer[(PartitionUniqueRecordKey, PartitionUniqueRecordValue)]()

    for(partitionId <- 1 to 10/*adapterConfig.monitoringConfig.consumersCount*/){/////////////////////fix this
      val rKey = new HbasePartitionUniqueRecordKey
      val rValue = new HbasePartitionUniqueRecordValue

      rKey.PartitionId = partitionId
      rKey.Name = adapterConfig.Name

      rValue.Offset = -1
      rValue.FileName = ""

      infoBuffer.append((rKey, rValue))
    }

    infoBuffer.toArray
  }

  override def getAllPartitionBeginValues: Array[(PartitionUniqueRecordKey, PartitionUniqueRecordValue)] = lock.synchronized {
    getKeyValuePairs()
  }

  override def getAllPartitionEndValues: Array[(PartitionUniqueRecordKey, PartitionUniqueRecordValue)] = lock.synchronized {
    getKeyValuePairs()
  }

  private def resetSleepTimer(): Unit = {
    sleepDuration = KamanjaHbaseConsumer.INITIAL_SLEEP
  }

  override def GetAllPartitionUniqueRecordKey: Array[PartitionUniqueRecordKey] = lock.synchronized {
    LOG.debug("Getting all partionas for " + adapterConfig.Name)
    var results: java.util.List[String] = null //change this bug (string ->)
    var partitionNames: scala.collection.mutable.ListBuffer[HbasePartitionUniqueRecordKey] = scala.collection.mutable.ListBuffer()
    val hbaseutil: HbaseUtility = new HbaseUtility
    var HbaseConsumer: Connection = null
    var isSuccessfulConnection = false
    while (!isSuccessfulConnection && !isQuiese) {
      try {
        hbaseutil.createConnection(adapterConfig)
        HbaseConsumer = hbaseutil.getConnection()
      //  results = hbaseutil.partitionsFor(qc.topic)  //change it fix this bug
        isSuccessfulConnection = true
        HbaseConsumer.close()
      } catch {
        case e: Throwable => {
          externalizeExceptionEvent(e)
          LOG.error ("Exception processing PARTITIONSFOR request..  Retrying ",e)
          try {
            Thread.sleep(getSleepTimer)
          } catch {
            case ie: InterruptedException => {
              externalizeExceptionEvent(ie)
              LOG.warn("KamanjaHbaseConsumer - sleep interrupted, shutting donw ")
              Shutdown
              HbaseConsumer.close()
              HbaseConsumer = null
              throw ie
            }
            case t: Throwable => {
              externalizeExceptionEvent(t)
              LOG.warn("KamanjaHbaseConsumer - sleep interrupted (UNKNOWN CAUSE), shutting down ",t)
              Shutdown
              HbaseConsumer.close()
              HbaseConsumer = null
              throw t
            }
          }
        }
      }
    }
    resetSleepTimer
    if (isQuiese) {
      // return the info back to the Engine.  if quiesing
      LOG.warn("Quiese request is receive during GetAllPartitionUniqueRecordKey")
      partitionNames.toArray
    }

    if (results == null)  {
      // return the info back to the Engine.  Just in case we end up with null result
      LOG.warn("Kafka broker returned a null during GetAllPartitionUniqueRecordKey")
      partitionNames.toArray
    }

    // Successful fetch of metadata.. return the values to the engine.
    var iter = results.iterator
    while(iter.hasNext && !isQuiese) {
      var thisRes = iter.next()
      var newVal = new HbasePartitionUniqueRecordKey
      newVal.Name = adapterConfig.Name
//      newVal.PartitionId = thisRes.partition            ///////fix this
      partitionNames += newVal
   //   LOG.debug(" GetAllPartitions returned " +thisRes.partition)  ////fix this
    }
    partitionNames.toArray
  }

  private def getSleepTimer() : Int = {
    var thisSleep = sleepDuration
    sleepDuration = scala.math.max(KamanjaHbaseConsumer.MAX_SLEEP, thisSleep *2)
    return thisSleep
  }

  override def StopProcessing(): Unit = {
    isShutdown = true
    terminateReaderTasks
  }

  override def getComponentStatusAndMetrics: MonitorComponentInfo = {
    implicit val formats = org.json4s.DefaultFormats

    var depths:  Array[(PartitionUniqueRecordKey, PartitionUniqueRecordValue)] = null

    try {
      depths = getAllPartitionEndValues
    } catch {
      case e: KamanjaException => {
        return new MonitorComponentInfo(AdapterConfiguration.TYPE_INPUT, adapterConfig.Name, KamanjaHbaseConsumer.ADAPTER_DESCRIPTION, startHeartBeat, lastSeen, Serialization.write(metrics).toString)
      }
      case e: Exception => {
        LOG.error ("SMART-FILE-ADAPTER: Unexpected exception determining depths for smart file input adapter " + adapterConfig.Name, e)
        return new MonitorComponentInfo(AdapterConfiguration.TYPE_INPUT, adapterConfig.Name, KamanjaHbaseConsumer.ADAPTER_DESCRIPTION, startHeartBeat, lastSeen, Serialization.write(metrics).toString)
      }
    }

    return new MonitorComponentInfo(AdapterConfiguration.TYPE_INPUT, adapterConfig.Name, KamanjaHbaseConsumer.ADAPTER_DESCRIPTION,
      startHeartBeat, lastSeen,  Serialization.write(metrics).toString)
  }

  override def getComponentSimpleStats: String = {
    return "Input/"+ adapterConfig.Name +"/evtCnt" + "->" + msgCount
  }

  private def terminateReaderTasks(): Unit = {
    if (readExecutor == null) return

    // Give the threads to gracefully stop their reading cycles, and then execute them with extreme prejudice.
    Thread.sleep(adapterConfig.noDataSleepTimeInMs + 1)
    if (readExecutor != null) readExecutor.shutdownNow
    while (readExecutor != null && readExecutor.isTerminated == false) {
      Thread.sleep(100)
    }

    LOG.debug("Hbase_ADAPTER - Shutdown Complete")
    readExecutor = null
  }
}
