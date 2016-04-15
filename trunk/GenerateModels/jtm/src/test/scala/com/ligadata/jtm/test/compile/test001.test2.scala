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
package com.ligadata.jtm.test.filter3
import com.ligadata.KamanjaBase._
import com.ligadata.KvBase.TimeRange
import com.ligadata.kamanja.metadata.ModelDef
import com.ligadata.Utils._
import com.ligadata.runtime.Conversion
class Factory(modelDef: ModelDef, nodeContext: NodeContext) extends ModelInstanceFactory(modelDef, nodeContext) {
  override def isValidMessage(msg: MessageContainerBase): Boolean = {
    msg.isInstanceOf[com.ligadata.kamanja.test.V1000000.msg1]
  }
  override def createModelInstance(): ModelInstance = return new Model(this)
  override def getModelName: String = "com.ligadata.jtm.test.filter1"
  override def getVersion: String = "0.0.1"
  override def createResultObject(): ModelResultBase = new MappedModelResults()
}
class Model(factory: ModelInstanceFactory) extends ModelInstance(factory) {
  val conversion = new com.ligadata.runtime.Conversion
  override def execute(txnCtxt: TransactionContext, execMsgsSet: Array[ContainerOrConcept], triggerdSetIndex: Int, outputDefault: Boolean): Array[ContainerOrConcept] = {
    val messagefactoryinterface = execMsgsSet(0).asInstanceOf[MessageFactoryInterface]
    //
    //
    def exeGenerated_test1_1(msg1: com.ligadata.kamanja.test.V1000000.msg1): Array[MessageInterface] = {
      // conversion to string
      val instr: String = conversion.ToString(msg1.in1)
      // conversion to int
      val inint: Int = conversion.ToInteger(instr)
      // in scala, type could be optional
      val out3: Int = inint + 1000
      def process_o1(): Array[MessageInterface] = {
        if (!(msg1.in2 != -1 && msg1.in2 < 100)) return Array.empty[MessageInterface]
        val t1: String = "s:" + msg1.in2.toString()
        val result = new com.ligadata.kamanja.test.V1000000.msg2(messagefactoryinterface)
        result.out4 = msg1.in3
        result.out3 = msg1.in2
        result.out2 = t1
        result.out1 = msg1.in1
        Array(result)
      }
      process_o1()
    }
    // Evaluate messages
    val msgs = execMsgsSet.map(m => m.getFullTypeName -> m).toMap
    val msg1 = msgs.get("com.ligadata.kamanja.test.msg1").getOrElse(null).asInstanceOf[com.ligadata.kamanja.test.V1000000.msg1]
    // Main dependency -> execution check
    //
    val results: Array[MessageInterface] =
      if(msg1!=null) {
        exeGenerated_test1_1(msg1)
      } else {
        Array.empty[MessageInterface]
      }
    results.asInstanceOf[Array[ContainerOrConcept]]
  }
}