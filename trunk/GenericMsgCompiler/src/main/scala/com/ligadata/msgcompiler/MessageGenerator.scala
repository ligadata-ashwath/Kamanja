package com.ligadata.msgcompiler

import com.ligadata.Exceptions._;
import com.ligadata.Exceptions.StackTrace;
import org.apache.logging.log4j.{ Logger, LogManager }
import com.ligadata.kamanja.metadata._;

class MessageGenerator {

  var builderGenerator = new MessageBuilderGenerator
  var msgObjectGenerator = new MessageObjectGenerator
  var mappedMsgGen = new MappedMsgGenerator
  var msgConstants = new MessageConstants
  val logger = this.getClass.getName
  lazy val log = LogManager.getLogger(logger)

  /*
   * Generate the versioned and non versioned class for both mapped and Fixed messages
 	 * add import stamts -- still need to add
   * Generate Message Class
   * Message Class lines generation
   * Generate all the getter methods in the class generation
   * Generation all the setter methods in class generation
   * 
   */
  def generateMessage(message: Message, mdMgr: MdMgr): (String, String) = {

    generateMsg(message, mdMgr)
  }

  private def generateMsg(message: Message, mdMgr: MdMgr): (String, String) = {

    var messageVerGenerator = new StringBuilder(8 * 1024)
    var messageNonVerGenerator = new StringBuilder(8 * 1024)
    var messageGenerator = new StringBuilder(8 * 1024)

    try {
      messageVerGenerator = messageVerGenerator.append(msgConstants.newline + msgConstants.packageStr.format(message.Pkg, msgConstants.newline));
      messageNonVerGenerator = messageNonVerGenerator.append(msgConstants.newline + msgConstants.packageStr.format(message.NameSpace, msgConstants.newline));
      messageGenerator = messageGenerator.append(msgConstants.importStatements + msgConstants.newline);
      messageGenerator = messageGenerator.append(msgObjectGenerator.generateMessageObject(message) + msgConstants.newline);
      messageGenerator = messageGenerator.append(msgConstants.newline);
      messageGenerator = messageGenerator.append(classGen(message));
      messageGenerator = messageGenerator.append(getMessgeBasicDetails(message));
      messageGenerator = messageGenerator.append(msgConstants.newline + keyTypesMap(message.Elements));
      messageGenerator = messageGenerator.append(methodsFromBaseMsg(message));
      messageGenerator = messageGenerator.append(msgConstants.newline + generateParitionKeysData(message) + msgConstants.newline);
      messageGenerator = messageGenerator.append(msgConstants.newline + generatePrimaryKeysData(message) + msgConstants.newline);
      if (message.Fixed.equalsIgnoreCase("true")) {
        messageGenerator = messageGenerator.append(msgConstants.newline + generatedMsgVariables(message));
        messageGenerator = messageGenerator.append(getSetMethodsFixed(message));
      } else if (message.Fixed.equalsIgnoreCase("false")) {
        var fieldIndexMap: Map[String, Int] = msgConstants.getScalarFieldindex(message.Elements)
        messageGenerator = messageGenerator.append(msgConstants.getSetMethods);
      }
      messageGenerator = messageGenerator.append(messageContructor(message))
      messageGenerator = messageGenerator.append(msgConstants.newline + msgConstants.closeBrace);
      messageVerGenerator = messageVerGenerator.append(messageGenerator.toString())
      messageNonVerGenerator = messageNonVerGenerator.append(messageGenerator.toString())
      // log.info("messageGenerator    " + messageGenerator.toString())
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return (messageVerGenerator.toString, messageNonVerGenerator.toString())
  }
  /*
   * Message Class
   */
  private def classGen(message: Message): String = {
    var baseMsgType: String = "";
    var factoryStr: String = "";

    val msgType = message.MsgType
    if (msgType == null || msgType.trim() == "")
      throw new Exception("Message Definition root element should be either Message or Container")

    if (msgType.equalsIgnoreCase(msgConstants.messageStr)) {
      baseMsgType = msgConstants.baseMsg
      factoryStr = msgConstants.baseMsgObj
    } else if (msgType.equalsIgnoreCase(msgConstants.containerStr)) {
      baseMsgType = msgConstants.baseContainer
      factoryStr = msgConstants.baseContainerObj
    }
    // (var transactionId: Long, other: CustAlertHistory) extends BaseContainer {
    return msgConstants.classStr.format(message.Name, factoryStr, baseMsgType, msgConstants.newline)

  }

  /*
   * getSet methods for Fixed Message
   */
  private def getSetMethodsFixed(message: Message): String = {
    var getSetFixed = new StringBuilder(8 * 1024)
    try {
      getSetFixed = getSetFixed.append(getWithReflection(message));
      getSetFixed = getSetFixed.append(getByStringhFixed(message));
      getSetFixed = getSetFixed.append(getByName(message));
      getSetFixed = getSetFixed.append(getOrElseFunc());
      getSetFixed = getSetFixed.append(getOrElseByIndexFunc);
      getSetFixed = getSetFixed.append(getAllAttributeValuesFixed(message));
      getSetFixed = getSetFixed.append(getAttributeNameAndValueIterator);
      getSetFixed = getSetFixed.append(getFuncByOffset(message.Elements));
      getSetFixed = getSetFixed.append(setByKeyFunc(message));
      getSetFixed = getSetFixed.append(setFuncByOffset(message.Elements));
      getSetFixed = getSetFixed.append(setValueAndValueTypeByKeyFunc);
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    }
    getSetFixed.toString()
  }

  /*
   * Generate the variables for the message 
   */
  private def generatedMsgVariables(message: Message): String = {
    var msgVariables = new StringBuilder(8 * 1024)
    try {
      message.Elements.foreach(field => {
        msgVariables.append(" %svar %s: %s = _; %s".format(msgConstants.pad2, field.Name, field.FieldTypePhysicalName, msgConstants.newline))
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    msgVariables.toString()
  }

  /*
   * Message constructor generation
   */

  private def msgConstructor(message: Message, msgStr: String, constructorStmts: String): String = {
    var msgClassConsGen: String = ""

    msgClassConsGen = """
        def """ + message.Name + """(""" + msgStr.substring(0, msgStr.length() - 2) + """) {
    """ + constructorStmts + """
        }"""

    return msgClassConsGen
  }

  /*
   * Message Class Constructor Generation
   */

  private def msgClassConstructorGen(message: Message): String = {
    var msgClassConsGen: String = ""
    var msgConsStr = new StringBuilder(8 * 1024)
    var constructorStmts = new StringBuilder(8 * 1024)

    try {
      message.Elements.foreach(element => {
        msgConsStr.append("%s: %s, ".format(element.Name, element.FieldTypePhysicalName))
        constructorStmts.append("%s this.%s = %s; %s ".format(msgConstants.pad2, element.Name, element.Name, msgConstants.newline))
      })
      val msgStr = msgConsStr.toString
      //log.info("constructor Generation ===================" + msgStr.substring(0, msgStr.length() - 1))
      msgClassConsGen = msgConstructor(message, msgStr, constructorStmts.toString)
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return msgClassConsGen.toString
  }

  /*
   * Get Method generation function for Fixed Messages
   */
  private def getFuncGeneration(fields: List[Element]): String = {
    var getMethod = new StringBuilder(8 * 1024)
    var getmethodStr: String = ""
    try {
      fields.foreach(field => {
        getmethodStr = """
        def get""" + field.Name.capitalize + """: """ + field.FieldTypePhysicalName + """= {
        	return this.""" + field.Name + """;
        }          
        """
        getMethod = getMethod.append(getmethodStr.toString())
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return getMethod.toString
  }

  /*
   * Get All Attribute Values of the message/Container
   */

  private def getAllAttributeValuesFixed(message: Message): String = {
    """
    override def getAllAttributeValues(): java.util.HashMap[String, AttributeValue] = { // Has (name, value, type))
      var attributeValsMap = new java.util.HashMap[String, AttributeValue];
      try{
 """ + getAttributeFixed(message.Elements) + """       
      }""" + msgConstants.catchStmt + """
      return attributeValsMap;
    }      
    """
  }

  /*
   * Get attributess for Fixed - GetAllAttributeValues
   */
  private def getAttributeFixed(fields: List[Element]): String = {
    var getAttributeFixedStrBldr = new StringBuilder(8 * 1024)
    var getAttributeFixed: String = ""
    try {
      fields.foreach(field => {
        getAttributeFixedStrBldr.append("%s{%s".format(msgConstants.pad3, msgConstants.newline));
        getAttributeFixedStrBldr.append("%svar attributeVal = new AttributeValue(); %s".format(msgConstants.pad4, msgConstants.newline));
        getAttributeFixedStrBldr.append("%sattributeVal.setValue(%s) %s".format(msgConstants.pad4, field.Name, msgConstants.newline));
        getAttributeFixedStrBldr.append("%sattributeVal.setValueType(keyTypes(\"%s\")) %s".format(msgConstants.pad4, field.Name, msgConstants.newline));
        getAttributeFixedStrBldr.append("%sattributeValsMap.put(\"%s\", attributeVal) %s".format(msgConstants.pad4, field.Name, msgConstants.newline));
        getAttributeFixedStrBldr.append("%s};%s".format(msgConstants.pad3, msgConstants.newline));
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return getAttributeFixedStrBldr.toString

  }

  /*
   * Get By Ordinal Function generation
   */
  private def getFuncByOffset(fields: List[Element]): String = {
    var getFuncByOffset: String = ""
    getFuncByOffset = """
      
    def get(index : Int) : AttributeValue = { // Return (value, type)
      var attributeValue = new AttributeValue();
      try{
        index match {
   """ + getByOffset(fields) + """
      	 case _ => throw new Exception("Bad index");
    	  }
        return attributeValue;
      }""" + msgConstants.catchStmt + """
    }      
    """
    return getFuncByOffset
  }

  /*
   * Get By Ordinal Function generation
   */
  private def getByOffset(fields: List[Element]): String = {
    var getByOffset = new StringBuilder(8 * 1024)
    try {
      fields.foreach(field => {
        getByOffset.append("%scase %s => { %s".format(msgConstants.pad2, field.FieldOrdinal, msgConstants.newline))
        getByOffset.append("%sattributeValue.setValue(this.%s); %s".format(msgConstants.pad3, field.Name, msgConstants.newline))
        getByOffset.append("%sattributeValue.setValueType(keyTypes(\"%s\"));  %s".format(msgConstants.pad3, field.Name, msgConstants.newline))
        getByOffset.append("%s } %s".format(msgConstants.pad2, msgConstants.newline))
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return getByOffset.toString
  }

  /*
   * Set By Ordinal Function generation
   */
  private def setFuncByOffset(fields: List[Element]): String = {
    var getFuncByOffset: String = ""
    getFuncByOffset = """
      
    def set(index : Int, value :Any): Unit = {
      try{
        index match {
 """ + setByOffset(fields) + """
        case _ => throw new Exception("Bad index");
        }
    	}""" + msgConstants.catchStmt + """
    }      
    """
    return getFuncByOffset
  }

  /*
   * Set By Ordinal Function generation
   */
  private def setByOffset(fields: List[Element]): String = {
    var setByOffset = new StringBuilder(8 * 1024)
    try {
      fields.foreach(field => {
        setByOffset.append("%scase %s => {this.%s = value.asInstanceOf[%s];} %s".format(msgConstants.pad4, field.FieldOrdinal, field.Name, field.FieldTypePhysicalName, msgConstants.newline))
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return setByOffset.toString
  }

  private def messageContructor(message: Message): String = {
    """
   def this(txnId: Long) = {
    this(txnId, null)
  }
  def this(other: """ + message.Name + """) = {
    this(0, other)
  }
  def this() = {
    this(0, null)
  }
   
    
  """
  }

  /*
   * message basic details in class
   */
  private def getMessgeBasicDetails(message: Message): String = {
    """ 
    val logger = this.getClass.getName
    lazy val log = LogManager.getLogger(logger)
   """
  }

  /*
   * some overridable methods from BaseMsg
   */
  private def methodsFromBaseMsg(message: Message): String = {
    """    
    override def save: Unit = { """ + message.Name + """.saveOne(this) }
  
    def Clone(): ContainerOrConcept = { """ + message.Name + """.build(this) }
    """
  }

  /*
   * GetOrElse method for Fixed messages
   */
  private def getOrElseFunc(): String = {
    """
    override def getOrElse(key: String, defaultVal: Any): AttributeValue = { // Return (value, type)
      var attributeValue: AttributeValue = new AttributeValue();
      try {
        val value = get(key.toLowerCase())
        if (value == null) {
          attributeValue.setValue(defaultVal);
          attributeValue.setValueType("Any");
          return attributeValue;
          } else {
          return value;
         }
        } catch {
          case e: Exception => {
          log.debug("", e)
          throw e
        }
      }
      return null;
    }
   """
  }

  /*
   * GetOrElse by index
   */
  private def getOrElseByIndexFunc = {
    """
    override def getOrElse(index: Int, defaultVal: Any): AttributeValue = { // Return (value,  type)
      var attributeValue: AttributeValue = new AttributeValue();
      try {
        val value = get(index)
        if (value == null) {
          attributeValue.setValue(defaultVal);
          attributeValue.setValueType("Any");
          return attributeValue;
        } else {
          return value;
        }
      } catch {
        case e: Exception => {
          log.debug("", e)
          throw e
        }
      }
      return null; ;
    }  
  """
  }
  /*
   * parititon keys code generation
   */

  private def generateParitionKeysData(message: Message): String = {
    var paritionKeysGen = new StringBuilder(8 * 1024)
    var returnPartitionKeyStr: String = ""
    val arryOfStr: String = "Array[String]()";

    if (message.PartitionKeys != null && message.PartitionKeys.size > 0) {
      paritionKeysGen.append("{" + msgConstants.newline)
      paritionKeysGen.append(msgConstants.partitionKeyVar.format(msgConstants.pad2, msgConstants.newline))
      paritionKeysGen.append(msgConstants.pad2 + "try {" + msgConstants.newline)

      message.PartitionKeys.foreach(key => {
        message.Elements.foreach(element => {
          if (element.Name.equalsIgnoreCase(key)) {
            paritionKeysGen.append("%s partitionKeys += %s.toString(get(\"%s\").getValue.asInstanceOf[%s]);%s".format(msgConstants.pad2, element.FldMetaataType.implementationName, element.Name.toLowerCase(), element.FieldTypePhysicalName, msgConstants.newline)) //"+ com.ligadata.BaseTypes.StringImpl+".toString(get"+element.Name.capitalize+") ")
          }
        })
      })
      paritionKeysGen.append("%s }".format(msgConstants.pad2))
      paritionKeysGen.append(msgConstants.catchStmt)
      paritionKeysGen.append("%s partitionKeys.toArray; %s".format(msgConstants.pad2, msgConstants.newline))
      paritionKeysGen.append("%s ".format(msgConstants.newline))
      paritionKeysGen.append("%s} %s".format(msgConstants.pad2, msgConstants.newline))
      returnPartitionKeyStr = msgConstants.paritionKeyData.format(msgConstants.pad2, paritionKeysGen.toString)
    } else {
      returnPartitionKeyStr = msgConstants.paritionKeyData.format(msgConstants.pad2, arryOfStr)
    }

    returnPartitionKeyStr
  }

  /*
   * primary keys code generation
   */
  private def generatePrimaryKeysData(message: Message): String = {
    var primaryKeysGen = new StringBuilder(8 * 1024)
    var returnPrimaryKeyStr: String = ""
    val arryOfStr: String = "Array[String]()";

    if (message.PrimaryKeys != null && message.PrimaryKeys.size > 0) {
      primaryKeysGen.append("{" + msgConstants.newline)
      primaryKeysGen.append(msgConstants.primaryKeyVar.format(msgConstants.pad2, msgConstants.newline))
      primaryKeysGen.append(msgConstants.pad2 + "try {" + msgConstants.newline)
      message.PrimaryKeys.foreach(key => {
        message.Elements.foreach(element => {
          if (element.Name.equalsIgnoreCase(key)) {
            primaryKeysGen.append("%s primaryKeys += %s.toString(get(\"%s\").getValue.asInstanceOf[%s]);%s".format(msgConstants.pad2, element.FldMetaataType.implementationName, element.Name.toLowerCase(), element.FieldTypePhysicalName, msgConstants.newline)) //"+ com.ligadata.BaseTypes.StringImpl+".toString(get"+element.Name.capitalize+") ")
          }
        })
      })
      primaryKeysGen.append("%s }".format(msgConstants.pad2))
      primaryKeysGen.append(msgConstants.catchStmt)
      primaryKeysGen.append("%s primaryKeys.toArray; %s".format(msgConstants.pad2, msgConstants.newline))
      primaryKeysGen.append("%s ".format(msgConstants.newline))
      primaryKeysGen.append("%s} %s".format(msgConstants.pad2, msgConstants.newline))
      returnPrimaryKeyStr = msgConstants.primaryKeyData.format(msgConstants.pad2, primaryKeysGen.toString)
    } else {
      returnPrimaryKeyStr = msgConstants.primaryKeyData.format(msgConstants.pad2, arryOfStr)
    }
    returnPrimaryKeyStr
  }

  /*
   * Builder method in the message definition class
   */

  private def builderMethod(): String = {
    """
    def newBuilder(): Builder = {
    	return new Builder();
    }
    
    """

  }

  /*
   * Generate the schema String of the input message/container
   */
  private def generateSchema(message: Message): String = {

    return "val schema: String = \" " + message.Schema + " \" ;"

  }

  /* 
 * SetByName for the mapped messages
 */
  private def setByNameFuncForMappedMsgs(): String = {

    return ""
  }

  /*
   * SetByName for the Fixed Messages
   */
  private def setByNameFuncForFixedMsgs(): String = {

    return ""
  }

  /*
   * GetByName for mapped Messages
   */
  private def getByNameFuncForMapped(): String = {

    return ""
  }

  /*
   * GetByName merthod for Fixed Messages
   */
  private def getByNameFuncForFixed: String = {

    return ""

  }

  private def partitionKeys(message: Message): String = {

    var paritionKeysStr: String = ""
    if (message.PartitionKeys == null || message.PartitionKeys.size == 0) {
      paritionKeysStr = "val partitionKeys: Array[String] = Array[String](); ";
    } else {
      paritionKeysStr = "val partitionKeys = Array(" + message.PartitionKeys.map(p => { " \"" + p.toLowerCase + "\"" }).mkString(", ") + ");";

    }

    return paritionKeysStr

  }

  private def primaryKeys(message: Message): String = {

    var primaryKeysStr: String = ""
    if (message.PrimaryKeys == null || message.PrimaryKeys.size == 0) {
      primaryKeysStr = "val primaryKeys: Array[String] = Array[String](); ";
    } else {
      primaryKeysStr = "val primaryKeys: Array[String] = Array(" + message.PrimaryKeys.map(p => { "\"" + p.toLowerCase + "\"" }).mkString(", ") + ");";

    }

    return primaryKeysStr

  }

  private def hasPrimaryKeysFunc() = {
    """
  override def hasPrimaryKey(): Boolean = {
    if (primaryKeys == null) return false;
    (primaryKeys.size > 0);
  }
  """
  }

  private def hasPartitionKeyFunc() = {
    """
  override def hasPartitionKey(): Boolean = {
    if (partitionKeys == null) return false;
    (partitionKeys.size > 0);
  }
  """
  }

  /*
   * handling system colums in Mapped Messages
   */

  private def getSystemColumns(): String = {

    return ""

  }

  /*
   * set method with key as arguments
   */

  private def getSetByName(): String = {

    return ""

  }

  /*
   * Get By String - Fixed Messages
   */

  private def getByStringhFixed(message: Message): String = {
    """
    override def get(key: String): AttributeValue = {
    try {
      // Try with reflection
      return getWithReflection(key.toLowerCase())
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        // Call By Name
        return getByName(key.toLowerCase())
        }
      }
    }      
    """
  }

  /*
   * GetBy Name method for fixed messages
   */
  private def getByName(message: Message): String = {
    """
    private def getByName(key: String): AttributeValue = {
    try {
      if (!keyTypes.contains(key)) throw new Exception("Key does not exists");
      var attributeValue = new AttributeValue();
  """ + getByNameStr(message) + """
     
      attributeValue.setValueType(keyTypes(key.toLowerCase()));
      return attributeValue;
    } """ + msgConstants.catchStmt + """
  }       
  """
  }

  /*
   * GetByName Str - For Fixed Messages
   */
  private def getByNameStr(message: Message): String = {
    if (message.Elements == null)
      return "";
    var keysStr = new StringBuilder(8 * 1024)
    try {
      message.Elements.foreach(field => {
        keysStr.append("%sif (key.equals(\"%s\")) { attributeValue.setValue(this.%s); }%s".format(msgConstants.pad3, field.Name, field.Name, msgConstants.newline));
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    keysStr.toString()
  }

  /*
   * Get With Reflection
   */
  private def getWithReflection(msg: Message): String = {
    """
    private def getWithReflection(key: String): AttributeValue = {
      var attributeValue = new AttributeValue();
      val ru = scala.reflect.runtime.universe
      val m = ru.runtimeMirror(getClass.getClassLoader)
      val im = m.reflect(this)
      val fieldX = ru.typeOf[""" + msg.Name + """].declaration(ru.newTermName(key)).asTerm.accessed.asTerm
      val fmX = im.reflectField(fieldX)
      attributeValue.setValue(fmX.get);
      attributeValue.setValueType(keyTypes(key))
      attributeValue
    } 
   """
  }

  /*
   * getAttributeNameAndValueIterator for Fixed messages to retrieve all attributes of message
   */

  private def getAttributeNameAndValueIterator = {
    """
    override def getAttributeNameAndValueIterator(): java.util.Iterator[java.util.Map.Entry[String, AttributeValue]] = {
      getAllAttributeValues.entrySet().iterator();
    }
    """
  }

  /*
     * Set By Key - Fixed Messages
     */
  private def setByKeyFunc(message: Message): String = {

    """
    override def set(key: String, value: Any) = {
    try {
   
  """ + setByKeyFuncStr(message) + """
      }""" + msgConstants.catchStmt + """
    }
  """
  }

  /*
   * SetBy KeynameFunc - Generation
   */
  private def setByKeyFuncStr(message: Message): String = {
    if (message.Elements == null)
      return "";
    var keysStr = new StringBuilder(8 * 1024)
    try {
      message.Elements.foreach(field => {
        keysStr.append("%sif (key.equals(\"%s\")) { this.%s = value.asInstanceOf[%s]; }%s".format(msgConstants.pad3, field.Name, field.Name, field.FieldTypePhysicalName, msgConstants.newline));
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    keysStr.toString()
  }

  /*
   * Set Value Type and Value By  atrribute name
   */
  private def setValueAndValueTypeByKeyFunc = {
    """
    override def set(key: String, value: Any, valTyp: String) = {
      throw new Exception ("Set Func for Value and ValueType By Key is not supported for Fixed Messages" )
    }
  """
  }

  private def keyTypesStr(fields: List[Element]): String = {
    var keysStr = new StringBuilder(8 * 1024)
    try {
      fields.foreach(field => {
        keysStr.append("\"" + field.Name + "\"-> \"" + field.FieldTypePhysicalName + "\",")
      })
    } catch {
      case e: Exception => {
        log.debug("", e)
        throw e
      }
    }
    keysStr.toString()
  }

  private def keyTypesMap(fields: List[Element]): String = {
    val keysStr = keyTypesStr(fields);
    if (keysStr == null || keysStr.trim == "" || keysStr.length() < 2)
      return "%sprivate var keyTypes = Map[String, String]();%s".format(msgConstants.pad2, msgConstants.newline)
    return "%sprivate var keyTypes = Map(%s);%s".format(msgConstants.pad2, keysStr.substring(0, keysStr.length() - 1), msgConstants.newline);
  }

  /*
   * Set Method Generation Function for Fixed Messages
   */
  private def setFuncGeneration(fields: List[Element]): String = {
    var setMethod = new StringBuilder(8 * 1024)
    var setmethodStr: String = ""
    try {
      fields.foreach(field => {
        setmethodStr = """
        def set""" + field.Name.capitalize + """(value: """ + field.FieldTypePhysicalName + """): Unit = {
        	this.""" + field.Name + """ = value;
        }
        """
        setMethod = setMethod.append(setmethodStr.toString())
      })
    } catch {
      case e: Exception => {
        val stackTrace = StackTrace.ThrowableTraceString(e)
        log.debug("StackTrace:" + stackTrace)
        throw e
      }
    }
    return setMethod.toString
  }

  /*var typstring = field.ttyp.get.implementationName
      
      
     

        keysStr.append("(\"" + f.Name + "\", " + mappedTypesABuf.indexOf(typstring) + "),")
        
       */

  /*
    def getKeysStr(keysStr: String) = {
    if (keysStr != null && keysStr.trim() != "") {

      """ 
      var keys = Map(""" + keysStr.toString.substring(0, keysStr.toString.length - 1) + ") \n " +
        """
      var fields: scala.collection.mutable.Map[String, (Int, Any)] = new scala.collection.mutable.HashMap[String, (Int, Any)];
 	"""
    } else {
      """ 
	    var keys = Map[String, Int]()
	    var fields: scala.collection.mutable.Map[String, (Int, Any)] = new scala.collection.mutable.HashMap[String, (Int, Any)];
	  
	  """
    }
  }
    */
}