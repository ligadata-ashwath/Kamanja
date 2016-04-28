package com.ligadata.InputAdapters

import java.io.IOException
import com.ligadata.AdaptersConfiguration.SmartFileAdapterConfiguration
import org.apache.logging.log4j.LogManager

import scala.actors.threadpool.Executors

/**
  *
  *
  * @param adapterConfig
  * @param fileHandler file to read messages from
  * @param startOffset offset in the file to start with
  * @param consumerContext has required params
  * @param messageFoundCallback to call for every read message
  * @param finishCallback call when finished reading
  */
class FileMessageExtractor(adapterConfig : SmartFileAdapterConfiguration,
                           fileHandler: SmartFileHandler,
                           startOffset : Int,
                           consumerContext : SmartFileConsumerContext,
                           messageFoundCallback : (SmartFileMessage, SmartFileConsumerContext) => Unit,
                           finishCallback : (SmartFileHandler, SmartFileConsumerContext, Int) => Unit ) {

  private val maxlen: Int = adapterConfig.monitoringConfig.workerBufferSize * 1024 * 1024 //in MB
  private val message_separator : Char = adapterConfig.monitoringConfig.messageSeparator
  private val message_separator_len = 1// since separator is a char

  lazy val loggerName = this.getClass.getName
  lazy val logger = LogManager.getLogger(loggerName)
  private var currentMsgNum = -1 // to start from zero
  private var globalOffset = 0

  private val extractExecutor = Executors.newFixedThreadPool(1)
  private val updatExecutor = Executors.newFixedThreadPool(1)

  private val StatusUpdateInterval = 1000//ms

  private var finished = false



  def extractMessages() : Unit = {

    if(!fileHandler.exists()){
      finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_NOT_FOUND)
    }

    else {
      //just run it in a separate thread
      val extractorThread = new Runnable() {
        override def run(): Unit = {
          readBytesChunksFromFile()
        }
      }
      extractExecutor.execute(extractorThread)

      //keep updating status so leader knows participant is working fine
      //TODO : find a way to send the update in same reading thread
      val statusUpdateThread = new Runnable() {
        override def run(): Unit = {
          try {
            while (!finished) {
              //put filename~offset~timestamp
              val data = fileHandler.getFullPath + "~" + currentMsgNum + "~" + System.nanoTime
              logger.debug("SMART FILE CONSUMER - Node {} with partition {} is updating status to value {}",
                consumerContext.nodeId, consumerContext.partitionId.toString, data)
              consumerContext.envContext.saveConfigInClusterCache(consumerContext.statusUpdateCacheKey, data.getBytes)


              Thread.sleep(StatusUpdateInterval)

            }
          }
          catch {
            case ie: InterruptedException => {}
            case e: Exception => logger.error("", e)
          }
        }
      }
      updatExecutor.execute(statusUpdateThread)
    }
  }

  private def readBytesChunksFromFile(): Unit = {

    val byteBuffer = new Array[Byte](maxlen)

    var readlen = 0
    var len: Int = 0

    val fileName = fileHandler.getFullPath

    logger.info("Smart File Consumer - Starting reading messages from file " + fileName)

    try {
      fileHandler.openForRead()
    } catch {

      case fio: java.io.FileNotFoundException => {
        logger.error("SMART_FILE_CONSUMER Exception accessing the file for processing the file - File is missing",fio)
        finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_NOT_FOUND)
        shutdownThreads
        return
      }
      case fio: IOException => {
        logger.error("SMART_FILE_CONSUMER Exception accessing the file for processing ",fio)
        finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_CORRUPT)
        shutdownThreads
        return
      }
      case ex : Exception => {
        logger.error("", ex)
        finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_CORRUPT)
        shutdownThreads

        return
      }
    }

    var curReadLen = 0
    var lastReadLen = 0

    //skip to startOffset
    //TODO : modify to use seek whenever possible
    var totalReadLen = 0
    do{
      curReadLen = fileHandler.read(byteBuffer, 0, Math.min(maxlen, startOffset))
      totalReadLen += curReadLen
    }while(totalReadLen < startOffset && curReadLen >0)
    globalOffset = totalReadLen

    curReadLen = 0

    try {

      do {
        try {

          var curReadLen = fileHandler.read(byteBuffer, readlen, maxlen - readlen - 1)
          lastReadLen = curReadLen

          if (curReadLen > 0){
            readlen += curReadLen
          }
          else // First time reading into buffer triggered end of file (< 0)
            readlen = curReadLen
          val minBuf = maxlen / 3; // We are expecting at least 1/3 of the buffer need to fill before
          while (readlen < minBuf && curReadLen > 0) {
            // Re-reading some more data
            curReadLen = fileHandler.read(byteBuffer, readlen, maxlen - readlen - 1)
            if (curReadLen > 0){
              readlen += curReadLen
            }
            lastReadLen = curReadLen
          }

        } catch {

          case ioe: IOException => {
            logger.error("Failed to read file " + fileName, ioe)
            finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_CORRUPT)
            shutdownThreads
            return
          }
          case e: Throwable => {
            logger.error("Failed to read file, file corrupted " + fileName, e)
            finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_CORRUPT)
            shutdownThreads
            return
          }
        }
        if (readlen > 0) {
          len += readlen

          //e.g we have 1024, but 1000 is consumeByte
          val consumedBytes = extractMessages(byteBuffer, readlen)
          if (consumedBytes < readlen) {
            val remainigBytes = readlen - consumedBytes
            val newByteBuffer = new Array[Byte](maxlen)
            // copy reaming from byteBuffer to byteBuffer
            /*System.arraycopy(byteBuffer, consumedBytes + 1, newByteBuffer, 0, remainigBytes)
            byteBuffer = newByteBuffer*/
            for(i <- 0 to readlen - consumedBytes){
              byteBuffer(i) = byteBuffer(consumedBytes + i)
            }

            readlen = readlen - consumedBytes
          }
          else {
            readlen = 0
          }
        }

      } while (lastReadLen > 0)

    }
    catch {
      case ioe: IOException => {
        logger.error("SMART FILE CONSUMER: Exception while accessing the file for processing " + fileName, ioe)
        finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_CORRUPT)
        shutdownThreads
        return
      }
    }

    // Done with this file... mark is as closed
    try {
      if (fileHandler != null) fileHandler.close

    } catch {
      case ioe: IOException => {
        logger.error("SMART FILE CONSUMER: Exception while closing file " + fileName, ioe)
      }
    }
    finally{
      if(finishCallback != null)
        finishCallback(fileHandler, consumerContext, SmartFileConsumer.FILE_STATUS_FINISHED)
      shutdownThreads()
    }

  }

  private def shutdownThreads(): Unit ={
    finished = true
    logger.debug("File message Extractor - shutting down updatExecutor")
    MonitorUtils.shutdownAndAwaitTermination(updatExecutor, "file message extracting status updator")

    logger.debug("File message Extractor - shutting down extractExecutor")
    MonitorUtils.shutdownAndAwaitTermination(extractExecutor, "file message extractor")
  }

  private def extractMessages(chunk : Array[Byte], len : Int) : Int = {
    var indx = 0
    var prevIndx = indx

    for(i <- 0 to len - 1){
      if (chunk(i).asInstanceOf[Char] == message_separator) {
        val newMsg: Array[Byte] = chunk.slice(prevIndx, indx)
        if(newMsg.length > 0) {
          currentMsgNum += 1
          //if(globalOffset >= startOffset) {//send messages that are only after startOffset
          val msgOffset = globalOffset + newMsg.length + message_separator_len //byte offset of next message in the file
          val smartFileMessage = new SmartFileMessage(newMsg, msgOffset, false, false, fileHandler, null, msgOffset)
            messageFoundCallback(smartFileMessage, consumerContext)

          //}
          prevIndx = indx + 1
          globalOffset = globalOffset + newMsg.length + message_separator_len
        }
      }
      indx = indx + 1
    }



    /*if(prevIndx == chunk.length)
      Array()
    else
      chunk.slice(prevIndx, chunk.length)*/
    prevIndx
  }
}
