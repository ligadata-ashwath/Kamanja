
package com.ligadata.InputAdapters

import scala.actors.threadpool.{ Executors, ExecutorService }
import org.apache.log4j.Logger
import java.io.{ InputStream, FileInputStream }
import java.util.zip.GZIPInputStream
import java.nio.file.{ Paths, Files }
import com.ligadata.OnLEPBase.{ EnvContext, AdapterConfiguration, InputAdapter, InputAdapterObj, OutputAdapter, ExecContext, MakeExecContext, CountersAdapter }
import com.ligadata.AdaptersConfiguration.{ FileAdapterConfiguration, FilePartitionUniqueRecordKey, FilePartitionUniqueRecordValue }
import scala.util.control.Breaks._

object FileConsumer extends InputAdapterObj {
  def CreateInputAdapter(inputConfig: AdapterConfiguration, output: Array[OutputAdapter], envCtxt: EnvContext, mkExecCtxt: MakeExecContext, cntrAdapter: CountersAdapter): InputAdapter = new FileConsumer(inputConfig, output, envCtxt, mkExecCtxt, cntrAdapter)
}

class FileConsumer(val inputConfig: AdapterConfiguration, val output: Array[OutputAdapter], val envCtxt: EnvContext, val mkExecCtxt: MakeExecContext, cntrAdapter: CountersAdapter) extends InputAdapter {
  private[this] val LOG = Logger.getLogger(getClass);

  private[this] val fc = new FileAdapterConfiguration
  private[this] var uniqueKey: FilePartitionUniqueRecordKey = new FilePartitionUniqueRecordKey
  private[this] val lock = new Object()

  uniqueKey.Name = "File"

  //BUGBUG:: Not Checking whether inputConfig is really FileAdapterConfiguration or not. 

  fc.Typ = inputConfig.Typ
  fc.Name = inputConfig.Name
  fc.className = inputConfig.className
  fc.jarName = inputConfig.jarName
  fc.dependencyJars = inputConfig.dependencyJars

  // For File we expect the format "Type~AdapterName~ClassName~JarName~DependencyJars~CompressionString(GZ/BZ2)~FilesList~PrefixMessage~IgnoreLines~AddTimeStampToMsgFlag"
  if (inputConfig.adapterSpecificTokens.size != 5) {
    val err = "We should find only Type, AdapterName, ClassName, JarName, DependencyJars, CompressionString, FilesList, PrefixMessage, IgnoreLines & AddTimeStampToMsgFlag for File Adapter Config:" + inputConfig.Name
    LOG.error(err)
    throw new Exception(err)
  }

  if (inputConfig.adapterSpecificTokens(0).size > 0)
    fc.CompressionString = inputConfig.adapterSpecificTokens(0)

  fc.Files = inputConfig.adapterSpecificTokens(1).split(",").map(str => str.trim).filter(str => str.size > 0)
  if (inputConfig.adapterSpecificTokens(2).size > 0)
    fc.MessagePrefix = inputConfig.adapterSpecificTokens(2)
  if (inputConfig.adapterSpecificTokens(3).size > 0)
    fc.IgnoreLines = inputConfig.adapterSpecificTokens(3).toInt
  fc.AddTS2MsgFlag = (inputConfig.adapterSpecificTokens(4).compareToIgnoreCase("1") == 0)

  var executor: ExecutorService = _

  // LOG.info("FileConsumer")

  //BUGBUG:: Not validating the values in FileAdapterConfiguration 

  val input = this

  val execThread = mkExecCtxt.CreateExecContext(input, 0, output, envCtxt)

  class Stats {
    var totalLines: Long = 0;
    var totalSent: Long = 0
  }

  private def ProcessFile(sFileName: String, msg: String, st: Stats, ignorelines: Int, AddTS2MsgFlag: Boolean, isGz: Boolean): Unit = {
    var is: InputStream = null

    LOG.info("FileConsumer Processing File:" + sFileName)

    try {
      if (isGz)
        is = new GZIPInputStream(new FileInputStream(sFileName))
      else
        is = new FileInputStream(sFileName)
    } catch {
      case e: Exception =>
        LOG.error("Failed to open FileConsumer for %s. Message:%s".format(sFileName, e.getMessage))
        throw e
        return
    }

    val uniqueVal = new FilePartitionUniqueRecordValue
    uniqueVal.FileFullPath = sFileName

    val trimMsg = if (msg != null) msg.trim else null
    var len = 0
    var readlen = 0
    var totalLen: Int = 0
    var locallinecntr: Int = 0
    val maxlen = 1024 * 1024
    val buffer = new Array[Byte](maxlen)
    var tm = System.nanoTime
    var ignoredlines = 0
    try {
      breakable {
        do {
          readlen = is.read(buffer, len, maxlen - 1 - len)
          if (readlen > 0) {
            totalLen += readlen
            len += readlen
            var startidx: Int = 0
            var isrn: Boolean = false
            for (idx <- 0 until len) {
              if ((isrn == false && buffer(idx) == '\n') || (buffer(idx) == '\r' && idx + 1 < len && buffer(idx + 1) == '\n')) {
                locallinecntr += 1
                var strlen = idx - startidx
                if (ignoredlines < ignorelines) {
                  ignoredlines += 1
                } else {
                  if (strlen > 0) {
                    var readTmNs = System.nanoTime
                    var readTmMs = System.currentTimeMillis

                    val ln = new String(buffer, startidx, idx - startidx)
                    val sendmsg = (if (trimMsg != null && trimMsg.isEmpty() == false) (trimMsg + ",") else "") + (if (AddTS2MsgFlag) (readTmMs.toString + ",") else "") + ln

                    try {
                      // Creating new string to convert from Byte Array to string
                      uniqueVal.Offset = 0 //BUGBUG:: yet to fill this information
                      execThread.execute(sendmsg, uniqueKey, uniqueVal, readTmNs, readTmMs)
                    } catch {
                      case e: Exception => LOG.error("Failed with Message:" + e.getMessage)
                    }

                    st.totalSent += sendmsg.size
                  }
                }

                if (executor.isShutdown)
                  break

                startidx = idx + 1;
                if (buffer(idx) == '\r') // Inc one more char in case of \r \n
                {
                  startidx += 1;
                  isrn = true
                }
                st.totalLines += 1;

                val key = Category + "/" + fc.Name + "/evtCnt"
                cntrAdapter.addCntr(key, 1)

                val curTm = System.nanoTime
                if ((curTm - tm) > 1000000000L) {
                  tm = curTm
                  LOG.info("Time:%10dms, Lns:%8d, Sent:%15d".format(curTm / 1000000, st.totalLines, st.totalSent))
                }
              } else {
                isrn = false
              }
            }

            var destidx: Int = 0
            // move rest of the data left to starting of the buffer
            for (idx <- startidx until len) {
              buffer(destidx) = buffer(idx)
              destidx += 1
            }
            len = destidx
          }
        } while (readlen > 0)

        if (len > 0 && ignoredlines >= ignorelines) {
          var readTmNs = System.nanoTime
          var readTmMs = System.currentTimeMillis

          val ln = new String(buffer, 0, len)
          val sendmsg = (if (trimMsg != null && trimMsg.isEmpty() == false) (trimMsg + ",") else "") + (if (AddTS2MsgFlag) (readTmMs.toString + ",") else "") + ln

          try {
            // Creating new string to convert from Byte Array to string
            uniqueVal.Offset = 0 //BUGBUG:: yet to fill this information
            execThread.execute(sendmsg, uniqueKey, uniqueVal, readTmNs, readTmMs)
          } catch {
            case e: Exception => LOG.error("Failed with Message:" + e.getMessage)
          }

          st.totalSent += sendmsg.size
          st.totalLines += 1;
          val key = Category + "/" + fc.Name + "/evtCnt"
          // cntrAdapter.addCntr(key, 1)
        }
      }
    } catch {
      case e: Exception => {
        LOG.error("Failed with Reason:%s Message:%s".format(e.getCause, e.getMessage))
      }
    }

    val curTm = System.nanoTime
    LOG.info("Time:%10dms, Lns:%8d, Sent:%15d, Last, file:%s".format(curTm / 1000000, st.totalLines, st.totalSent, sFileName))
    is.close();
  }

  private def elapsedTm[A](f: => A): Long = {
    val s = System.nanoTime
    f
    (System.nanoTime - s)
  }

  override def Shutdown: Unit = lock.synchronized {
    StopProcessing
  }

  override def StopProcessing: Unit = lock.synchronized {
    if (executor == null) return

    executor.shutdown

    while (executor.isTerminated == false) {
      Thread.sleep(100) // sleep 100ms and then check
    }

    executor = null
  }

  override def StartProcessing(partitionUniqueRecordKeys: Array[String], partitionUniqueRecordValues: Array[String]): Unit = lock.synchronized {
    executor = Executors.newFixedThreadPool(1)
    executor.execute(new Runnable() {
      override def run() {

        // LOG.info("FileConsumer.run")

        val s = System.nanoTime

        var tm: Long = 0
        val st: Stats = new Stats
        val compString = if (fc.CompressionString == null) null else fc.CompressionString.trim
        val isTxt = (compString == null || compString.size == 0)
        val isGz = (compString != null && compString.compareToIgnoreCase("gz") == 0)
        fc.Files.foreach(fl => {
          if (isTxt || isGz) {
            tm = tm + elapsedTm(ProcessFile(fl, fc.MessagePrefix, st, fc.IgnoreLines, fc.AddTS2MsgFlag, isGz))
          } else {
            throw new Exception("Not yet handled other than text & GZ files")
          }
          if (executor.isShutdown) {
            LOG.info("Stop processing File:%s in the middle ElapsedTime:%.02fms".format(fl, tm / 1000000.0))
            break
          } else {
            LOG.info("File:%s ElapsedTime:%.02fms".format(fl, tm / 1000000.0))
          }
        })
        /*
      if (st.totalLines > 0) {
        val rem = (st.totalLines - (st.totalLines / 100) * 100)
        if (rem > 0) {
          val key = Category + "/" + fc.Name + "/evtCnt"
          cntrAdapter.addCntr(key, rem)

        }
      }
*/
        LOG.info("Done. ElapsedTime:%.02fms".format((System.nanoTime - s) / 1000000.0))
      }
    });

  }

  override def GetAllPartitionUniqueRecordKey: Array[String] = lock.synchronized {
    if (uniqueKey != null) {
      return Array(uniqueKey.Serialize)
    }
    null
  }

}

