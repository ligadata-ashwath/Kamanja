package com.ligadata.metadataapiservice

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.io.IO

import spray.routing.RequestContext
import spray.httpx.SprayJsonSupport
import spray.client.pipelining._
import com.ligadata.kamanja.metadata._
import scala.util.{ Success, Failure }

import com.ligadata.MetadataAPI._

object AddFunctionService {
  case class Process(functionJson:String, formatType:String)
}

class AddFunctionService(requestContext: RequestContext, userid:Option[String], password:Option[String], cert:Option[String]) extends Actor {

  import AddFunctionService._
  
  implicit val system = context.system
  import system.dispatcher
  val log = Logging(system, getClass)
   val APIName = "AddFunctionService"
  
  def receive = {
    case Process(functionJson, formatType) =>
      process(functionJson, formatType)
      context.stop(self)
  }
  
  def process(functionJson:String, formatType:String): Unit = {
    
    log.debug("Requesting AddFunction {},{}",functionJson,formatType)
    
    var nameVal: String = null
    if (formatType.equalsIgnoreCase("json")) {
      nameVal = APIService.extractNameFromJson(functionJson,AuditConstants.FUNCTION) 
    } else {
      requestContext.complete(new ApiResult(ErrorCodeConstants.Failure, APIName, null, "Error:Unsupported format: "+formatType).toString ) 
      return
    }

    if (!MetadataAPIImpl.checkAuth(userid,password,cert, MetadataAPIImpl.getPrivilegeName("insert","function"))) {
      MetadataAPIImpl.logAuditRec(userid,Some(AuditConstants.WRITE),AuditConstants.INSERTOBJECT,functionJson,AuditConstants.FAIL,"",nameVal)
      requestContext.complete(new ApiResult(ErrorCodeConstants.Failure, APIName, null,  "Error:UPDATE not allowed for this user").toString )
    } else {
      val apiResult = MetadataAPIImpl.AddFunctions(functionJson,formatType,userid)    
      requestContext.complete(apiResult)     
    }
  }
}
