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

package com.ligadata.MetadataAPI

import java.util.Properties
import java.io._
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.text.ParseException
import com.ligadata.MetadataAPI.MetadataAPI.ModelType
import com.ligadata.MetadataAPI.MetadataAPI.ModelType.ModelType

import scala.Enumeration
import scala.io._
import scala.collection.mutable.ArrayBuffer

import scala.collection.mutable._
import scala.reflect.runtime.{ universe => ru }

import com.ligadata.kamanja.metadata.ObjType._
import com.ligadata.kamanja.metadata._
import com.ligadata.kamanja.metadata.MdMgr._

import com.ligadata.kamanja.metadataload.MetadataLoad

// import com.ligadata.keyvaluestore._
import com.ligadata.HeartBeat.{ MonitoringContext, HeartBeatUtil }
import com.ligadata.StorageBase.{ DataStore, Transaction }
import com.ligadata.KvBase.{ Key, TimeRange }

import scala.util.parsing.json.JSON
import scala.util.parsing.json.{ JSONObject, JSONArray }
import scala.collection.immutable.Map
import scala.collection.immutable.HashMap
import scala.collection.mutable.HashMap

import com.google.common.base.Throwables

import com.ligadata.msgcompiler._
import com.ligadata.Exceptions._

import scala.xml.XML
import org.apache.logging.log4j._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import com.ligadata.ZooKeeper._
import org.apache.curator.framework.CuratorFramework
import org.apache.zookeeper.CreateMode

import com.ligadata.keyvaluestore._
import com.ligadata.Serialize._
import com.ligadata.Utils._
import scala.util.control.Breaks._
import com.ligadata.AuditAdapterInfo._
import com.ligadata.SecurityAdapterInfo.SecurityAdapter
import com.ligadata.keyvaluestore.KeyValueManager
import com.ligadata.Exceptions.StackTrace

import java.util.Date
import org.json4s.jackson.Serialization

// The implementation class
object MessageAndContainerUtils {

  lazy val sysNS = "System"
  // system name space
  lazy val loggerName = this.getClass.getName
  lazy val logger = LogManager.getLogger(loggerName)
  lazy val serializerType = "kryo"
  lazy val serializer = SerializerManager.GetSerializer(serializerType)
  private[this] val lock = new Object
    // 646 - 676 Change begins - replace MetadataAPIImpl
  val getMetadataAPI = MetadataAPIImpl.getMetadataAPI
  // 646 - 676 Change ends

  /**
   * AddContainerDef
   *
   * @param contDef
   * @param recompile
   * @return
   */
  def AddContainerDef(contDef: ContainerDef, recompile: Boolean = false): String = {
    var key = contDef.FullNameWithVer
    val dispkey = contDef.FullName + "." + MdMgr.Pad0s2Version(contDef.Version)
    try {
      getMetadataAPI.AddObjectToCache(contDef, MdMgr.GetMdMgr)
      getMetadataAPI.UploadJarsToDB(contDef)
      var objectsAdded = AddMessageTypes(contDef, MdMgr.GetMdMgr, recompile)
      objectsAdded = contDef +: objectsAdded
      PersistenceUtils.SaveSchemaInformation(contDef.cType.SchemaId, contDef.NameSpace, contDef.Name, contDef.Version, contDef.PhysicalName, contDef.cType.AvroSchema, "Container")
      PersistenceUtils.SaveElementInformation(contDef.MdElementId, "Container", contDef.NameSpace, contDef.Name)
      getMetadataAPI.SaveObjectList(objectsAdded, "containers")
      val operations = for (op <- objectsAdded) yield "Add"
      getMetadataAPI.NotifyEngine(objectsAdded, operations)
      val apiResult = new ApiResult(ErrorCodeConstants.Success, "AddContainerDef", null, ErrorCodeConstants.Add_Container_Successful + ":" + dispkey)
      apiResult.toString()
    } catch {
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddContainerDef", null, "Error :" + e.toString() + ErrorCodeConstants.Add_Container_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * AddMessageDef
   *
   * @param msgDef
   * @param recompile
   * @return
   */
  def AddMessageDef(msgDef: MessageDef, recompile: Boolean = false): String = {
    val dispkey = msgDef.FullName + "." + MdMgr.Pad0s2Version(msgDef.Version)
    try {
      getMetadataAPI.AddObjectToCache(msgDef, MdMgr.GetMdMgr)
      getMetadataAPI.UploadJarsToDB(msgDef)
      var objectsAdded = AddMessageTypes(msgDef, MdMgr.GetMdMgr, recompile)
      objectsAdded = msgDef +: objectsAdded
      PersistenceUtils.SaveSchemaInformation(msgDef.cType.SchemaId, msgDef.NameSpace, msgDef.Name, msgDef.Version, msgDef.PhysicalName, msgDef.cType.AvroSchema, "Message")
      PersistenceUtils.SaveElementInformation(msgDef.MdElementId, "Message", msgDef.NameSpace, msgDef.Name)
      getMetadataAPI.SaveObjectList(objectsAdded, "messages")
      val operations = for (op <- objectsAdded) yield "Add"
      getMetadataAPI.NotifyEngine(objectsAdded, operations)
      val apiResult = new ApiResult(ErrorCodeConstants.Success, "AddMessageDef", null, ErrorCodeConstants.Add_Message_Successful + ":" + dispkey)
      apiResult.toString()
    } catch {
      case e: Exception => {
        logger.error("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddMessageDef", null, "Error :" + e.toString() + ErrorCodeConstants.Add_Message_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * AddMessageTypes
   *
   * @param msgDef
   * @param mdMgr the metadata manager receiver
   * @param recompile
   * @return
   */
  def AddMessageTypes(msgDef: BaseElemDef, mdMgr: MdMgr, recompile: Boolean = false): Array[BaseElemDef] = {
    logger.debug("The class name => " + msgDef.getClass().getName())
    try {
      val tenantId = "" // Use empty for types for now
      var types = new Array[BaseElemDef](0)
      val msgType = getMetadataAPI.getObjectType(msgDef)
      val depJars = if (msgDef.DependencyJarNames != null)
        (msgDef.DependencyJarNames :+ msgDef.JarName)
      else Array(msgDef.JarName)
      msgType match {
        case "MessageDef" | "ContainerDef" => {
          // ArrayOf<TypeName>
          var obj: BaseElemDef = mdMgr.MakeArray(msgDef.nameSpace, "arrayof" + msgDef.name, msgDef.nameSpace, msgDef.name, 1, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */ , msgDef.ver, recompile)
          obj.dependencyJarNames = depJars
          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          types = types :+ obj

          // MapOf<TypeName>
          obj = mdMgr.MakeMap(msgDef.nameSpace, "mapof" + msgDef.name, msgDef.nameSpace, msgDef.name, msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */ , recompile)
          obj.dependencyJarNames = depJars
          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          types = types :+ obj

          // ArrayBufferOf<TypeName>
          //          obj = mdMgr.MakeArrayBuffer(msgDef.nameSpace, "arraybufferof" + msgDef.name, msgDef.nameSpace, msgDef.name, 1, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, msgDef.ver, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // SortedSetOf<TypeName>
          //          obj = mdMgr.MakeSortedSet(msgDef.nameSpace, "sortedsetof" + msgDef.name, msgDef.nameSpace, msgDef.name, msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // ImmutableMapOfIntArrayOf<TypeName>
          //          obj = mdMgr.MakeImmutableMap(msgDef.nameSpace, "immutablemapofintarrayof" + msgDef.name, (sysNS, "Int"), (msgDef.nameSpace, "arrayof" + msgDef.name), msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // ImmutableMapOfString<TypeName>
          //          obj = mdMgr.MakeImmutableMap(msgDef.nameSpace, "immutablemapofstringarrayof" + msgDef.name, (sysNS, "String"), (msgDef.nameSpace, "arrayof" + msgDef.name), msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // ArrayOfArrayOf<TypeName>
          //          obj = mdMgr.MakeArray(msgDef.nameSpace, "arrayofarrayof" + msgDef.name, msgDef.nameSpace, "arrayof" + msgDef.name, 1, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, msgDef.ver, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // MapOfStringArrayOf<TypeName>
          //          obj = mdMgr.MakeMap(msgDef.nameSpace, "mapofstringarrayof" + msgDef.name, (sysNS, "String"), (msgDef.nameSpace, "arrayof" + msgDef.name), msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // MapOfIntArrayOf<TypeName>
          //          obj = mdMgr.MakeMap(msgDef.nameSpace, "mapofintarrayof" + msgDef.name, (sysNS, "Int"), (msgDef.nameSpace, "arrayof" + msgDef.name), msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // SetOf<TypeName>
          //          obj = mdMgr.MakeSet(msgDef.nameSpace, "setof" + msgDef.name, msgDef.nameSpace, msgDef.name, msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          //          // TreeSetOf<TypeName>
          //          obj = mdMgr.MakeTreeSet(msgDef.nameSpace, "treesetof" + msgDef.name, msgDef.nameSpace, msgDef.name, msgDef.ver, msgDef.OwnerId, tenantId, getMetadataAPI.GetUniqueId, 0L /* FIXME:- Not yet handled this */, recompile)
          //          obj.dependencyJarNames = depJars
          //          getMetadataAPI.AddObjectToCache(obj, mdMgr)
          //          types = types :+ obj
          types
        }
        case _ => {
          throw InternalErrorException("Unknown class in AddMessageTypes", null)
        }
      }
    } catch {
      case e: Exception => {
        logger.error("", e)
        throw e
      }
    }
  }

  /**
   * AddContainerOrMessage
   *
   * @param contOrMsgText message
   * @param format        its format
   * @param userid        the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                      method. If Security and/or Audit are configured, this value must be a value other than None.
   * @param recompile     a
   * @return <description please>
   */
  def AddContainerOrMessage(contOrMsgText: String, format: String, userid: Option[String], tenantId: Option[String] = None, pStr : Option[String],  recompile: Boolean = false): String = {
    var resultStr: String = ""

    if (tenantId == None) return (new ApiResult(ErrorCodeConstants.Failure, "AddContainer/AddMessage", null, s"Tenant ID is required to perform an ADD CONTAINER or an ADD MESSAGE operation")).toString

    try {
      var compProxy = new CompilerProxy
      //compProxy.setLoggerLevel(Level.TRACE)
      val (classStrVer, cntOrMsgDef, classStrNoVer) = compProxy.compileMessageDef(false, contOrMsgText, tenantId, recompile)
      if (cntOrMsgDef != null) {
        cntOrMsgDef.ownerId = if (userid == None) "kamanja" else userid.get
        cntOrMsgDef.tenantId = tenantId.get
      }

        cntOrMsgDef.setParamValues(pStr)
        cntOrMsgDef.setCreationTime()
         cntOrMsgDef.setModTime()
      logger.debug("Message/Container Compiler returned an object of type " + cntOrMsgDef.getClass().getName())
      cntOrMsgDef match {
        case msg: MessageDef => {
          getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.INSERTOBJECT, contOrMsgText, AuditConstants.SUCCESS, "", msg.FullNameWithVer)
          // Make sure we are allowed to add this version.
          val latestVersion = GetLatestMessage(msg)
          var isValid = true
          if (latestVersion != None) {
            isValid = IsValidVersion(latestVersion.get, msg)
          }
          if (!isValid) {
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", null, ErrorCodeConstants.Update_Message_Failed + ":" + msg.Name + " Error:Invalid Version")
            apiResult.toString()
          }
          // 1119 Changes begin  - prevents addition of an existing message
          if (DoesAnyMessageExist(msg) == true) {
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddContainerOrMessage", null, ErrorCodeConstants.Add_Message_Failed + ":" + msg.Name + " Already Exists, perform update")
            return apiResult.toString()

          }
          // 1119 Changes end

         if (recompile) {
            // Incase of recompile, Message Compiler is automatically incrementing the previous version
            // by 1. Before Updating the metadata with the new version, remove the old version
            val latestVersion = GetLatestMessage(msg)
            RemoveMessage(latestVersion.get.nameSpace, latestVersion.get.name, latestVersion.get.ver, None)
            resultStr = AddMessageDef(msg, recompile)
          } else {
            resultStr = AddMessageDef(msg, recompile)
          }

          if (recompile) {
            val depModels = GetDependentModels(msg.NameSpace, msg.Name, msg.ver)
            if (depModels.length > 0) {
              depModels.foreach(mod => {
                logger.debug("DependentModel => " + mod.FullNameWithVer)
                resultStr = resultStr + getMetadataAPI.RecompileModel(mod, userid, Some(msg))
              })
            }
          }
          resultStr
        }
        case cont: ContainerDef => {
          getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.INSERTOBJECT, contOrMsgText, AuditConstants.SUCCESS, "", cont.FullNameWithVer)
          // Make sure we are allowed to add this version.
          val latestVersion = GetLatestContainer(cont)
          var isValid = true
          if (latestVersion != None) {
            isValid = IsValidVersion(latestVersion.get, cont)
          }
          if (!isValid) {
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", null, ErrorCodeConstants.Update_Message_Failed + ":" + cont.Name + " Error:Invalid Version")
            apiResult.toString()
          }

          // 1119 Changes begin, prevents addition of existing container with a different version
          if (DoesAnyContainerExist(cont) == true) {
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddMessageOrContainer", null, ErrorCodeConstants.Add_Container_Failed + ":" + cont.Name + " Container exists, perform update")
            return apiResult.toString()
          }

          if (recompile) {
            // Incase of recompile, Message Compiler is automatically incrementing the previous version
            // by 1. Before Updating the metadata with the new version, remove the old version
            val latestVersion = GetLatestContainer(cont)
            RemoveContainer(latestVersion.get.nameSpace, latestVersion.get.name, latestVersion.get.ver, None)
            resultStr = AddContainerDef(cont, recompile)
          } else {
            resultStr = AddContainerDef(cont, recompile)
          }

          if (recompile) {
            val depModels = GetDependentModels(cont.NameSpace, cont.Name, cont.ver)
            if (depModels.length > 0) {
              depModels.foreach(mod => {
                logger.debug("DependentModel => " + mod.FullNameWithVer)
                resultStr = resultStr + getMetadataAPI.RecompileModel(mod, userid, None)
              })
            }
          }
          resultStr
        }
      }
    } catch {
      case e: ModelCompilationFailedException => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddContainerOrMessage", contOrMsgText, "Error: " + e.toString + ErrorCodeConstants.Add_Container_Or_Message_Failed)
        apiResult.toString()
      }
      case e: MsgCompilationFailedException => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddContainerOrMessage", contOrMsgText, "Error: " + e.toString + ErrorCodeConstants.Add_Container_Or_Message_Failed)
        apiResult.toString()
      }
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "AddContainerOrMessage", contOrMsgText, "Error: " + e.toString + ErrorCodeConstants.Add_Container_Or_Message_Failed)
        apiResult.toString()
      }
    }
  }

  def AddContainer(containerText: String, format: String, userid: Option[String], tenantId: Option[String] = None): String = {
    AddContainerOrMessage(containerText, format, userid, tenantId, None)
  }

  /**
   * AddContainer
   *
   * @param containerText
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def AddContainer(containerText: String, userid: Option[String], tenantId: Option[String]): String = {
    AddContainer(containerText, "JSON", userid, tenantId)
  }

  /**
   * RecompileMessage
   *
   * @param msgFullName
   * @return
   */
  def RecompileMessage(msgFullName: String): String = {
    var tenantId: String = ""
    var resultStr: String = ""
    try {
      var messageText: String = null

      val latestMsgDef = MdMgr.GetMdMgr.Message(msgFullName, -1, true)
      if (latestMsgDef == None) {
        val latestContDef = MdMgr.GetMdMgr.Container(msgFullName, -1, true)
        if (latestContDef == None) {
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RecompileMessage", null, ErrorCodeConstants.Recompile_Message_Failed + ":" + msgFullName + " Error:No message or container named ")
          return apiResult.toString()
        } else {
          tenantId = latestMsgDef.get.TenantId
          messageText = latestContDef.get.objectDefinition
        }
      } else {
        tenantId = latestMsgDef.get.TenantId
        messageText = latestMsgDef.get.objectDefinition
      }
      resultStr = AddContainerOrMessage(messageText, "JSON", None, Some(tenantId), None, true)
      resultStr

    } catch {
      case e: MsgCompilationFailedException => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RecompileMessage", null, "Error :" + e.toString() + ErrorCodeConstants.Recompile_Message_Failed + ":" + msgFullName)
        apiResult.toString()
      }
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RecompileMessage", null, "Error :" + e.toString() + ErrorCodeConstants.Recompile_Message_Failed + ":" + msgFullName)
        apiResult.toString()
      }
    }
  }

  /**
   * UpdateMessage
   *
   * @param messageText text of the message (as JSON/XML string as defined by next parameter formatType)
   * @param format
   * @param userid      the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                    method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.statusCode
   *         indicates success or failure of operation: 0 for success, Non-zero for failure. The Value of
   *         ApiResult.statusDescription and ApiResult.resultData indicate the nature of the error in case of failure
   */
  def UpdateMessage(messageText: String, format: String, userid: Option[String] = None, tenantId: Option[String], pStr : Option[String]): String = {
    var resultStr: String = ""
    try {

      if (tenantId == None) return (new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage/UpdateContainer", null, s"Tenant ID is required to perform an UPDATE MESSAGE or an UPDATE CONTAINER operation")).toString

      var compProxy = new CompilerProxy
      //compProxy.setLoggerLevel(Level.TRACE)
      val (classStrVer, msgDef, classStrNoVer) = compProxy.compileMessageDef(true, messageText, tenantId)
      if (msgDef != null) {
        msgDef.ownerId = if (userid == None) "kamanja" else userid.get
        msgDef.tenantId = tenantId.get
      }

      msgDef.setParamValues(pStr)
      msgDef.setModTime()
      val key = msgDef.FullNameWithVer
      msgDef match {
        case msg: MessageDef => {
          getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.UPDATEOBJECT, messageText, AuditConstants.SUCCESS, "", msg.FullNameWithVer)

          /**
           * FIXME: It is incorrect to assume that the latest message is the one being replaced.
           * It is possible that multiple message versions could be present in the system.  UpdateMessage should explicitly
           * receive the version to be replaced.  There could be a convenience method that uses this method for the "latest" case.
           */
          val latestVersion = GetLatestMessage(msg)
          var isValid = true
          if (latestVersion != None) {
            isValid = IsValidVersion(latestVersion.get, msg)
          }
          // 1118 - Changes begin - Message must exist for update to happen
          else {
            return (new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", null, s"Message must exist to update")).toString

          }
          // 1118 - Changes end
          if (isValid) {
            // Check to make sure the TenantId is the same
            if (!tenantId.get.equalsIgnoreCase(latestVersion.get.tenantId)) {
              return (new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", null, s"Tenant ID is different from the one in the existing objects.")).toString
            }

            RemoveMessage(latestVersion.get.nameSpace, latestVersion.get.name, latestVersion.get.ver, None)
            //resultStr = AddMessageDef(msg)
            AddMessageDef(msg)
            logger.debug("Check for dependent messages ...")
            val depMessages = GetDependentMessages.getDependentObjects(msg)
            if (depMessages.length > 0) {
              depMessages.foreach(msg => {
                logger.debug("  DependentMessage => " + msg)
                //resultStr = resultStr + RecompileMessage(msg)
                RecompileMessage(msg)
              })
            }
            val depModels = GetDependentModels(msg.NameSpace, msg.Name, msg.Version.toLong)
            if (depModels.length > 0) {
              depModels.foreach(mod => {
                logger.debug("DependentModel => " + mod.FullNameWithVer)
                //resultStr = resultStr + getMetadataAPI.RecompileModel(mod, userid, Some(msg))
                getMetadataAPI.RecompileModel(mod, userid, Some(msg))
              })
            }
            val resultStr = new ApiResult(ErrorCodeConstants.Success, "UpdateMessage", messageText, ErrorCodeConstants.Update_Message_Successful).toString
            resultStr
          } else {
            // 1112 - Introduced to let user know higher version required - Change begins
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", messageText, ErrorCodeConstants.Update_Message_Failed_Higher_Version_Required)
            // 1112 - Change ends
            apiResult.toString()
          }
        }
        case msg: ContainerDef => {
          getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.UPDATEOBJECT, messageText, AuditConstants.SUCCESS, "", msg.FullNameWithVer)
          val latestVersion = GetLatestContainer(msg)
          var isValid = true
          if (latestVersion != None) {
            isValid = IsValidVersion(latestVersion.get, msg)
          }
          // 1118 - Changes begin - Message must exist for update to happen
          else {
            return (new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", null, s"Container must exist to update")).toString

          }
          // 1118 - Changes end

          if (isValid) {
            // Check to make sure the TenantId is the same
            if (!tenantId.get.equalsIgnoreCase(latestVersion.get.tenantId)) {
              return (new ApiResult(ErrorCodeConstants.Failure, "UpdateContainer", null, s"Tenant ID is different from the one in the existing objects.")).toString
            }

            RemoveContainer(latestVersion.get.nameSpace, latestVersion.get.name, latestVersion.get.ver, None)
            resultStr = AddContainerDef(msg)

            val depMessages = GetDependentMessages.getDependentObjects(msg)
            if (depMessages.length > 0) {
              depMessages.foreach(msg => {
                logger.debug("DependentMessage => " + msg)
                resultStr = resultStr + RecompileMessage(msg)
              })
            }
            val depModels = getMetadataAPI.GetDependentModels(msg.NameSpace, msg.Name, msg.Version.toLong)
            if (depModels.length > 0) {
              depModels.foreach(mod => {
                logger.debug("DependentModel => " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version))
                resultStr = resultStr + getMetadataAPI.RecompileModel(mod, userid, None)
              })
            }
            resultStr
          } else {
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", messageText, ErrorCodeConstants.Update_Message_Failed + " Error:Invalid Version")
            apiResult.toString()
          }
        }
      }
    } catch {
      case e: MsgCompilationFailedException => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", messageText, "Error :" + e.toString() + ErrorCodeConstants.Update_Message_Failed)
        apiResult.toString()
      }
      case e: ObjectNotFoundException => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", messageText, "Error :" + e.toString() + ErrorCodeConstants.Update_Message_Failed)
        apiResult.toString()
      }
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "UpdateMessage", messageText, "Error :" + e.toString() + ErrorCodeConstants.Update_Message_Failed)
        apiResult.toString()
      }
    }
  }

  /**
   * UpdateContainer
   *
   * @param messageText
   * @param format
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.statusCode
   *         indicates success or failure of operation: 0 for success, Non-zero for failure. The Value of
   *         ApiResult.statusDescription and ApiResult.resultData indicate the nature of the error in case of failure
   */
  //def UpdateContainer(messageText: String, format: String, userid: Option[String] = None): String = {
  //  UpdateMessage(messageText, format, userid)
  // }

  /**
   * UpdateContainer
   *
   * @param messageText
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  // def UpdateContainer(messageText: String, userid: Option[String]): String = {
  //   UpdateMessage(messageText, "JSON", userid)
  // }

  /**
   * UpdateMessage
   *
   * @param messageText
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  // def UpdateMessage(messageText: String, userid: Option[String]): String = {
  //   UpdateMessage(messageText, "JSON", userid)
  // }

  /**
   * Remove container with Container Name and Version Number
   *
   * @param nameSpace namespace of the object
   * @param name
   * @param version   Version of the object
   * @param userid    the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                  method. If Security and/or Audit are configured, this value must be a value other than None.
   * @param zkNotify
   * @return
   */
  def RemoveContainer(nameSpace: String, name: String, version: Long, userid: Option[String], zkNotify: Boolean = true): String = {
    var key = nameSpace + "." + name + "." + version
    val dispkey = nameSpace + "." + name + "." + MdMgr.Pad0s2Version(version)
    var newTranId = getMetadataAPI.GetNewTranId
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.DELETEOBJECT, "Container", AuditConstants.SUCCESS, "", key)
    try {
      val o = MdMgr.GetMdMgr.Container(nameSpace.toLowerCase, name.toLowerCase, version, true)
      o match {
        case None =>
          None
          logger.debug("container not found => " + key)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RemoveContainer", null, ErrorCodeConstants.Remove_Container_Failed_Not_Found + ":" + dispkey)
          apiResult.toString()
        case Some(m) =>
          logger.debug("container found => " + m.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[ContainerDef].Version))
          val contDef = m.asInstanceOf[ContainerDef]
          var objectsToBeRemoved = GetAdditionalTypesAdded(contDef, MdMgr.GetMdMgr)
          // Also remove a type with same name as messageDef
          var typeName = name
          var typeDef = TypeUtils.GetType(nameSpace, typeName, version.toString, "JSON", None)
          if (typeDef != None) {
            objectsToBeRemoved = objectsToBeRemoved :+ typeDef.get
          }
          objectsToBeRemoved.foreach(typ => {
            //typ.tranId = newTranId
            TypeUtils.RemoveType(typ.nameSpace, typ.name, typ.ver, None)
          })
          // ContainerDef itself
          contDef.tranId = newTranId
          getMetadataAPI.DeleteObject(contDef)
          var allObjectsArray = objectsToBeRemoved :+ contDef

          val operations = for (op <- allObjectsArray) yield "Remove"
          getMetadataAPI.NotifyEngine(allObjectsArray, operations)

          val apiResult = new ApiResult(ErrorCodeConstants.Success, "RemoveContainer", null, ErrorCodeConstants.Remove_Container_Successful + ":" + dispkey)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RemoveContainer", null, "Error :" + e.toString() + ErrorCodeConstants.Remove_Container_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * Remove message with Message Name and Version Number
   *
   * @param nameSpace namespace of the object
   * @param name
   * @param version   Version of the object
   * @param userid    the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                  method. If Security and/or Audit are configured, this value must be a value other than None.
   * @param zkNotify
   * @return
   */
  def RemoveMessage(nameSpace: String, name: String, version: Long, userid: Option[String], zkNotify: Boolean = true): String = {
    var key = nameSpace + "." + name + "." + version
    val dispkey = nameSpace + "." + name + "." + MdMgr.Pad0s2Version(version)
    var newTranId = getMetadataAPI.GetNewTranId
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.WRITE), AuditConstants.DELETEOBJECT, AuditConstants.MESSAGE, AuditConstants.SUCCESS, "", key)
    try {
      val o = MdMgr.GetMdMgr.Message(nameSpace.toLowerCase, name.toLowerCase, version, true)
      o match {
        case None =>
          None
          logger.debug("Message not found => " + key)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RemoveMessage", null, ErrorCodeConstants.Remove_Message_Failed_Not_Found + ":" + dispkey)
          apiResult.toString()
        case Some(m) =>
          val msgDef = m.asInstanceOf[MessageDef]
          logger.debug("message found => " + msgDef.FullName + "." + MdMgr.Pad0s2Version(msgDef.Version))
          var objectsToBeRemoved = GetAdditionalTypesAdded(msgDef, MdMgr.GetMdMgr)

          // Also remove a type with same name as messageDef
          var typeName = name
          var typeDef = TypeUtils.GetType(nameSpace, typeName, version.toString, "JSON", None)

          if (typeDef != None) {
            objectsToBeRemoved = objectsToBeRemoved :+ typeDef.get
          }

          objectsToBeRemoved.foreach(typ => {
            //typ.tranId = newTranId
            TypeUtils.RemoveType(typ.nameSpace, typ.name, typ.ver, None)
          })

          // MessageDef itself - add it to the list of other objects to be passed to the zookeeper
          // to notify other instnances
          msgDef.tranId = newTranId
          getMetadataAPI.DeleteObject(msgDef)
          var allObjectsArray = objectsToBeRemoved :+ msgDef

          val operations = for (op <- allObjectsArray) yield "Remove"
          getMetadataAPI.NotifyEngine(allObjectsArray, operations)

          val apiResult = new ApiResult(ErrorCodeConstants.Success, "RemoveMessage", null, ErrorCodeConstants.Remove_Message_Successful + ":" + dispkey)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "RemoveMessage", null, "Error :" + e.toString() + ErrorCodeConstants.Remove_Message_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * When a message or container is compiled, the getMetadataAPI will automatically catalog an array, array buffer,
   * sorted set, immutable map of int array, array of array, et al where the message or container is a member element.
   * The type names are of the form <collectiontype>of<message type>.  Currently these container names are created:
   *
   * {{{
   *       arrayof<message type>
   *       arraybufferof<message type>
   *       sortedsetof<message type>
   *       immutablemapofintarrayof<message type>
   *       immutablemapofstringarrayof<message type>
   *       arrayofarrayof<message type>
   *       mapofstringarrayof<message type>
   *       mapofintarrayof<message type>
   *       setof<message type>
   *       treesetof<message type>
   * }}}
   *
   * @param msgDef the name of the msgDef's type is used for the type name formation
   * @param mdMgr  the metadata manager receiver
   * @return <description please>
   */
  def GetAdditionalTypesAdded(msgDef: BaseElemDef, mdMgr: MdMgr): Array[BaseElemDef] = {
    var types = new Array[BaseElemDef](0)
    logger.debug("The class name => " + msgDef.getClass().getName())
    try {
      val msgType = getMetadataAPI.getObjectType(msgDef)
      msgType match {
        case "MessageDef" | "ContainerDef" => {
          // ArrayOf<TypeName>
          var typeName = "arrayof" + msgDef.name
          var typeDef = TypeUtils.GetType(msgDef.nameSpace, typeName, msgDef.ver.toString, "JSON", None)
          if (typeDef != None) {
            types = types :+ typeDef.get
          }
          // MapOf<TypeName>
          typeName = "mapof" + msgDef.name
          typeDef = TypeUtils.GetType(msgDef.nameSpace, typeName, msgDef.ver.toString, "JSON", None)
          if (typeDef != None) {
            types = types :+ typeDef.get
          }
          logger.debug("Type objects to be removed = " + types.length)
          types
        }
        case _ => {
          throw InternalErrorException("Unknown class in AddMessageTypes", null)
        }
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw e
      }
    }
  }

  /**
   * Remove message with Message Name and Version Number based upon advice in supplied notification
   *
   * @param zkMessage
   * @return
   */
  def RemoveMessageFromCache(zkMessage: ZooKeeperNotification) = {
    try {
      var key = zkMessage.NameSpace + "." + zkMessage.Name + "." + zkMessage.Version
      val dispkey = zkMessage.NameSpace + "." + zkMessage.Name + "." + MdMgr.Pad0s2Version(zkMessage.Version.toLong)
      val o = MdMgr.GetMdMgr.Message(zkMessage.NameSpace, zkMessage.Name, zkMessage.Version.toLong, true)
      o match {
        case None =>
          None
          logger.debug("Message not found, Already Removed? => " + dispkey)
        case Some(m) =>
          val msgDef = m.asInstanceOf[MessageDef]
          logger.debug("message found => " + msgDef.FullName + "." + MdMgr.Pad0s2Version(msgDef.Version))
          val types = GetAdditionalTypesAdded(msgDef, MdMgr.GetMdMgr)

          var typeName = zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "arrayof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "sortedsetof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "arraybufferof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          MdMgr.GetMdMgr.RemoveMessage(zkMessage.NameSpace, zkMessage.Name, zkMessage.Version.toLong)
      }
    } catch {
      case e: Exception => {
        logger.error("Failed to delete the Message from cache:" + e.toString, e)
      }
    }
  }

  /**
   * RemoveContainerFromCache
   *
   * @param zkMessage
   * @return
   */
  def RemoveContainerFromCache(zkMessage: ZooKeeperNotification) = {
    try {
      var key = zkMessage.NameSpace + "." + zkMessage.Name + "." + zkMessage.Version
      val dispkey = zkMessage.NameSpace + "." + zkMessage.Name + "." + MdMgr.Pad0s2Version(zkMessage.Version.toLong)
      val o = MdMgr.GetMdMgr.Container(zkMessage.NameSpace, zkMessage.Name, zkMessage.Version.toLong, true)
      o match {
        case None =>
          None
          logger.debug("Message not found, Already Removed? => " + dispkey)
        case Some(m) =>
          val msgDef = m.asInstanceOf[MessageDef]
          logger.debug("message found => " + msgDef.FullName + "." + MdMgr.Pad0s2Version(msgDef.Version))
          var typeName = zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "arrayof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "sortedsetof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          typeName = "arraybufferof" + zkMessage.Name
          MdMgr.GetMdMgr.RemoveType(zkMessage.NameSpace, typeName, zkMessage.Version.toLong)
          MdMgr.GetMdMgr.RemoveContainer(zkMessage.NameSpace, zkMessage.Name, zkMessage.Version.toLong)
      }
    } catch {
      case e: Exception => {
        logger.error("Failed to delete the Message from cache:" + e.toString, e)
      }
    }
  }

  /**
   * Remove message with Message Name and Version Number
   *
   * @param messageName Name of the given message
   * @param version     Version of the given message
   * @param userid      the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                    method. If Security and/or Audit are configured, this value should be other than None
   * @return the result as a JSON String of object ApiResult where ApiResult.statusCode
   *         indicates success or failure of operation: 0 for success, Non-zero for failure. The Value of
   *         ApiResult.statusDescription and ApiResult.resultData indicate the nature of the error in case of failure
   */
  def RemoveMessage(messageName: String, version: Long, userid: Option[String]): String = {
    RemoveMessage(sysNS, messageName, version, userid)
  }

  /**
   * Remove container with Container Name and Version Number
   *
   * @param containerName Name of the given container
   * @param version       Version of the object   Version of the given container
   * @param userid        the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                      method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.statusCode
   *         indicates success or failure of operation: 0 for success, Non-zero for failure. The Value of
   *         ApiResult.statusDescription and ApiResult.resultData indicate the nature of the error in case of failure
   */
  def RemoveContainer(containerName: String, version: Long, userid: Option[String]): String = {
    RemoveContainer(sysNS, containerName, version, userid)
  }

  /**
   * getBaseType
   *
   * @param typ a type to be determined
   * @return
   */
  private def getBaseType(typ: BaseTypeDef): BaseTypeDef = {
    // Just return the "typ" if "typ" is not supported yet
    if (typ.tType == tMap) {
      val typ1 = typ.asInstanceOf[MapTypeDef].valDef
      return getBaseType(typ1)
    }
    //    if (typ.tType == tHashMap) {
    //      logger.debug("HashMapTypeDef is not yet handled")
    //      return typ
    //    }
    //    if (typ.tType == tSet) {
    //      val typ1 = typ.asInstanceOf[SetTypeDef].keyDef
    //      return getBaseType(typ1)
    //    }
    //    if (typ.tType == tTreeSet) {
    //      val typ1 = typ.asInstanceOf[TreeSetTypeDef].keyDef
    //      return getBaseType(typ1)
    //    }
    //    if (typ.tType == tSortedSet) {
    //      val typ1 = typ.asInstanceOf[SortedSetTypeDef].keyDef
    //      return getBaseType(typ1)
    //    }
    //    if (typ.tType == tList) {
    //      val typ1 = typ.asInstanceOf[ListTypeDef].valDef
    //      return getBaseType(typ1)
    //    }
    //    if (typ.tType == tQueue) {
    //      val typ1 = typ.asInstanceOf[QueueTypeDef].valDef
    //      return getBaseType(typ1)
    //    }
    if (typ.tType == tArray) {
      val typ1 = typ.asInstanceOf[ArrayTypeDef].elemDef
      return getBaseType(typ1)
    }
    //    if (typ.tType == tArrayBuf) {
    //      val typ1 = typ.asInstanceOf[ArrayBufTypeDef].elemDef
    //      return getBaseType(typ1)
    //    }
    return typ
  }

  /**
   * GetDependentModels
   *
   * @param msgNameSpace
   * @param msgName
   * @param msgVer
   * @return
   */
  def GetDependentModels(msgNameSpace: String, msgName: String, msgVer: Long): Array[ModelDef] = {
    try {
      val msgObj = Array(msgNameSpace, msgName, msgVer).mkString(".").toLowerCase
      val msgObjName = (msgNameSpace + "." + msgName).toLowerCase
      val modDefs = MdMgr.GetMdMgr.Models(true, true)
      var depModels: scala.collection.mutable.Set[ModelDef] = scala.collection.mutable.Set[ModelDef]()
      modDefs match {
        case None =>
          logger.debug("No Models found ")
        case Some(ms) =>
          val msa = ms.toArray
          msa.foreach(mod => {
            logger.debug("Checking model " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version))
            mod.inputMsgSets.foreach(set => {
              set.foreach(msgInfo => {
                if (msgInfo != null && msgInfo.message != null && msgInfo.message.trim.nonEmpty &&
                    msgInfo.message.toLowerCase == msgObjName) {
                    logger.warn("The model " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version) + " is  dependent on the message " + msgInfo.message)
                    depModels.add(mod)
                }
              })
            })

            mod.outputMsgs.foreach( message => {
              if (message != null && message.toLowerCase() == msgObjName) {
                logger.warn("The model " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version) + " is  dependent on the message " + message)
                depModels.add(mod)
              }
            })
	    // use ModeDef.depContainers to determine the model dependency on containers
	    mod.depContainers.foreach(dc => {
	      if (dc.toLowerCase() == msgObjName){
		logger.warn("The model " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version) + " is  dependent on the container " + dc)
		depModels.add(mod)
	      }
	    })

	    // use model.modelConfig to determine the model dependency on messages
	    var messages = getMessagesFromModelConfig(mod.modelConfig)
	    messages.foreach(m => {
	      if (m.toLowerCase() == msgObjName){
		logger.warn("The model " + mod.FullName + "." + MdMgr.Pad0s2Version(mod.Version) + " is  dependent on the message " + m)
		depModels.add(mod)
	      }
	    })
          })
      }
      logger.debug("Found " + depModels.size + " dependent models ")
      depModels.toArray
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw InternalErrorException("Unable to find dependent models " + e.getMessage(), e)
      }
    }
  }

  /**
   * GetAllMessageDefs - get all available messages(format JSON or XML) as a String
   *
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def GetAllMessageDefs(formatType: String, userid: Option[String] = None): String = {
    try {
      val msgDefs = MdMgr.GetMdMgr.Messages(true, true)
      msgDefs match {
        case None =>
          None
          logger.debug("No Messages found ")
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetAllMessageDefs", null, ErrorCodeConstants.Get_All_Messages_Failed_Not_Available)
          apiResult.toString()
        case Some(ms) =>
          val msa = ms.toArray
          val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetAllMessageDefs", JsonSerializer.SerializeObjectListToJson("Messages", msa), ErrorCodeConstants.Get_All_Messages_Succesful)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetAllMessageDefs", null, "Error :" + e.toString() + ErrorCodeConstants.Get_All_Messages_Failed)
        apiResult.toString()
      }
    }
  }

  // All available containers(format JSON or XML) as a String
  /**
   * GetAllContainerDefs
   *
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return result as string
   */
  def GetAllContainerDefs(formatType: String, userid: Option[String] = None): String = {
    try {
      val msgDefs = MdMgr.GetMdMgr.Containers(true, true)
      msgDefs match {
        case None =>
          None
          logger.debug("No Containers found ")
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetAllContainerDefs", null, ErrorCodeConstants.Get_All_Containers_Failed_Not_Available)
          apiResult.toString()
        case Some(ms) =>
          val msa = ms.toArray
          val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetAllContainerDefs", JsonSerializer.SerializeObjectListToJson("Containers", msa), ErrorCodeConstants.Get_All_Containers_Successful)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetAllContainerDefs", null, "Error :" + e.toString() + ErrorCodeConstants.Get_All_Containers_Failed)
        apiResult.toString()
      }
    }
  }

  /**
   * GetAllMessagesFromCache
   *
   * @param active
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  // 646 - 672 Changes begin - filter by tenantId
  def GetAllMessagesFromCache(active: Boolean, userid: Option[String] = None, tid : Option[String] = None): Array[String] = {
    var messageList: Array[String] = new Array[String](0)
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.READ), AuditConstants.GETKEYS, AuditConstants.MESSAGE, AuditConstants.SUCCESS, "", AuditConstants.MESSAGE)
    try {
      val msgDefs = MdMgr.GetMdMgr.Messages(active, true)
      msgDefs match {
        case None =>
          None
          logger.debug("No Messages found ")
          messageList
        case Some(ms) =>
          val msa = ms.toArray
          val msgCount = msa.length
          var newMessageList : List[String] = List[String]() ;
             for (i <- 0 to msgCount - 1) {
            if (tid.isEmpty || (tid.get == msa(i).tenantId)) {
                 newMessageList = newMessageList ::: List(msa(i).FullName + "." + MdMgr.Pad0s2Version(msa(i).Version))

            }
          }
          if (newMessageList.isEmpty) {
            messageList
          }
          else {
            (newMessageList map(_.toString)).toArray
          }
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException("Failed to fetch all the messages:" + e.toString, e)
      }
    }
  }
// 646 - 672 Changes end
  /**
   * GetAllContainersFromCache
   *
   * @param active
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  // 646 - 672 Changes begin - filter by tenantId
  def GetAllContainersFromCache(active: Boolean, userid: Option[String] = None, tid: Option[String] = None): Array[String] = {
    var containerList: Array[String] = new Array[String](0)
    var newContainerList : List[String] = List[String]() ;
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.READ), AuditConstants.GETKEYS, AuditConstants.CONTAINER, AuditConstants.SUCCESS, "", AuditConstants.CONTAINER)
    try {
      val contDefs = MdMgr.GetMdMgr.Containers(active, true)
      contDefs match {
        case None =>
          None
          logger.debug("No Containers found ")
          containerList
        case Some(ms) =>
          val msa = ms.toArray
          val contCount = msa.length
    //      containerList = new Array[String](contCount)
          for (i <- 0 to contCount - 1) {
            if (tid.isEmpty || tid.get == msa(i).tenantId) {

              //containerList(i) = msa(i).FullName + "." + MdMgr.Pad0s2Version(msa(i).Version)
              newContainerList = newContainerList ::: List(msa(i).FullName + "." + MdMgr.Pad0s2Version(msa(i).Version))
            }
          }
          if (newContainerList.isEmpty) {
            containerList
          }
          else {
            (newContainerList map(_.toString)).toArray
          }
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException("Failed to fetch all the containers:" + e.toString, e)
      }
    }
  }
// 646 - 672 Changes end
  /**
   * Get the specific message (format JSON or XML) as a String using messageName(with version) as the key
   *
   * @param nameSpace  namespace of the object
   * @param name
   * @param formatType format of the return value, either JSON or XML
   * @param version    Version of the object
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def GetMessageDefFromCache(nameSpace: String, name: String, formatType: String, version: String, userid: Option[String] = None, tid : Option[String] = None): String = {
    val dispkey = nameSpace + "." + name + "." + MdMgr.Pad0s2Version(version.toLong)
    var key = nameSpace + "." + name + "." + version.toLong
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.GETOBJECT), AuditConstants.GETOBJECT, AuditConstants.MESSAGE, AuditConstants.SUCCESS, "", dispkey)
    try {
      val o = MdMgr.GetMdMgr.Message(nameSpace.toLowerCase, name.toLowerCase, version.toLong, true)
      o match {
        case None =>
          logger.debug("message not found => " + dispkey)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetMessageDefFromCache", null, ErrorCodeConstants.Get_Message_From_Cache_Failed + ":" + dispkey)
          apiResult.toString()
        case Some(m) =>
          if (tid == None || tid.get == m.tenantId) {
            logger.debug("message found => " + m.asInstanceOf[MessageDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[MessageDef].Version))
            val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetMessageDefFromCache", JsonSerializer.SerializeObjectToJson(m), ErrorCodeConstants.Get_Message_From_Cache_Successful)
            apiResult.toString()
          }
          else {
              logger.debug("message not found => " + dispkey + " for tenantId " + tid)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetMessageDefFromCache", null, ErrorCodeConstants.Get_Message_From_Cache_Failed + ":" + dispkey + ", ")
          apiResult.toString()
          }
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetMessageDefFromCache", null, "Error :" + e.toString() + ErrorCodeConstants.Get_Message_From_Cache_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * Get the specific container (format JSON or XML) as a String using containerName(with version) as the key
   *
   * @param nameSpace  namespace of the object
   * @param name
   * @param formatType format of the return value, either JSON or XML
   * @param version    Version of the object
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def GetContainerDefFromCache(nameSpace: String, name: String, formatType: String, version: String, userid: Option[String], tid: Option[String]): String = {
    var key = nameSpace + "." + name + "." + version.toLong
    val dispkey = nameSpace + "." + name + "." + MdMgr.Pad0s2Version(version.toLong)
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.GETOBJECT), AuditConstants.GETOBJECT, AuditConstants.CONTAINER, AuditConstants.SUCCESS, "", dispkey)
    try {
      val o = MdMgr.GetMdMgr.Container(nameSpace.toLowerCase, name.toLowerCase, version.toLong, true)
      o match {
        case None =>
          None
          logger.debug("container not found => " + dispkey)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetContainerDefFromCache", null, ErrorCodeConstants.Get_Container_From_Cache_Failed + ":" + dispkey)
          apiResult.toString()
        case Some(m) =>
          if (tid == None || tid.get == m.tenantId) {
          logger.debug("container found => " + m.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[ContainerDef].Version))
            val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetContainerDefFromCache", JsonSerializer.SerializeObjectToJson(m), ErrorCodeConstants.Get_Container_From_Cache_Successful)
            apiResult.toString()
          }
          else {
            logger.debug("container not found => " + dispkey)
            val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetContainerDefFromCache", null, ErrorCodeConstants.Get_Container_From_Cache_Failed + ":" + dispkey)
            apiResult.toString()
          }
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetContainerDefFromCache", null, "Error :" + e.toString() + ErrorCodeConstants.Get_Container_From_Cache_Failed + ":" + dispkey)
        apiResult.toString()
      }
    }
  }

  /**
   * Return Specific messageDef object using messageName(with version) as the key
   *
   * @param nameSpace  namespace of the object
   * @param name
   * @param formatType format of the return value, either JSON or XML
   * @param version    Version of the object
   * @return
   */
  @throws(classOf[ObjectNotFoundException])
  def GetMessageDefInstanceFromCache(nameSpace: String, name: String, formatType: String, version: String): MessageDef = {
    var key = nameSpace + "." + name + "." + version.toLong
    val dispkey = nameSpace + "." + name + "." + MdMgr.Pad0s2Version(version.toLong)
    try {
      val o = MdMgr.GetMdMgr.Message(nameSpace.toLowerCase, name.toLowerCase, version.toLong, true)
      o match {
        case None =>
          None
          logger.debug("message not found => " + dispkey)
          throw ObjectNotFoundException("Failed to Fetch the message:" + dispkey, null)
        case Some(m) =>
          m.asInstanceOf[MessageDef]
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw ObjectNotFoundException("Failed to Fetch the message:" + dispkey + ":" + e.getMessage(), e)
      }
    }
  }

  // Get the latest message for a given FullName
  /**
   *
   * @param msgDef
   * @return
   */
  def GetLatestMessage(msgDef: MessageDef): Option[MessageDef] = {
    try {
      var key = msgDef.nameSpace + "." + msgDef.name + "." + msgDef.ver
      val dispkey = msgDef.nameSpace + "." + msgDef.name + "." + MdMgr.Pad0s2Version(msgDef.ver)
      val o = MdMgr.GetMdMgr.Messages(msgDef.nameSpace.toLowerCase,
        msgDef.name.toLowerCase,
        false,
        true)
      o match {
        case None =>
          None
          logger.debug("message not in the cache => " + dispkey)
          None
        case Some(m) =>
          // We can get called from the Add Message path, and M could be empty.
          if (m.size == 0) return None
          logger.debug("message found => " + m.head.asInstanceOf[MessageDef].FullName + "." + MdMgr.Pad0s2Version(m.head.asInstanceOf[MessageDef].ver))
          Some(m.head.asInstanceOf[MessageDef])
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  /**
   * Get the latest container for a given FullName
   *
   * @param contDef
   * @return
   */
  def GetLatestContainer(contDef: ContainerDef): Option[ContainerDef] = {
    try {
      var key = contDef.nameSpace + "." + contDef.name + "." + contDef.ver
      val dispkey = contDef.nameSpace + "." + contDef.name + "." + MdMgr.Pad0s2Version(contDef.ver)
      val o = MdMgr.GetMdMgr.Containers(contDef.nameSpace.toLowerCase,
        contDef.name.toLowerCase,
        false,
        true)
      o match {
        case None =>
          None
          logger.debug("container not in the cache => " + dispkey)
          None
        case Some(m) =>
          // We can get called from the Add Container path, and M could be empty.
          if (m.size == 0) return None
          logger.debug("container found => " + m.head.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.head.asInstanceOf[ContainerDef].ver))
          Some(m.head.asInstanceOf[ContainerDef])
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  /**
   * IsValidVersion
   *
   * @param oldObj
   * @param newObj
   * @return
   */
  def IsValidVersion(oldObj: BaseElemDef, newObj: BaseElemDef): Boolean = {
    if (newObj.ver > oldObj.ver) {
      return true
    } else {
      return false
    }
  }

  /**
   * Check whether message already exists in metadata manager. Ideally,
   * we should never add the message into metadata manager more than once
   * and there is no need to use this function in main code flow
   * This is just a utility function being during these initial phases
   *
   * @param msgDef
   * @return
   */
  def DoesMessageAlreadyExist(msgDef: MessageDef): Boolean = {
    IsMessageAlreadyExists(msgDef)
  }

  /**
   * Check whether message already exists in metadata manager. Ideally,
   * we should never add the message into metadata manager more than once
   * and there is no need to use this function in main code flow
   * This is just a utility function being during these initial phases
   *
   * @param msgDef
   * @return
   */
  def IsMessageAlreadyExists(msgDef: MessageDef): Boolean = {
    try {
      var key = msgDef.nameSpace + "." + msgDef.name + "." + msgDef.ver
      val dispkey = msgDef.nameSpace + "." + msgDef.name + "." + MdMgr.Pad0s2Version(msgDef.ver)
      val o = MdMgr.GetMdMgr.Message(msgDef.nameSpace.toLowerCase,
        msgDef.name.toLowerCase,
        msgDef.ver,
        false)
      o match {
        case None =>
          None
          logger.debug("message not in the cache => " + key)
          return false;
        case Some(m) =>
          logger.debug("message found => " + m.asInstanceOf[MessageDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[MessageDef].ver))
          return true
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  // 1119 Changes begin
  /**
    * Check whether any message already exists in metadata manager. Ideally,
    * we should never add the message into metadata manager more than once
    * and there is no need to use this function in main code flow
    * This is just a utility function being during these initial phases
    *
    * @param msgDef
    * @return
    */
  def DoesAnyMessageExist(msgDef: MessageDef): Boolean = {
    try {
      var key = msgDef.nameSpace + "." + msgDef.name + "." + msgDef.ver
      val o = MdMgr.GetMdMgr.Message(msgDef.nameSpace.toLowerCase,
        msgDef.name.toLowerCase,
        0,
        false)
      o match {
        case None =>
          logger.debug("message not in the cache => " + key)
          return false;
        case Some(m) =>
          logger.debug("message found => " + m.asInstanceOf[MessageDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[MessageDef].ver))
          return true
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  // 1119 Changes end

  /**
      * Answer if the supplied MessageDef contains is a MappedMsgTypeDef.
      *
      * @param aType a MessageDef
      * @return true if a MappedMsgTypeDef
      */
    def IsMappedMessage(msg : MessageDef) : Boolean = {
        msg.containerType.isInstanceOf[MappedMsgTypeDef]
    }

  /**
   * Check whether message already exists in metadata manager. Ideally,
    * we should never add the message into metadata manager more than once
    * and there is no need to use this function in main code flow
    * This is just a utility function being during these initial phases
    *
    * @param objectName
    * @return MessageDef
    */

  def IsMessageExists(objectName: String): MessageDef = {
    try {
      val nameNodes: Array[String] = if (objectName != null &&
        objectName.contains('.')) objectName.split('.')
      else Array(MdMgr.sysNS, objectName)
      var name = nameNodes(nameNodes.size - 1)
      val nmspcNodes: Array[String] = nameNodes.splitAt(nameNodes.size - 1)._1
      val buffer: StringBuilder = new StringBuilder
      val nameSpace: String = nmspcNodes.addString(buffer, ".").toString

      val o = MdMgr.GetMdMgr.Message(nameSpace.toLowerCase,
        name.toLowerCase,
        -1,
        false)
      o match {
        case None =>
          None
          logger.debug("message not in the cache => " + objectName)
          return null;
        case Some(m) =>
          logger.debug("message found => " + m.asInstanceOf[MessageDef].FullName + "." +
            MdMgr.Pad0s2Version(m.asInstanceOf[MessageDef].ver))
          return m.asInstanceOf[MessageDef]
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  def createDefaultOutputMessage(modDef: ModelDef, optUserId: Option[String]): String = {
    try {
      logger.info("Creating a default output message for the model " + modDef.FullNameWithVer + " if it doesn't already exist ")
      val nameSpace = modDef.NameSpace
      val name = modDef.Name

      val msgName = name + "_outputmsg"
      val msgFullName = nameSpace + "." + msgName

      if (IsMessageExists(msgFullName) != null) {
        logger.info("The message " + msgFullName + " already exist, not recreating it...")
        return msgFullName
      } else {
        var msgJson = "{\"Message\":{" +
          "\"NameSpace\":" + "\"" + nameSpace + "\"" +
          ",\"Name\":" + "\"" + msgName + "\"" +
          ",\"Version\":\"00.00.01\"" +
          ",\"Description\":\"Default Output Message for " + name + "\"" +
          ",\"Fixed\":\"false\"" +
          "}}"
        logger.info("The default output message string => " + msgJson)
        val resultStr = AddContainerOrMessage(msgJson, "JSON", optUserId, Some(modDef.TenantId), None, false)
        return msgFullName
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw e
      }
    }
  }

  /**
   * IsContainer
   *
   * @param contName
   * @return
   */

  def IsContainer(contName: String): Boolean = {
    try {
      var(nameSpace,name) = MdMgr.SplitFullName(contName)
      val dispkey = nameSpace + "." + name
      val o = MdMgr.GetMdMgr.Container(nameSpace.toLowerCase,name.toLowerCase,-1,false)
      o match {
        case None =>
          logger.debug("container not in the cache => " + dispkey)
          return false;
        case Some(m) =>
          logger.debug("container found => " + m.asInstanceOf[ContainerDef].FullName);
          return true
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  def getMessagesAndContainers(cfgmap: Map[String, Any]): List[String] = {
    logger.debug("getMessagesAndContainers: cfgmap => " + cfgmap)
    val typDeps1 = cfgmap.getOrElse(ModelCompilationConstants.TYPES_DEPENDENCIES, null)
    val typDeps =
      if (typDeps1 == null && cfgmap.size == 1 && cfgmap.head._2.isInstanceOf[scala.collection.immutable.Map[String, Any]]) {
        // If we have one that may be top level map. Trying to take lower level
        cfgmap.head._2.asInstanceOf[scala.collection.immutable.Map[String, Any]].getOrElse(ModelCompilationConstants.TYPES_DEPENDENCIES, null)
      } else {
        typDeps1
      }
    logger.debug("getMessagesAndContainers: typDeps => " + typDeps)

    if( typDeps == null ){
      logger.debug("Types in modelConfig object are not defined")
      List[String]()
    }
    else{
      typDeps.asInstanceOf[List[String]]
    }
  }

  def getContainersFromModelConfig(cfgmap: Map[String,Any]): Array[String] = {
    var containerList = List[String]()
    var msgsAndContainers = getMessagesAndContainers(cfgmap)
    if( msgsAndContainers.length > 0 ){
      msgsAndContainers.foreach(msg => {
	logger.debug("checking the message " + msg)
	if( MessageAndContainerUtils.IsContainer(msg) ){
	  logger.debug("The " + msg + " is a container")
	  containerList = msg :: containerList
	}
	else{
	  logger.debug("The " + msg + " is not a container")
	}
      })
    }
    else{
      logger.debug("getMessagesAndContainers: No types for the model config " + cfgmap)
    }
    containerList.toArray
  }

  def getContainersFromModelConfig(modCfgJson: String): Array[String] = {
    logger.debug("Parsing ModelConfig : " + modCfgJson)
    var cfgmap = parse(modCfgJson).values.asInstanceOf[Map[String, Any]]
    logger.debug("Count of objects in cfgmap : " + cfgmap.keys.size)
    var depContainers = List[String]()
    var containers = getContainersFromModelConfig(cfgmap)
    logger.debug("containers => " + containers)
    depContainers = depContainers ::: containers.toList
    logger.debug("depContainers => " + depContainers)
    depContainers.toArray
  }

  def IsMessage(msgName: String): Boolean = {
    try {
      var(nameSpace,name) = MdMgr.SplitFullName(msgName)
      val dispkey = nameSpace + "." + name
      val o = MdMgr.GetMdMgr.Message(nameSpace.toLowerCase,name.toLowerCase,-1,false)
      o match {
        case None =>
          logger.debug("message not in the cache => " + dispkey)
          return false;
        case Some(m) =>
          logger.debug("message found => " + m.asInstanceOf[MessageDef].FullName);
          return true
      }
    } catch {
      case e: Exception => {
        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  def getMessagesFromModelConfig(cfgmap: Map[String,Any]): Array[String] = {
    var messageList = List[String]()
    var msgsAndContainers = getMessagesAndContainers(cfgmap)
    if( msgsAndContainers.length > 0 ){
      msgsAndContainers.foreach(msg => {
	logger.debug("checking the message " + msg)
	if( MessageAndContainerUtils.IsMessage(msg) ){
	  logger.debug("The " + msg + " is a message")
	  messageList = msg :: messageList
	}
	else{
	  logger.debug("The " + msg + " is not a message")
	}
      })
    }
    else{
      logger.debug("getMessagesAndContainers: No types for the model config " + cfgmap)
    }
    messageList.toArray
  }

  def getMessagesFromModelConfig(modCfgJson: String): Array[String] = {
    logger.debug("Parsing ModelConfig : " + modCfgJson)
    var cfgmap = parse(modCfgJson).values.asInstanceOf[Map[String, Any]]
    logger.debug("Count of objects in cfgmap : " + cfgmap.keys.size)
    var depMessages = List[String]()
    var messages = getMessagesFromModelConfig(cfgmap)
    logger.debug("messages => " + messages)
    depMessages = depMessages ::: messages.toList
    logger.debug("depMessages => " + depMessages)
    depMessages.toArray
  }

  def getContainersFromModelConfig(userid: Option[String], cfgName: String): Array[String] = {
    var containerList = List[String]()
    var msgsAndContainers = getModelMessagesContainers(cfgName,userid)
    if( msgsAndContainers.length > 0 ){
      msgsAndContainers.foreach(msg => {
	logger.debug("processing the message " + msg)
	if( MessageAndContainerUtils.IsContainer(msg) ){
	  logger.debug("The " + msg + " is a container")
	  containerList = msg :: containerList
	}
	else{
	  logger.debug("The " + msg + " is not a container")
	}
      })
    }
    else{
      logger.debug("MetadataAPIImpl.getModelMessagesContainers: No types for the model config " + cfgName)
    }
    containerList.toArray
  }


  /**
   * DoesContainerAlreadyExist
   *
   * @param contDef
   * @return
   */
  def DoesContainerAlreadyExist(contDef: ContainerDef): Boolean = {
    IsContainerAlreadyExists(contDef)
  }

  /**
   * IsContainerAlreadyExists
   *
   * @param contDef
   * @return
   */
  def IsContainerAlreadyExists(contDef: ContainerDef): Boolean = {
    try {
      var key = contDef.nameSpace + "." + contDef.name + "." + contDef.ver
      val dispkey = contDef.nameSpace + "." + contDef.name + "." + MdMgr.Pad0s2Version(contDef.ver)
      val o = MdMgr.GetMdMgr.Container(contDef.nameSpace.toLowerCase,
        contDef.name.toLowerCase,
        contDef.ver,
        false)
      o match {
        case None =>
          None
          logger.debug("container not in the cache => " + dispkey)
          return false;
        case Some(m) =>
          logger.debug("container found => " + m.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[ContainerDef].ver))
          return true
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  // 1119 Changes begin - checks for any existence of container
  /**
    * DoesAnyContainerExist
    *
    * @param contDef
    * @return
    */
  def DoesAnyContainerExist(contDef: ContainerDef): Boolean = {
    try {
      val dispkey = contDef.nameSpace + "." + contDef.name + "." + MdMgr.Pad0s2Version(contDef.ver)
      val o = MdMgr.GetMdMgr.Container(contDef.nameSpace.toLowerCase,
        contDef.name.toLowerCase,
        0,
        false)
      o match {
        case None =>
          logger.debug("container not in the cache => " + dispkey)
          return false;
        case Some(m) =>
          logger.debug("container found => " + m.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[ContainerDef].ver))
          return true
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        throw UnexpectedMetadataAPIException(e.getMessage(), e)
      }
    }
  }

  // 1119 Changes end

  def LoadMessageIntoCache(key: String) {
    try {
      logger.debug("Fetch the object " + key + " from database ")
      val obj = getMetadataAPI.GetObject(key.toLowerCase, "messages")
      logger.debug("Deserialize the object " + key)
      val msg: MessageDef = MetadataAPISerialization.deserializeMetadata(new String(obj._2.asInstanceOf[Array[Byte]])).asInstanceOf[MessageDef] //serializer.DeserializeObjectFromByteArray(obj._2.asInstanceOf[Array[Byte]]).asInstanceOf[MessageDef]
      logger.debug("Get the jar from database ")
      val msgDef = msg.asInstanceOf[MessageDef]
      getMetadataAPI.DownloadJarFromDB(msgDef)
      logger.debug("Add the object " + key + " to the cache ")
      getMetadataAPI.AddObjectToCache(msgDef, MdMgr.GetMdMgr)
    } catch {
      case e: Exception => {
        logger.error("Failed to load message into cache " + key, e)
      }
    }
  }

  /**
   * LoadContainerIntoCache
   *
   * @param key
   */
  def LoadContainerIntoCache(key: String) {
    try {
      val obj = getMetadataAPI.GetObject(key.toLowerCase, "containers")
      val cont: ContainerDef = MetadataAPISerialization.deserializeMetadata(new String(obj._2.asInstanceOf[Array[Byte]])).asInstanceOf[ContainerDef] //serializer.DeserializeObjectFromByteArray(obj._2.asInstanceOf[Array[Byte]]).asInstanceOf[ContainerDef]
      logger.debug("Get the jar from database ")
      val contDef = cont.asInstanceOf[ContainerDef]
      getMetadataAPI.DownloadJarFromDB(contDef)
      getMetadataAPI.AddObjectToCache(contDef, MdMgr.GetMdMgr)
    } catch {
      case e: Exception => {

        logger.debug("", e)
      }
    }
  }

  /**
   * Get a the most recent mesage def (format JSON or XML) as a String
   *
   * @param objectName the name of the message possibly namespace qualified (is simple name, "system" namespace is substituted)
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def GetMessageDef(objectName: String, formatType: String, userid: Option[String] = None, tid : Option[String] = None): String = {
    val nameNodes: Array[String] = if (objectName != null && objectName.contains('.')) objectName.split('.') else Array(MdMgr.sysNS, objectName)
    val nmspcNodes: Array[String] = nameNodes.splitAt(nameNodes.size - 1)._1
    val buffer: StringBuilder = new StringBuilder
    val nameSpace: String = nmspcNodes.addString(buffer, ".").toString
    GetMessageDef(nameSpace, objectName, formatType, "-1", userid, tid)
  }

  /**
   * Get a specific message (format JSON or XML) as a String using messageName(with version) as the key
   *
   * @param objectName Name of the MessageDef, possibly namespace qualified.
   * @param version    Version of the MessageDef
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.resultData contains
   *         the MessageDef either as a JSON or XML string depending on the parameter formatType
   */
  def GetMessageDef(objectName: String, version: String, formatType: String, userid: Option[String], tid : Option[String]): String = {

    val nameNodes: Array[String] = if (objectName != null && objectName.contains('.')) objectName.split('.') else Array(MdMgr.sysNS, objectName)
    val nmspcNodes: Array[String] = nameNodes.splitAt(nameNodes.size - 1)._1
    val buffer: StringBuilder = new StringBuilder
    val nameSpace: String = nmspcNodes.addString(buffer, ".").toString
    GetMessageDef(nameSpace, objectName, formatType, version, userid, tid)
  }

  /**
   * Get a specific message (format JSON or XML) as a String using messageName(with version) as the key
   *
   * @param nameSpace  namespace of the object
   * @param objectName Name of the MessageDef
   * @param version    Version of the MessageDef
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.resultData contains
   *         the MessageDef either as a JSON or XML string depending on the parameter formatType
   */
  def GetMessageDef(nameSpace: String, objectName: String, formatType: String, version: String, userid: Option[String], tid : Option[String]): String = {
    getMetadataAPI.logAuditRec(userid, Some(AuditConstants.READ), AuditConstants.GETOBJECT, AuditConstants.MESSAGE, AuditConstants.SUCCESS, "", nameSpace + "." + objectName + "." + version)
    GetMessageDefFromCache(nameSpace, objectName, formatType, version, userid, tid)
  }

  /**
   * Get a specific container (format JSON or XML) as a String using containerName(without version) as the key
   *
   * @param objectName Name of the ContainerDef, possibly namespace qualified. When no namespace, "system" substituted
   * @param formatType
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @param tid tenantID filter
   * @return the result as a JSON String of object ApiResult where ApiResult.resultData contains
   *         the ContainerDef either as a JSON or XML string depending on the parameter formatType
   */
  def GetContainerDef(objectName: String, formatType: String, userid: Option[String] = None, tid : Option[String] = None): String = {
    val nameNodes: Array[String] = if (objectName != null && objectName.contains('.')) objectName.split('.') else Array(MdMgr.sysNS, objectName)
    val nmspcNodes: Array[String] = nameNodes.splitAt(nameNodes.size - 1)._1
    val buffer: StringBuilder = new StringBuilder
    val nameSpace: String = nmspcNodes.addString(buffer, ".").toString
    GetContainerDefFromCache(nameSpace, objectName, formatType, "-1", userid, tid)
  }

  /**
   * Get a specific container (format JSON or XML) as a String using containerName(with version) as the key
   *
   * @param nameSpace  namespace of the object
   * @param objectName Name of the ContainerDef
   * @param formatType format of the return value, either JSON or XML format of the return value, either JSON or XML
   * @param version    Version of the ContainerDef
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.resultData contains
   *         the ContainerDef either as a JSON or XML string depending on the parameter formatType
   */
  def GetContainerDef(nameSpace: String, objectName: String, formatType: String, version: String, userid: Option[String], tid : Option[String]): String = {
    getMetadataAPI.logAuditRec(userid, Some(AuditConstants.READ), AuditConstants.GETOBJECT, AuditConstants.CONTAINER, AuditConstants.SUCCESS, "", nameSpace + "." + objectName + "." + version)

    GetContainerDefFromCache(nameSpace, objectName, formatType, version, None, tid)
  }

  /**
   * Get a specific container (format JSON or XML) as a String using containerName(without version) as the key
   *
   * @param objectName Name of the ContainerDef, possibly namespace qualified. When no namespace, "system" substituted
   * @param version    Version of the object
   * @param formatType format of the return value, either JSON or XML
   * @param userid     the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *                   method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return the result as a JSON String of object ApiResult where ApiResult.resultData contains
   *         the ContainerDef either as a JSON or XML string depending on the parameter formatType
   */
  def GetContainerDef(objectName: String, version: String, formatType: String, userid: Option[String], tid : Option[String]): String = {
    val nameNodes: Array[String] = if (objectName != null && objectName.contains('.')) objectName.split('.') else Array(MdMgr.sysNS, objectName)
    val nmspcNodes: Array[String] = nameNodes.splitAt(nameNodes.size - 1)._1
    val buffer: StringBuilder = new StringBuilder
    val nameSpace: String = nmspcNodes.addString(buffer, ".").toString
    GetContainerDef(nameSpace, objectName, formatType, version, userid, tid)
  }

  /**
   * getModelMessagesContainers
   *
   * @param modelConfigName
   * @param userid the identity to be used by the security adapter to ascertain if this user has access permissions for this
   *               method. If Security and/or Audit are configured, this value must be a value other than None.
   * @return
   */
  def getModelMessagesContainers(modelConfigName: String, userid: Option[String]): List[String] = {
    // if userid is not supplied, it seem to defualt to "_"
    //val u = if( userid != None ) userid.get else "_"
    //var key = u + "." + modelConfigName
    var key = modelConfigName
    //if( userid != None ){
    //  key = userid.get + "." + modelConfigName
    //}
    logger.debug("MessageAndContainerUtils: Get the model config for " + key)
    var config = MdMgr.GetMdMgr.GetModelConfig(key.toLowerCase)
    logger.debug("MessageAndContainerUtils: Size of the model config map => " + config.keys.size);
    val typDeps = config.getOrElse(ModelCompilationConstants.TYPES_DEPENDENCIES, null)
    if (typDeps != null) {
      if (typDeps.isInstanceOf[List[_]])
        return typDeps.asInstanceOf[List[String]]
      if (typDeps.isInstanceOf[Array[_]])
        return typDeps.asInstanceOf[Array[String]].toList
    }
    logger.debug("Types in modelConfig object are not defined")
    List[String]()
  }

  def convertModelMessagesContainersToInputTypesSets(modelConfigName: String, userid: Option[String]): List[String] = {
    var key = modelConfigName
    logger.debug("Get the model config for " + key)
    var config = MdMgr.GetMdMgr.GetModelConfig(key)
    logger.debug("Size of the model config map => " + config.keys.size);
    val typDeps = config.getOrElse(ModelCompilationConstants.TYPES_DEPENDENCIES, null)
    if (typDeps != null) {
      if (typDeps.isInstanceOf[List[_]])
        return typDeps.asInstanceOf[List[String]]
      if (typDeps.isInstanceOf[Array[_]])
        return typDeps.asInstanceOf[Array[String]].toList
    }
    logger.debug("Types in modelConfig object are not defined")
    List[String]()
  }

  def getModelInputTypesSets(modelConfigName: String, userid: Option[String] = None): List[List[String]] = {
    var config = MdMgr.GetMdMgr.GetModelConfig(modelConfigName)
    val inputTypDeps = config.getOrElse(ModelCompilationConstants.INPUT_TYPES_SETS, null)
    if (inputTypDeps != null) {
      if (inputTypDeps.isInstanceOf[List[Any]]) {
        if (inputTypDeps.isInstanceOf[List[List[Any]]])
          return inputTypDeps.asInstanceOf[List[List[String]]]
        if (inputTypDeps.isInstanceOf[List[Array[Any]]])
          return inputTypDeps.asInstanceOf[List[Array[String]]].map(lst => lst.toList)
      } else if (inputTypDeps.isInstanceOf[Array[Any]]) {
        if (inputTypDeps.isInstanceOf[Array[List[Any]]])
          return inputTypDeps.asInstanceOf[Array[List[String]]].toList
        if (inputTypDeps.isInstanceOf[Array[Array[Any]]])
          return inputTypDeps.asInstanceOf[Array[Array[String]]].map(lst => lst.toList).toList
      }
    }
    List[List[String]]()
  }

  def getModelOutputTypes(modelConfigName: String, userid: Option[String]): List[String] = {
    var config = MdMgr.GetMdMgr.GetModelConfig(modelConfigName)
    val typDeps = config.getOrElse(ModelCompilationConstants.OUTPUT_TYPES_SETS, null)
    if (typDeps != null) {
      if (typDeps.isInstanceOf[List[_]])
        return typDeps.asInstanceOf[List[String]]
      if (typDeps.isInstanceOf[Array[_]])
        return typDeps.asInstanceOf[Array[String]].toList
    }
    List[String]()
  }

  def GetTypeBySchemaId(schemaId: Int, userid: Option[String]): String = {
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.GETOBJECT), AuditConstants.GETOBJECT, AuditConstants.CONTAINER, AuditConstants.SUCCESS, "", schemaId.toString)
    try {
      if (schemaId < 0) {
        val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetTypeBySchemaId", "", "Please provide the proper schema id")
        return apiResult.toString()
      }
      val o = MdMgr.GetMdMgr.ContainerForSchemaId(schemaId)
      o match {
        case None =>
          None
          logger.debug("message/container not found => " + schemaId)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetTypeBySchemaId", null, ErrorCodeConstants.Get_Type_By_SchemaId_Failed + ":" + schemaId)
          apiResult.toString()
        case Some(m) =>
          logger.debug("message/container found => " + m.asInstanceOf[ContainerDef].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[ContainerDef].Version))
          val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetTypeBySchemaId", m.asInstanceOf[ContainerDef].MdElementCategory + " - " + m.asInstanceOf[ContainerDef].PhysicalName, ErrorCodeConstants.Get_Type_By_SchemaId_Successful)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetTypeBySchemaId", null, "Error :" + e.toString() + ErrorCodeConstants.Get_Type_By_SchemaId_Failed + ":" + schemaId.toString)
        apiResult.toString()
      }
    }
  }

  def GetTypeByElementId(elementId: Long, userid: Option[String]): String = {
    if (userid != None) getMetadataAPI.logAuditRec(userid, Some(AuditConstants.GETOBJECT), AuditConstants.GETOBJECT, AuditConstants.CONTAINER, AuditConstants.SUCCESS, "", elementId.toString)
    try {
      if (elementId < 0) {
        val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetTypeByElementId", "", "Please provide the proper element id")
        return apiResult.toString()
      }
      val o = MdMgr.GetMdMgr.ElementForElementId(elementId)
      o match {
        case None =>
          None
          logger.debug("message/container/model not found => " + elementId)
          val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetTypeByElementId", null, ErrorCodeConstants.Get_Type_By_ElementId_Failed + ":" + elementId)
          apiResult.toString()
        case Some(m) =>
          logger.debug("message/container/model found => " + m.asInstanceOf[BaseElem].FullName + "." + MdMgr.Pad0s2Version(m.asInstanceOf[BaseElem].Version))
          val apiResult = new ApiResult(ErrorCodeConstants.Success, "GetTypeByElementId",   m.asInstanceOf[BaseElem].MdElementCategory +" - " +m.asInstanceOf[BaseElem].PhysicalName, ErrorCodeConstants.Get_Type_By_ElementId_Successful)
          apiResult.toString()
      }
    } catch {
      case e: Exception => {

        logger.debug("", e)
        val apiResult = new ApiResult(ErrorCodeConstants.Failure, "GetTypeByElementId", null, "Error :" + e.toString() + ErrorCodeConstants.Get_Type_By_ElementId_Failed + ":" + elementId.toString)
        apiResult.toString()
      }
    }
  }

}
