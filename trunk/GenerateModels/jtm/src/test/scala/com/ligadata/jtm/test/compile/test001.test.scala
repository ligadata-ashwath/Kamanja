/*
* Copyright 2016 ligaDATA
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
package com.ligadata.jtm.test.filter.V1
import com.ligadata.KamanjaBase._
import com.ligadata.KvBase.TimeRange
import com.ligadata.kamanja.metadata.ModelDef
import com.ligadata.runtime.Log
import com.ligadata.Utils._
import com.ligadata.runtime.Conversion
class ModelFactory(modelDef: ModelDef, nodeContext: NodeContext) extends ModelInstanceFactory(modelDef, nodeContext) {
  override def createModelInstance(txnCtxt: com.ligadata.KamanjaBase.TransactionContext): ModelInstance = return new Model(this)
  override def getModelName: String = "com.ligadata.jtm.test.filter.Model"
  override def getVersion: String = "0.0.1"
  override def createResultObject(): ModelResultBase = new MappedModelResults()
}
class Model(factory: ModelInstanceFactory) extends ModelInstance(factory) {
  val conversion = new com.ligadata.runtime.Conversion
  val log = new com.ligadata.runtime.Log(this.getClass.getName)
  val context = new com.ligadata.runtime.JtmContext
  import log._
  // Model code start
  // Model code end
  override def execute(txnCtxt: TransactionContext, execMsgsSet: Array[ContainerOrConcept], triggerdSetIndex: Int, outputDefault: Boolean): Array[ContainerOrConcept] = {
    if (isTraceEnabled)
      Trace(s"Model::execute transid=%d triggeredset=%d outputdefault=%s".format(txnCtxt.transId, triggerdSetIndex, outputDefault.toString))
    if(isDebugEnabled)
    {
      execMsgsSet.foreach(m => Debug( s"Input: %s -> %s".format(m.getFullTypeName, m.toString())))
    }
    // Grok parts
    // Model methods
    def exeGenerated_test1_1(msg1: com.ligadata.kamanja.test.V1000000.msg1): Array[MessageInterface] = {
      Debug("exeGenerated_test1_1")
      context.SetSection("test1")
      // in scala, type could be optional
      val out3: Int = msg1.in1 + 1000
      def process_o1(): Array[MessageInterface] = {
        Debug("exeGenerated_test1_1::process_o1")
        context.SetScope("o1")
        try {
          if (!(!(msg1.in2 != -1 && msg1.in2 < 100))) {
            Debug("Filtered: test1@o1")
            return Array.empty[MessageInterface]
          }
          val t1: String = "s:" + msg1.in2.toString()
          val result = com.ligadata.kamanja.test.V1000000.msg2.createInstance
          result.out4 = msg1.in3
          result.out3 = msg1.in2
          result.out2 = t1
          result.out1 = msg1.in1
          if(context.CurrentErrors()==0) {
            Array(result)
          } else {
            return Array.empty[MessageInterface]
          }
        } catch {
          case e: Exception => {
            context.AddError(e.getMessage)
          }
        }
        return Array.empty[MessageInterface]
      }
      process_o1()
    }
    // Evaluate messages
    val msgs = execMsgsSet.map(m => m.getFullTypeName -> m).toMap
    val msg1 = msgs.getOrElse("com.ligadata.kamanja.test.msg1", null).asInstanceOf[com.ligadata.kamanja.test.V1000000.msg1]
    // Main dependency -> execution check
    // Create result object
    val results: Array[MessageInterface] =
      (if(msg1!=null) {
        exeGenerated_test1_1(msg1)
      } else {
        Array.empty[MessageInterface]
      }) ++
        Array.empty[MessageInterface]
    if(isDebugEnabled)
    {
      results.foreach(m => Debug( s"Output: %s -> %s".format(m.getFullTypeName, m.toString())))
    }
    results.asInstanceOf[Array[ContainerOrConcept]]
  }
}
