package com.ligadata.adapterconfiguration

import com.ligadata.Exceptions.{FatalAdapterException, KamanjaException}
import com.ligadata.InputOutputAdapterInfo.{AdapterConfiguration, PartitionUniqueRecordKey, PartitionUniqueRecordValue}
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * Created by Yousef on 8/13/2016.
  */
class HbaseAdapterConfiguration extends AdapterConfiguration{
  var host: String = null //folder to write files
  var scehmaName: String = "" // prefix for the file names
  var TableName: String = "" // optional separator inserted between messages
  var kerberos: KerberosConfig = null
  var instancePartitions: Set[Int] = _
  var noDataSleepTimeInMs: Int = 300
}

class KerberosConfig {
  var principal: String = null
  var keytab: String = null
}

object HbaseAdapterConfiguration {

  def getAdapterConfig(inputConfig: AdapterConfiguration): HbaseAdapterConfiguration = {

    if (inputConfig.adapterSpecificCfg == null || inputConfig.adapterSpecificCfg.size == 0) {
      val err = "Not found Type and Connection info for Smart File Adapter Config:" + inputConfig.Name
      throw new KamanjaException(err, null)
    }

    val adapterConfig = new HbaseAdapterConfiguration()
    adapterConfig.Name = inputConfig.Name
    adapterConfig.className = inputConfig.className
    adapterConfig.jarName = inputConfig.jarName
    adapterConfig.dependencyJars = inputConfig.dependencyJars

    val adapCfg = parse(inputConfig.adapterSpecificCfg)
    if (adapCfg == null || adapCfg.values == null) {
      val err = "Smart File Producer configuration must be specified for " + adapterConfig.Name
      throw new KamanjaException(err, null)
    }

    val adapCfgValues = adapCfg.values.asInstanceOf[Map[String, Any]]
    adapCfgValues.foreach(kv => {
      if (kv._1.compareToIgnoreCase("host") == 0) {
        adapterConfig.host = kv._2.toString.trim
      } else if (kv._1.compareToIgnoreCase("scehmaName") == 0) {
        adapterConfig.scehmaName = kv._2.toString.trim
      } else if (kv._1.compareToIgnoreCase("TableName") == 0) {
        adapterConfig.TableName = kv._2.toString
      } else if (kv._1.compareToIgnoreCase("Kerberos") == 0) {
        adapterConfig.kerberos = new KerberosConfig()
        val kerbConf = kv._2.asInstanceOf[Map[String, String]]
        adapterConfig.kerberos.principal = kerbConf.getOrElse("Principal", null)
        adapterConfig.kerberos.keytab = kerbConf.getOrElse("Keytab", null)
      }
    })

    adapterConfig.instancePartitions = Set[Int]()

    if (adapterConfig.host == null || adapterConfig.host.size == 0)
      throw FatalAdapterException("host should not be NULL or empty for Hbase Producer" + adapterConfig.Name, new Exception("Invalid Parameters"))

    if (adapterConfig.kerberos != null) {
      if (adapterConfig.kerberos.principal == null || adapterConfig.kerberos.principal.size == 0)
        throw FatalAdapterException("Principal should be specified for Kerberos authentication for Hbase Producer: " + adapterConfig.Name, new Exception("Invalid Parameters"))

      if (adapterConfig.kerberos.keytab == null || adapterConfig.kerberos.keytab.size == 0)
        throw FatalAdapterException("Keytab should be specified for Kerberos authentication for Hbase Producer: " + adapterConfig.Name, new Exception("Invalid Parameters"))
    }

    adapterConfig
  }
}

case class HbaseKeyData(Version: Int, Type: String, Name: String, PartitionId: Int)

class HbasePartitionUniqueRecordKey extends PartitionUniqueRecordKey {
  val Version: Int = 1
  var Name: String = _ // Name
  val Type: String = "Hbase"
  var PartitionId: Int = _ // Partition Id

  override def Serialize: String = { // Making String from key
  val json =
    ("Version" -> Version) ~
      ("Type" -> Type) ~
      ("Name" -> Name) ~
      ("PartitionId" -> PartitionId)
    compact(render(json))
  }

  override def Deserialize(key: String): Unit = { // Making Key from Serialized String
  implicit val jsonFormats: Formats = DefaultFormats
    val keyData = parse(key).extract[HbaseKeyData]
    if (keyData.Version == Version && keyData.Type.compareTo(Type) == 0) {
      Name = keyData.Name
      PartitionId = keyData.PartitionId
    }
  }
}

case class HbaseRecData(Version: Int, FileName : String, Offset: Option[Long])

class HbasePartitionUniqueRecordValue extends PartitionUniqueRecordValue {
  val Version: Int = 1
  var FileName : String = _
  var Offset: Long = -1 // Offset of next message in the file

  override def Serialize: String = {
    // Making String from Value
    val json =
      ("Version" -> Version) ~
        ("Offset" -> Offset) ~
        ("FileName" -> FileName)
    compact(render(json))
  }

  override def Deserialize(key: String): Unit = {
    // Making Value from Serialized String
    implicit val jsonFormats: Formats = DefaultFormats
    val recData = parse(key).extract[HbaseRecData]
    if (recData.Version == Version) {
      Offset = recData.Offset.get
      FileName = recData.FileName
    }
    // else { } // Not yet handling other versions
  }
}

object KamanjaHbaseAdapterConstants {
  // Statistics Keys
  val PARTITION_COUNT_KEYS = "Partition Counts"
  val PARTITION_DEPTH_KEYS = "Partition Depths"
  val EXCEPTION_SUMMARY = "Exception Summary"
}
