package com.ligadata.tool.generatemessage

import scala.collection.immutable.Map

/**
  * Created by Yousef on 5/15/2016.
  */
class ConfigBean {

  private var _delimiter = ","
  private var _outputPath = ""
  private var _saveMessage = false
  private var _messageType = false  // false -> fixed, true -> mapped
  private var _nameSpace = "com.message"
  private var _partitionKey = ""
  private var _primaryKey = ""
  private var _timePartition = ""
  private var _feilds = Map[String, String]()
  private var _messageName = "Default"
  private var _hasPartitionKey = false
  private var _hasPrimaryKey = false
  private var _hasTimePartition = false

  // Getter
  def delimiter = _delimiter
  def outputPath = _outputPath
  def saveMessage= _saveMessage
  def nameSpace= _nameSpace
  def partitionKey = _partitionKey
  def primaryKey = _primaryKey
  def timePartition = _timePartition
  def messageType = _messageType
  def feilds = _feilds
  def messageName = _messageName
  def hasPartitionKey = _hasPartitionKey
  def hasPrimaryKey = _hasPrimaryKey
  def hasTimePartition = _hasTimePartition

  // Setter
  def delimiter_= (value:String):Unit = _delimiter = value
  def outputPath_= (value:String):Unit = _outputPath = value
  def saveMessage_= (value:Boolean):Unit = _saveMessage = value
  def messageType_= (value:Boolean):Unit = _messageType = value
  def nameSpace_= (value:String):Unit = _nameSpace = value
  def partitionKey_= (value:String):Unit = _partitionKey = value
  def primaryKey_= (value:String):Unit = _primaryKey = value
  def timePartition_= (value:String):Unit = _timePartition = value
  def feilds_= (value:Map[String,String]):Unit = _feilds = value
  def messageName_= (value:String):Unit = _messageName = value
  def hasPartitionKey_= (value:Boolean):Unit = _hasPartitionKey = value
  def hasPrimaryKey_= (value:Boolean):Unit = _hasPrimaryKey = value
  def hasTimePartition_= (value:Boolean):Unit = _hasTimePartition = value
}