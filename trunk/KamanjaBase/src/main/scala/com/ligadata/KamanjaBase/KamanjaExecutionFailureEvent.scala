
package com.ligadata.KamanjaBase;

import org.json4s.jackson.JsonMethods._
import org.json4s.DefaultFormats
import org.json4s.Formats
import com.ligadata.KamanjaBase.{ AttributeTypeInfo, AttributeValue, ContainerFactoryInterface, ContainerInterface, MessageFactoryInterface, MessageInterface, TimePartitionInfo, ContainerOrConceptFactory, RDDObject, JavaRDDObject, ContainerOrConcept }
import com.ligadata.BaseTypes._
import com.ligadata.Exceptions.StackTrace;
import org.apache.logging.log4j.{ Logger, LogManager }
import java.util.Date;
import scala.collection.JavaConversions._

object KamanjaExecutionFailureEvent extends RDDObject[KamanjaExecutionFailureEvent] with MessageFactoryInterface {
  type T = KamanjaExecutionFailureEvent;
  override def getFullTypeName: String = "com.ligadata.KamanjaBase.KamanjaExecutionFailureEvent";
  override def getTypeNameSpace: String = "com.ligadata.KamanjaBase";
  override def getTypeName: String = "KamanjaExecutionFailureEvent";
  override def getTypeVersion: String = "000001.000002.000000";
  override def getSchemaId: Int = 1000005;
  override def createInstance: KamanjaExecutionFailureEvent = new KamanjaExecutionFailureEvent(KamanjaExecutionFailureEvent);
  override def isFixed: Boolean = true;
  override def getContainerType: ContainerFactoryInterface.ContainerType = ContainerFactoryInterface.ContainerType.MESSAGE
  override def getFullName = getFullTypeName;
  override def toJavaRDDObject: JavaRDDObject[T] = JavaRDDObject.fromRDDObject[T](this);

  def build = new T(this)
  def build(from: T) = new T(from)
  override def getPartitionKeyNames: Array[String] = Array[String]();

  override def getPrimaryKeyNames: Array[String] = Array[String]();

  override def getTimePartitionInfo: TimePartitionInfo = { return null; } // FieldName, Format & Time Partition Types(Daily/Monthly/Yearly)

  override def hasPrimaryKey(): Boolean = {
    val pKeys = getPrimaryKeyNames();
    return (pKeys != null && pKeys.length > 0);
  }

  override def hasPartitionKey(): Boolean = {
    val pKeys = getPartitionKeyNames();
    return (pKeys != null && pKeys.length > 0);
  }

  override def hasTimePartitionInfo(): Boolean = {
    val tmInfo = getTimePartitionInfo();
    return (tmInfo != null && tmInfo.getTimePartitionType != TimePartitionInfo.TimePartitionType.NONE);
  }

  override def getAvroSchema: String = """{ "type": "record",  "namespace" : "com.ligadata.kamanjabase" , "name" : "kamanjaexecutionfailureevent" , "fields":[{ "name" : "msgid" , "type" : "long"},{ "name" : "timeoferrorepochms" , "type" : "long"},{ "name" : "msgcontent" , "type" : "string"},{ "name" : "errordetail" , "type" : "string"}]}""";
}

class KamanjaExecutionFailureEvent(factory: MessageFactoryInterface, other: KamanjaExecutionFailureEvent) extends MessageInterface(factory) {

  private val log = LogManager.getLogger(getClass)

  var keyTypes: Map[String, AttributeTypeInfo] = attributeTypes.map { a => (a.getName, a) }.toMap;

  if (other != null && other != this) {
    // call copying fields from other to local variables
    fromFunc(other)
  }

  override def save: Unit = { /* KamanjaExecutionFailureEvent.saveOne(this) */ }

  def Clone(): ContainerOrConcept = { KamanjaExecutionFailureEvent.build(this) }

  override def getPartitionKey: Array[String] = Array[String]()

  override def getPrimaryKey: Array[String] = Array[String]()

  var attributeTypes = generateAttributeTypes;

  private def generateAttributeTypes(): Array[AttributeTypeInfo] = {
    var attributeTypes = new Array[AttributeTypeInfo](4);
    attributeTypes :+ new AttributeTypeInfo("msgid", 0, AttributeTypeInfo.TypeCategory.LONG, 4, 4, 0)
    attributeTypes :+ new AttributeTypeInfo("timeoferrorepochms", 1, AttributeTypeInfo.TypeCategory.LONG, 4, 4, 0)
    attributeTypes :+ new AttributeTypeInfo("msgcontent", 2, AttributeTypeInfo.TypeCategory.STRING, 1, 1, 0)
    attributeTypes :+ new AttributeTypeInfo("errordetail", 3, AttributeTypeInfo.TypeCategory.STRING, 1, 1, 0)

    return attributeTypes
  }

  var msgid: Long = _;
  var timeoferrorepochms: Long = _;
  var msgcontent: String = _;
  var errordetail: String = _;

  override def getAttributeTypes(): Array[AttributeTypeInfo] = {
    if (attributeTypes == null) return null;
    return attributeTypes
  }
  
  override def getAttributeType(name: String): AttributeTypeInfo = {
      if (name == null || name.trim() == "") return null;
      attributeTypes.foreach(attributeType => {
        if(attributeType.getName == name.toLowerCase())
          return attributeType
      }) 
      return null;
    }
  
  private def getWithReflection(key: String): AnyRef = {
    val ru = scala.reflect.runtime.universe
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val im = m.reflect(this)
    val fieldX = ru.typeOf[KamanjaExecutionFailureEvent].declaration(ru.newTermName(key)).asTerm.accessed.asTerm
    val fmX = im.reflectField(fieldX)
    return fmX.get.asInstanceOf[AnyRef];
  }

  override def get(key: String): AnyRef = {
    try {
      // Try with reflection
      return getByName(key.toLowerCase())
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        // Call By Name
        return getWithReflection(key.toLowerCase())
      }
    }
  }

  private def getByName(key: String): AnyRef = {
    if (!keyTypes.contains(key)) throw new Exception(s"Key $key does not exists in message/container hl7Fixed ");
    return get(keyTypes(key).getIndex)
  }

  override def getOrElse(key: String, defaultVal: Any): AnyRef = { // Return (value, type)
    try {
      val value = get(key.toLowerCase())
      if (value == null) return defaultVal.asInstanceOf[AnyRef]; else return value;
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    }
    return null;
  }

  override def getOrElse(index: Int, defaultVal: Any): AnyRef = { // Return (value,  type)
    try {
      val value = get(index)
      if (value == null) return defaultVal.asInstanceOf[AnyRef]; else return value;
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    }
    return null;
  }

  override def getAttributeNames(): Array[String] = {
    try {
      if (keyTypes.isEmpty) {
        return null;
      } else {
        return keyTypes.keySet.toArray;
      }
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    }
    return null;
  }

  override def getAllAttributeValues(): Array[AttributeValue] = { // Has ( value, attributetypeinfo))
    var attributeVals = new Array[AttributeValue](4);
    try {
      attributeVals :+ new AttributeValue(this.msgid, keyTypes("msgid"))
      attributeVals :+ new AttributeValue(this.timeoferrorepochms, keyTypes("timeoferrorepochms"))
      attributeVals :+ new AttributeValue(this.msgcontent, keyTypes("msgcontent"))
      attributeVals :+ new AttributeValue(this.errordetail, keyTypes("errordetail"))

    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

    return attributeVals;
  }

  override def getAttributeNameAndValueIterator(): java.util.Iterator[AttributeValue] = {
    //getAllAttributeValues.iterator.asInstanceOf[java.util.Iterator[AttributeValue]];
    return null; // Fix - need to test to make sure the above iterator works properly

  }

  override def get(index: Int): AnyRef = { // Return (value, type)
    try {
      index match {
        case 0 => return this.msgid.asInstanceOf[AnyRef];
        case 1 => return this.timeoferrorepochms.asInstanceOf[AnyRef];
        case 2 => return this.msgcontent.asInstanceOf[AnyRef];
        case 3 => return this.errordetail.asInstanceOf[AnyRef];

        case _ => throw new Exception(s"$index is a bad index for message KamanjaExecutionFailureEvent");
      }
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

  }

  override def set(key: String, value: Any) = {
    try {

      if (!keyTypes.contains(key)) throw new Exception(s"Key $key does not exists in message KamanjaExecutionFailureEvent")
      set(keyTypes(key).getIndex, value);

    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

  }

  def set(index: Int, value: Any): Unit = {
    if (value == null) throw new Exception(s"Value is null for index $index in message KamanjaExecutionFailureEvent ")
    try {
      index match {
        case 0 => {
          if (value.isInstanceOf[Long])
            this.msgid = value.asInstanceOf[Long];
          else throw new Exception(s"Value is the not the correct type for index $index in message KamanjaExecutionFailureEvent")
        }
        case 1 => {
          if (value.isInstanceOf[Long])
            this.timeoferrorepochms = value.asInstanceOf[Long];
          else throw new Exception(s"Value is the not the correct type for index $index in message KamanjaExecutionFailureEvent")
        }
        case 2 => {
          if (value.isInstanceOf[String])
            this.msgcontent = value.asInstanceOf[String];
          else throw new Exception(s"Value is the not the correct type for index $index in message KamanjaExecutionFailureEvent")
        }
        case 3 => {
          if (value.isInstanceOf[String])
            this.errordetail = value.asInstanceOf[String];
          else throw new Exception(s"Value is the not the correct type for index $index in message KamanjaExecutionFailureEvent")
        }

        case _ => throw new Exception(s"$index is a bad index for message KamanjaExecutionFailureEvent");
      }
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

  }

  override def set(key: String, value: Any, valTyp: String) = {
    throw new Exception("Set Func for Value and ValueType By Key is not supported for Fixed Messages")
  }

  private def fromFunc(other: KamanjaExecutionFailureEvent): KamanjaExecutionFailureEvent = {
    this.msgid = com.ligadata.BaseTypes.LongImpl.Clone(other.msgid);
    this.timeoferrorepochms = com.ligadata.BaseTypes.LongImpl.Clone(other.timeoferrorepochms);
    this.msgcontent = com.ligadata.BaseTypes.StringImpl.Clone(other.msgcontent);
    this.errordetail = com.ligadata.BaseTypes.StringImpl.Clone(other.errordetail);

    //this.timePartitionData = com.ligadata.BaseTypes.LongImpl.Clone(other.timePartitionData);
    return this;
  }

  def this(factory: MessageFactoryInterface) = {
    this(factory, null)
  }

  def this(other: KamanjaExecutionFailureEvent) = {
    this(other.getFactory.asInstanceOf[MessageFactoryInterface], other)
  }

}