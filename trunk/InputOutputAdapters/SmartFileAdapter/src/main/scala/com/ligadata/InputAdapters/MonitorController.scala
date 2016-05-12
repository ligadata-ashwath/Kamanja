package com.ligadata.InputAdapters

import java.io.IOException

import com.ligadata.AdaptersConfiguration.SmartFileAdapterConfiguration
import com.ligadata.Exceptions.KamanjaException
import org.apache.logging.log4j.LogManager

import scala.actors.threadpool.{Executors, ExecutorService}
import scala.collection.mutable.ArrayBuffer

/**
  *
  * @param adapterConfig
  * @param newFileDetectedCallback callback to notify leader whenever a file is detected
  */
class MonitorController(adapterConfig : SmartFileAdapterConfiguration,
                        newFileDetectedCallback :(String) => Unit) {

  val NOT_RECOVERY_SITUATION = -1

  private val bufferingQ_map: scala.collection.mutable.Map[SmartFileHandler, (Long, Long, Int)] = scala.collection.mutable.Map[SmartFileHandler, (Long, Long, Int)]()
  private val bufferingQLock = new Object
  private var smartFileMonitor : SmartFileMonitor = null

  private var fileQ: scala.collection.mutable.PriorityQueue[EnqueuedFileHandler] =
      new scala.collection.mutable.PriorityQueue[EnqueuedFileHandler]()(Ordering.by(OldestFile))
  private val fileQLock = new Object

  private var refreshRate: Int = 2000 //Refresh rate for monitorBufferingFiles
  private var bufferTimeout: Int = 300000  // Default to 5 minutes
  private var maxTimeFileAllowedToLive: Int = 3000  // default to 50 minutes.. will be multiplied by 1000 later
  private var maxBufferErrors = 5

  private var keepMontoringBufferingFiles = false
  var globalFileMonitorService: ExecutorService = Executors.newFixedThreadPool(2)

  lazy val loggerName = this.getClass.getName
  lazy val logger = LogManager.getLogger(loggerName)

  private var initialFiles :  List[(String, Int, String, Int)] = null

  def init(files :  List[(String, Int, String, Int)]): Unit ={
    initialFiles = files
  }

  def checkConfigDirsAccessibility(): Unit ={
    adapterConfig.monitoringConfig.locations.foreach(dir => {
      val handler = SmartFileHandlerFactory.createSmartFileHandler(adapterConfig, dir)
      if(!handler.isAccessible)
        throw new KamanjaException("Smart File Consumer - Dir to watch " + dir + " is not accessible. It must be readable and writable", null)
    })

    val handler = SmartFileHandlerFactory.createSmartFileHandler(adapterConfig, adapterConfig.monitoringConfig.targetMoveDir)
    if(!handler.isAccessible)
      throw new KamanjaException("Smart File Consumer - Target Dir " + adapterConfig.monitoringConfig.targetMoveDir + " is not accessible. It must be readable and writable", null)
  }

  def startMonitoring(): Unit ={
    smartFileMonitor = SmartFileMonitorFactory.createSmartFileMonitor(adapterConfig.Name, adapterConfig._type, fileDetectedCallback)
    smartFileMonitor.init(adapterConfig.adapterSpecificCfg)
    logger.debug("SMART FILE CONSUMER (MonitorController):  running smartFileMonitor.monitor()")
    smartFileMonitor.monitor()

    keepMontoringBufferingFiles = true

    globalFileMonitorService.execute(new Runnable() {
      override def run() = {
        logger.debug("SMART FILE CONSUMER (MonitorController):  buffering files monitoring thread run")
        //while(true) {
          monitorBufferingFiles
        //}
      }
    })
  }

  def stopMonitoring(): Unit ={

    logger.debug("MonitorController - shutting down")

    if(smartFileMonitor != null)
      smartFileMonitor.shutdown()
    else
      logger.debug("smartFileMonitor is null")

    keepMontoringBufferingFiles = false
    MonitorUtils.shutdownAndAwaitTermination(globalFileMonitorService, "MonitorController globalFileMonitorService")
  }

  /**
    * this method is used as callback to be passed to monitor
    * it basically does what method processExistingFiles used to do in file consumer tool
    * @param fileHandler
    */
  def fileDetectedCallback (fileHandler : SmartFileHandler) : Unit = {
    logger.debug("SMART FILE CONSUMER (MonitorController): got file {}", fileHandler.getFullPath)
    //if (MonitorUtils.isValidFile(fileHandler))
    enQBufferedFile(fileHandler)
  }

  private def enQBufferedFile(fileHandler: SmartFileHandler): Unit = {
    bufferingQLock.synchronized {
      bufferingQ_map(fileHandler) = (0L, System.currentTimeMillis(),0) // Initially, always set to 0.. this way we will ensure that it has time to be processed
    }
  }

  // Stuff used by the File Priority Queue.
  def OldestFile(file: EnqueuedFileHandler): Long = {
    file.createDate * -1
  }

  /**
    *  Look at the files on the DEFERRED QUEUE... if we see that it stops growing, then move the file onto the READY
    *  to process QUEUE.
    */
  private def monitorBufferingFiles: Unit = {
    // This guys will keep track of when to exgernalize a WARNING Message.  Since this loop really runs every second,
    // we want to throttle the warning messages.
    logger.debug("SMART FILE CONSUMER (MonitorController):  monitorBufferingFiles")

    var specialWarnCounter: Int = 1

    var checkCount = 1
    while (keepMontoringBufferingFiles) {
      // Scan all the files that we are buffering, if there is not difference in their file size.. move them onto
      // the FileQ, they are ready to process.
      bufferingQLock.synchronized {

        val newlyAdded = ArrayBuffer[SmartFileHandler]()

        //val iter = bufferingQ_map.iterator
        bufferingQ_map.foreach(fileTuple => {

          //TODO C&S - changes
          var thisFileFailures: Int = fileTuple._2._3
          var thisFileStarttime: Long = fileTuple._2._2
          var thisFileOrigLength: Long = fileTuple._2._1


          try {
            val fileHandler = fileTuple._1

            logger.debug("SMART FILE CONSUMER (MonitorController):  monitorBufferingFiles - file " + fileHandler.getFullPath)

            val matchingFileInfo : List[(String, Int, String, Int)] =
              if (initialFiles ==null) null
              else initialFiles.filter(tuple => tuple._3.equals(fileHandler.getFullPath))

            if (matchingFileInfo != null && matchingFileInfo.size > 0) {
              //this is an initial file, the leader will take care of it, ignore
              /*initialFiles.filter(tuple => tuple._3.equals(fileHandler.getFullPath)) match{
                case None =>
                case Some(initialFileInfo) => initialFiles = initialFiles diff List(initialFileInfo)
              }*/
              logger.debug("SMART FILE CONSUMER (MonitorController): file {} is already in initial files", fileHandler.getFullPath)
              bufferingQ_map.remove(fileHandler)
              initialFiles = initialFiles diff matchingFileInfo

              logger.debug("SMART FILE CONSUMER (MonitorController): now initialFiles = {}",initialFiles)
            }
            else {
              // If the filesystem is accessible
              if (fileHandler.exists) {

                //TODO C&S - Changes
                thisFileOrigLength = fileHandler.length

                // If file hasn't grown in the past 2 seconds - either a delay OR a completed transfer.
                if (fileTuple._2._1 == thisFileOrigLength) {
                  // If the length is > 0, we assume that the file completed transfer... (very problematic, but unless
                  // told otherwise by BofA, not sure what else we can do here.
                  if (thisFileOrigLength > 0 && MonitorUtils.isValidFile(fileHandler)) {
                    if(isEnqueued(fileTuple._1)){
                      logger.debug("SMART FILE CONSUMER (MonitorController):  File already enqueued " + fileHandler.getFullPath)
                    }else{
                      logger.info("SMART FILE CONSUMER (MonitorController):  File READY TO PROCESS " + fileHandler.getFullPath)
                      enQFile(fileTuple._1, NOT_RECOVERY_SITUATION, fileHandler.lastModified)
                      newlyAdded.append(fileHandler)
                    }
                    bufferingQ_map.remove(fileTuple._1)

                  } else {
                    // Here becayse either the file is sitll of len 0,or its deemed to be invalid.
                    if (thisFileOrigLength == 0) {
                      val diff = System.currentTimeMillis - thisFileStarttime //d.lastModified
                      if (diff > bufferTimeout) {
                        logger.warn("SMART FILE CONSUMER (MonitorController): Detected that " + fileHandler.getFullPath + " has been on the buffering queue longer then " + bufferTimeout / 1000 + " seconds - Cleaning up")
                        moveFile(fileTuple._1)
                        bufferingQ_map.remove(fileTuple._1)
                      }
                    } else {
                      //Invalid File - due to content type
                      logger.error("SMART FILE CONSUMER (MonitorController): Moving out " + fileHandler.getFullPath + " with invalid file type ")
                      moveFile(fileTuple._1)
                      bufferingQ_map.remove(fileTuple._1)
                    }
                  }
                } else {
                  logger.debug("SMART FILE CONSUMER (MonitorController):  File {} size changed from {} to {}",
                    fileHandler.getFullPath, thisFileOrigLength.toString, fileTuple._2._1.toString)
                  bufferingQ_map(fileTuple._1) = (thisFileOrigLength, thisFileStarttime, thisFileFailures)
                }
              } else {
                // File System is not accessible.. issue a warning and go on to the next file.
                logger.warn("SMART FILE CONSUMER (MonitorController): File on the buffering Q is not found " + fileHandler.getFullPath)
                bufferingQ_map.remove(fileTuple._1)
              }
            }
          } catch {
            case ioe: IOException => {
              thisFileFailures += 1
              if ((System.currentTimeMillis - thisFileStarttime) > maxTimeFileAllowedToLive && thisFileFailures > maxBufferErrors) {
                logger.warn("SMART FILE CONSUMER (MonitorController): Detected that a stuck file " + fileTuple._1.getFullPath + " on the buffering queue", ioe)
                try {
                  moveFile(fileTuple._1)
                  bufferingQ_map.remove(fileTuple._1)
                } catch {
                  case e: Throwable => {
                    logger.error("SMART_FILE_CONSUMER: Failed to move file, retyring", e)
                  }
                }
              } else {
                bufferingQ_map(fileTuple._1) = (thisFileOrigLength, thisFileStarttime, thisFileFailures)
                logger.warn("SMART_FILE_CONSUMER: IOException trying to monitor the buffering queue ", ioe)
              }
            }
            case e: Throwable => {
              thisFileFailures += 1
              if ((System.currentTimeMillis - thisFileStarttime) > maxTimeFileAllowedToLive && thisFileFailures > maxBufferErrors) {
                logger.error("SMART FILE CONSUMER (MonitorController): Detected that a stuck file " + fileTuple._1 + " on the buffering queue", e)
                try {
                  moveFile(fileTuple._1)
                  bufferingQ_map.remove(fileTuple._1)
                } catch {
                  case e: Throwable => {
                    logger.error("SMART_FILE_CONSUMER (MonitorController): Failed to move file, retyring", e)
                  }
                }
              } else {
                bufferingQ_map(fileTuple._1) = (thisFileOrigLength, thisFileStarttime, thisFileFailures)
                logger.error("SMART_FILE_CONSUMER: IOException trying to monitor the buffering queue ", e)
              }
            }
          }

        })

        newlyAdded.foreach(fileHandler => {
          //notify leader about the new files
          if(newFileDetectedCallback != null){
            logger.debug("Smart File Adapter (MonitorController) - New file is enqueued in monitor controller queue ({})", fileHandler.getFullPath)
            newFileDetectedCallback(fileHandler.getFullPath)
          }
        })

      }

      checkCount += 1
      if(checkCount > 3)
        initialFiles = null

      // Give all the files a 1 second to add a few bytes to the contents
      try {
        Thread.sleep(refreshRate)
      }
      catch{case e : Throwable => }
    }
  }

  private def enQFile(fileHandler: SmartFileHandler, offset: Int, createDate: Long, partMap: scala.collection.mutable.Map[Int,Int] = scala.collection.mutable.Map[Int,Int]()): Unit = {
    fileQLock.synchronized {
      logger.info("SMART FILE CONSUMER (MonitorController):  enq file " + fileHandler.getFullPath + " with priority " + createDate+" --- curretnly " + fileQ.size + " files on a QUEUE")
      fileQ += new EnqueuedFileHandler(fileHandler, offset, createDate, partMap)
    }
  }

  private def isEnqueued(fileHandler: SmartFileHandler) : Boolean = {
    fileQLock.synchronized {
      if (fileQ.isEmpty) {
        return false
      }
      fileQ.exists(f => f.fileHandler.getFullPath.equals(fileHandler.getFullPath))
    }
  }

  private def deQFile: EnqueuedFileHandler = {
    fileQLock.synchronized {
      if (fileQ.isEmpty) {
        return null
      }
      val ef = fileQ.dequeue()
      logger.info("SMART FILE CONSUMER (MonitorController):  deq file " + ef.fileHandler.getFullPath + " with priority " + ef.createDate+" --- curretnly " + fileQ.size + " files left on a QUEUE")
      return ef

    }
  }

  //get file name only for now
  def getNextFileToProcess : String = {
    val f = deQFile
    if(f == null) null else f.fileHandler.getFullPath
  }

  private def moveFile(fileHandler: SmartFileHandler): Unit = {
    val targetMoveDir = adapterConfig.monitoringConfig.targetMoveDir

    val fileStruct = fileHandler.getFullPath.split("/")
    logger.info("SMART FILE CONSUMER Moving File" + fileHandler.getFullPath+ " to " + targetMoveDir)
    if (fileHandler.exists()) {
      fileHandler.moveTo(targetMoveDir + "/" + fileStruct(fileStruct.size - 1))
      //fileCacheRemove(fileHandler.getFullPath)
    } else {
      logger.warn("SMART FILE CONSUMER File has been deleted" + fileHandler.getFullPath);
    }
  }
}
