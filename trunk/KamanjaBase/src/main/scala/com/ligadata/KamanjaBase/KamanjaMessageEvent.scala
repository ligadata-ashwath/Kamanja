
package com.ligadata.KamanjaBase

import org.json4s.jackson.JsonMethods._;
import org.json4s.DefaultFormats;
import org.json4s.Formats;
import com.ligadata.KamanjaBase._;
import com.ligadata.BaseTypes._;
import com.ligadata.Exceptions.StackTrace;
import org.apache.logging.log4j.{ Logger, LogManager }
import java.util.Date;
import java.io.{ DataInputStream, DataOutputStream, ByteArrayOutputStream }



object KamanjaMessageEvent extends RDDObject[KamanjaMessageEvent] with MessageFactoryInterface {

  val log = LogManager.getLogger(getClass)
  type T = KamanjaMessageEvent ;
  override def getFullTypeName: String = "com.ligadata.KamanjaBase.KamanjaMessageEvent";
  override def getTypeNameSpace: String = "com.ligadata.KamanjaBase";
  override def getTypeName: String = "KamanjaMessageEvent";
  override def getTypeVersion: String = "000001.000000.000000";
  override def getSchemaId: Int = 1000005;
  override def getTenantId: String = "System";
  override def createInstance: KamanjaMessageEvent = new KamanjaMessageEvent(KamanjaMessageEvent);
  override def isFixed: Boolean = true;
  override def getContainerType: ContainerTypes.ContainerType = ContainerTypes.ContainerType.MESSAGE
  override def getFullName = getFullTypeName;
  override def getRddTenantId = getTenantId;
  override def toJavaRDDObject: JavaRDDObject[T] = JavaRDDObject.fromRDDObject[T](this);

  def build = new T(this)
  def build(from: T) = new T(from)
  override def getPartitionKeyNames: Array[String] = Array[String]();

  override def getPrimaryKeyNames: Array[String] = Array[String]();


  override def getTimePartitionInfo: TimePartitionInfo = { return null;}  // FieldName, Format & Time Partition Types(Daily/Monthly/Yearly)


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

  override def getAvroSchema: String = """{ "type": "record",  "namespace" : "com.ligadata.kamanjabase" , "name" : "KamanjaMessageEvent" , "fields":[{ "name" : "messageid" , "type" : "long"},{ "name" : "modelinfo" ,"type": {"type": "array", "items": }},{ "name" : "elapsedtimeinms" , "type" : "float"},{ "name" : "eventepochtime" , "type" : "long"},{ "name" : "messagekey" , "type" : "string"},{ "name" : "messagevalue" , "type" : "string"},{ "name" : "error" , "type" : "string"}]}""";

  final override def convertFrom(srcObj: Any): T = convertFrom(createInstance(), srcObj);

  override def convertFrom(newVerObj: Any, oldVerobj: Any): ContainerInterface = {
    try {
      if (oldVerobj == null) return null;
      oldVerobj match {

        case oldVerobj: com.ligadata.KamanjaBase.KamanjaMessageEvent => { return  convertToVer1000000000000(oldVerobj); }
        case _ => {
          throw new Exception("Unhandled Version Found");
        }
      }
    } catch {
      case e: Exception => {
        throw e
      }
    }
    return null;
  }

  private def convertToVer1000000000000(oldVerobj: com.ligadata.KamanjaBase.KamanjaMessageEvent): com.ligadata.KamanjaBase.KamanjaMessageEvent= {
    return oldVerobj
  }


  /****   DEPRECATED METHODS ***/
  override def FullName: String = getFullTypeName
  override def NameSpace: String = getTypeNameSpace
  override def Name: String = getTypeName
  override def Version: String = getTypeVersion
  override def CreateNewMessage: BaseMsg= createInstance.asInstanceOf[BaseMsg];
  override def CreateNewContainer: BaseContainer= null;
  override def IsFixed: Boolean = true
  override def IsKv: Boolean = false
  override def CanPersist: Boolean = false
  override def isMessage: Boolean = true
  override def isContainer: Boolean = false
  override def PartitionKeyData(inputdata: InputData): Array[String] = { throw new Exception("Deprecated method PartitionKeyData in obj KamanjaMessageEvent") };
  override def PrimaryKeyData(inputdata: InputData): Array[String] = throw new Exception("Deprecated method PrimaryKeyData in obj KamanjaMessageEvent");
  override def TimePartitionData(inputdata: InputData): Long = throw new Exception("Deprecated method TimePartitionData in obj KamanjaMessageEvent");
  override def NeedToTransformData: Boolean = false
}

class KamanjaMessageEvent(factory: MessageFactoryInterface, other: KamanjaMessageEvent) extends MessageInterface(factory) {

  val log = KamanjaMessageEvent.log

  var attributeTypes = generateAttributeTypes;

  private def generateAttributeTypes(): Array[AttributeTypeInfo] = {
    var attributeTypes = new Array[AttributeTypeInfo](7);
    attributeTypes(0) = new AttributeTypeInfo("messageid", 0, AttributeTypeInfo.TypeCategory.LONG, -1, -1, 0)
    attributeTypes(1) = new AttributeTypeInfo("modelinfo", 1, AttributeTypeInfo.TypeCategory.ARRAY, 1001, -1, 5)
    attributeTypes(2) = new AttributeTypeInfo("elapsedtimeinms", 2, AttributeTypeInfo.TypeCategory.FLOAT, -1, -1, 0)
    attributeTypes(3) = new AttributeTypeInfo("eventepochtime", 3, AttributeTypeInfo.TypeCategory.LONG, -1, -1, 0)
    attributeTypes(4) = new AttributeTypeInfo("messagekey", 4, AttributeTypeInfo.TypeCategory.STRING, -1, -1, 0)
    attributeTypes(5) = new AttributeTypeInfo("messagevalue", 5, AttributeTypeInfo.TypeCategory.STRING, -1, -1, 0)
    attributeTypes(6) = new AttributeTypeInfo("error", 6, AttributeTypeInfo.TypeCategory.STRING, -1, -1, 0)


    return attributeTypes
  }

  var keyTypes: Map[String, AttributeTypeInfo] = attributeTypes.map { a => (a.getName, a) }.toMap;

  if (other != null && other != this) {
    // call copying fields from other to local variables
    fromFunc(other)
  }

  override def save: Unit = { /* KamanjaMessageEvent.saveOne(this) */}

  def Clone(): ContainerOrConcept = { KamanjaMessageEvent.build(this) }

  override def getPartitionKey: Array[String] = Array[String]()

  override def getPrimaryKey: Array[String] = Array[String]()

  override def getAttributeType(name: String): AttributeTypeInfo = {
    if (name == null || name.trim() == "") return null;
    attributeTypes.foreach(attributeType => {
      if(attributeType.getName == name.toLowerCase())
        return attributeType
    })
    return null;
  }


  var messageid: Long = _;
  var modelinfo: scala.Array[com.ligadata.KamanjaBase.KamanjaModelEvent] = _;
  var elapsedtimeinms: Float = _;
  var eventepochtime: Long = _;
  var messagekey: String = _;
  var messagevalue: String = _;
  var error: String = _;

  override def getAttributeTypes(): Array[AttributeTypeInfo] = {
    if (attributeTypes == null) return null;
    return attributeTypes
  }

  private def getWithReflection(keyName: String): AnyRef = {
    if(keyName == null || keyName.trim.size == 0) throw new Exception("Please provide proper key name "+keyName);
    val key = keyName.toLowerCase;
    val ru = scala.reflect.runtime.universe
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val im = m.reflect(this)
    val fieldX = ru.typeOf[KamanjaMessageEvent].declaration(ru.newTermName(key)).asTerm.accessed.asTerm
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

  private def getByName(keyName: String): AnyRef = {
    if(keyName == null || keyName.trim.size == 0) throw new Exception("Please provide proper key name "+keyName);
    val key = keyName.toLowerCase;

    if (!keyTypes.contains(key)) throw new Exception(s"Key $key does not exists in message/container KamanjaMessageEvent");
    return get(keyTypes(key).getIndex)
  }

  override def getOrElse(keyName: String, defaultVal: Any): AnyRef = { // Return (value, type)
    if (keyName == null || keyName.trim.size == 0) throw new Exception("Please provide proper key name "+keyName);
    val key = keyName.toLowerCase;
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


  override def get(index : Int) : AnyRef = { // Return (value, type)
    try{
      index match {
        case 0 => return this.messageid.asInstanceOf[AnyRef];
        case 1 => return this.modelinfo.asInstanceOf[AnyRef];
        case 2 => return this.elapsedtimeinms.asInstanceOf[AnyRef];
        case 3 => return this.eventepochtime.asInstanceOf[AnyRef];
        case 4 => return this.messagekey.asInstanceOf[AnyRef];
        case 5 => return this.messagevalue.asInstanceOf[AnyRef];
        case 6 => return this.error.asInstanceOf[AnyRef];

        case _ => throw new Exception(s"$index is a bad index for message KamanjaMessageEvent");
      }
    }catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

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
  var attributeVals = new Array[AttributeValue](7);
    try{
      attributeVals(0) = new AttributeValue(this.messageid, keyTypes("messageid"))
      attributeVals(1) = new AttributeValue(this.modelinfo, keyTypes("modelinfo"))
      attributeVals(2) = new AttributeValue(this.elapsedtimeinms, keyTypes("elapsedtimeinms"))
      attributeVals(3) = new AttributeValue(this.eventepochtime, keyTypes("eventepochtime"))
      attributeVals(4) = new AttributeValue(this.messagekey, keyTypes("messagekey"))
      attributeVals(5) = new AttributeValue(this.messagevalue, keyTypes("messagevalue"))
      attributeVals(6) = new AttributeValue(this.error, keyTypes("error"))

    }catch {
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

  override def set(keyName: String, value: Any) = {
    if(keyName == null || keyName.trim.size == 0) throw new Exception("Please provide proper key name "+keyName);
    val key = keyName.toLowerCase;
    try {

      if (!keyTypes.contains(key)) throw new Exception(s"Key $key does not exists in message KamanjaMessageEvent")
      set(keyTypes(key).getIndex, value);

    }catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

  }


  def set(index : Int, value :Any): Unit = {
    if (value == null) throw new Exception(s"Value is null for index $index in message KamanjaMessageEvent ")
    try{
      index match {
        case 0 => {
          if(value.isInstanceOf[Long])
            this.messageid = value.asInstanceOf[Long];
          else throw new Exception(s"Value is the not the correct type for field messageid in message KamanjaMessageEvent")
        }
        case 1 => {
          if(value.isInstanceOf[scala.Array[com.ligadata.KamanjaBase.KamanjaModelEvent]])
            this.modelinfo = value.asInstanceOf[scala.Array[com.ligadata.KamanjaBase.KamanjaModelEvent]];
          else if(value.isInstanceOf[scala.Array[ContainerInterface]])
            this.modelinfo = value.asInstanceOf[scala.Array[ContainerInterface]].map(v => v.asInstanceOf[com.ligadata.KamanjaBase.KamanjaModelEvent]);
          else throw new Exception(s"Value is the not the correct type for field modelinfo in message KamanjaMessageEvent")
        }
        case 2 => {
          if(value.isInstanceOf[Float])
            this.elapsedtimeinms = value.asInstanceOf[Float];
          else throw new Exception(s"Value is the not the correct type for field elapsedtimeinms in message KamanjaMessageEvent")
        }
        case 3 => {
          if(value.isInstanceOf[Long])
            this.eventepochtime = value.asInstanceOf[Long];
          else throw new Exception(s"Value is the not the correct type for field eventepochtime in message KamanjaMessageEvent")
        }
        case 4 => {
          if(value.isInstanceOf[String])
            this.messagekey = value.asInstanceOf[String];
          else throw new Exception(s"Value is the not the correct type for field messagekey in message KamanjaMessageEvent")
        }
        case 5 => {
          if(value.isInstanceOf[String])
            this.messagevalue = value.asInstanceOf[String];
          else throw new Exception(s"Value is the not the correct type for field messagevalue in message KamanjaMessageEvent")
        }
        case 6 => {
          if(value.isInstanceOf[String])
            this.error = value.asInstanceOf[String];
          else throw new Exception(s"Value is the not the correct type for field error in message KamanjaMessageEvent")
        }

        case _ => throw new Exception(s"$index is a bad index for message KamanjaMessageEvent");
      }
    }catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    };

  }

  override def set(key: String, value: Any, valTyp: String) = {
    throw new Exception ("Set Func for Value and ValueType By Key is not supported for Fixed Messages" )
  }

  private def fromFunc(other: KamanjaMessageEvent): KamanjaMessageEvent = {
    this.messageid = com.ligadata.BaseTypes.LongImpl.Clone(other.messageid);
    if (other.modelinfo != null) {
      modelinfo = new scala.Array[com.ligadata.KamanjaBase.KamanjaModelEvent](other.modelinfo.length)
      modelinfo = other.modelinfo.map(f => f.Clone.asInstanceOf[com.ligadata.KamanjaBase.KamanjaModelEvent] );
    }
    else modelinfo = null;
    this.elapsedtimeinms = com.ligadata.BaseTypes.FloatImpl.Clone(other.elapsedtimeinms);
    this.eventepochtime = com.ligadata.BaseTypes.LongImpl.Clone(other.eventepochtime);
    this.messagekey = com.ligadata.BaseTypes.StringImpl.Clone(other.messagekey);
    this.messagevalue = com.ligadata.BaseTypes.StringImpl.Clone(other.messagevalue);
    this.error = com.ligadata.BaseTypes.StringImpl.Clone(other.error);

    this.setTimePartitionData(com.ligadata.BaseTypes.LongImpl.Clone(other.getTimePartitionData));
    return this;
  }

  def withmessageid(value: Long) : KamanjaMessageEvent = {
    this.messageid = value
    return this
  }
  def withmodelinfo(value: scala.Array[com.ligadata.KamanjaBase.KamanjaModelEvent]) : KamanjaMessageEvent = {
    this.modelinfo = value
    return this
  }
  def withelapsedtimeinms(value: Float) : KamanjaMessageEvent = {
    this.elapsedtimeinms = value
    return this
  }
  def witheventepochtime(value: Long) : KamanjaMessageEvent = {
    this.eventepochtime = value
    return this
  }
  def withmessagekey(value: String) : KamanjaMessageEvent = {
    this.messagekey = value
    return this
  }
  def withmessagevalue(value: String) : KamanjaMessageEvent = {
    this.messagevalue = value
    return this
  }
  def witherror(value: String) : KamanjaMessageEvent = {
    this.error = value
    return this
  }

  def this(factory:MessageFactoryInterface) = {
    this(factory, null)
  }

  def this(other: KamanjaMessageEvent) = {
    this(other.getFactory.asInstanceOf[MessageFactoryInterface], other)
  }

}