import com.ligadata.cache.DataCache

/**
  * Created by Saleh on 3/23/2016.
  */
object TestMaxSize {
  def main(args: Array[String]) {

    val aclass = Class.forName("com.ligadata.cache.MemoryDataCacheImp").newInstance
    val node = aclass.asInstanceOf[DataCache]

    node.init("""{"name":"CacheCluster","diskSpoolBufferSizeMB":"20","jgroups.tcpping.initial_hosts":"192.168.1.129[7800],192.168.1.129[7800]","jgroups.port":"7800","replicatePuts":"true","replicateUpdates":"true","replicateUpdatesViaCopy":"false","replicateRemovals":"true","replicateAsynchronously":"true","CacheConfig":{"maxBytesLocalHeap":"20971520","eternal":"false","bootstrapAsynchronously":"false","timeToIdleSeconds":"3000","timeToLiveSeconds":"3000","memoryStoreEvictionPolicy":"LFU","transactionalMode":"off","class":"net.sf.ehcache.distribution.jgroups.JGroupsCacheManagerPeerProviderFactory","separator":"::","peerconfig":"channelName=EH_CACHE::file=jgroups_tcp.xml","enableListener":"true"}}""", null)
    node.start()

    System.out.println("====================")
    for(i<-1 until 1000){
      node.put(i.toString,"this is a memory test"+i)
    }
    System.out.println("====================")

    System.out.println("====================")
    for(i<-1 until 1000) {
      System.out.println(node.get(i.toString).toString)
    }
    System.out.println("====================")

    System.out.println("====================")
    for(i<-1 until 1000) {
      System.out.println(node.get(i.toString).toString)
    }
    System.out.println("====================")

  }
}
