package com.ligadata.BasicCacheConcurrency

import net.sf.ehcache.config.CacheConfiguration


/**
  * Created by Saleh on 3/21/2016.
  */
class CacheCustomConfig(jsonString:String) extends CacheConfiguration{
  val json = org.json4s.jackson.JsonMethods.parse(jsonString)
  val values = json.values.asInstanceOf[Map[String, String]]

  /*
             name="Node"
             maxEntriesLocalHeap="10000"
             maxEntriesLocalDisk="1000"
             eternal="false"
             diskSpoolBufferSizeMB="20"
             timeToIdleSeconds="300"
             timeToLiveSeconds="600"
             memoryStoreEvictionPolicy="LFU"
             transactionalMode="off"
   */

  this.name(values.getOrElse("name","Node"))
    .eternal(values.getOrElse("eternal","false").toBoolean)
    .maxEntriesLocalHeap(values.getOrElse("maxEntriesLocalHeap","10000").toInt)
    .maxEntriesLocalDisk(values.getOrElse("maxEntriesLocalDisk","1000").toInt)
    .diskSpoolBufferSizeMB(values.getOrElse("diskSpoolBufferSizeMB","20").toInt)
    .timeToLiveSeconds(values.getOrElse("timeToLiveSeconds","600").toInt)
    .timeToIdleSeconds(values.getOrElse("timeToIdleSeconds","300").toInt)
    .memoryStoreEvictionPolicy(values.getOrElse("memoryStoreEvictionPolicy","LFU"))
    .transactionalMode(values.getOrElse("transactionalMode","off"))
}
