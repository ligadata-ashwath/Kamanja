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
package com.ligadata.models.samples.finance.V1
import com.ligadata.KamanjaBase._
import com.ligadata.KvBase.TimeRange
import com.ligadata.kamanja.metadata.ModelDef
import com.ligadata.runtime.Conversion
class TransactionIngestFactory(modelDef: ModelDef, nodeContext: NodeContext) extends ModelInstanceFactory(modelDef, nodeContext) {
  override def createModelInstance(): ModelInstance = return new TransactionIngest(this)
  override def getModelName: String = "com.ligadata.models.samples.finance"
  override def getVersion: String = "0.0.1"
  override def createResultObject(): ModelResultBase = new MappedModelResults()
}
class TransactionIngest(factory: ModelInstanceFactory) extends ModelInstance(factory) {
  val conversion = new com.ligadata.runtime.Conversion
  override def execute(txnCtxt: TransactionContext, execMsgsSet: Array[ContainerOrConcept], triggerdSetIndex: Int, outputDefault: Boolean): Array[ContainerOrConcept] = {
    //
    //
    def exeGenerated_transactionmsg_1(msg1: com.ligadata.kamanja.samples.messages.V1000000.TransactionMsgIn): Array[MessageInterface] = {
      // Split the incoming data
      val arrayData: Array[String] = msg1.data.split(",")
      def process_o1(): Array[MessageInterface] = {
        // extract the type
        val typeName: String = arrayData(0)
        if (!("com.ligadata.kamanja.samples.messages.TransactionMsg" == typeName)) return Array.empty[MessageInterface]
        val result = com.ligadata.kamanja.samples.messages.V1000000.TransactionMsg.createInstance
        result.branchid = conversion.ToInteger(arrayData(2))
        result.custid = conversion.ToLong(arrayData(1))
        result.locationid = conversion.ToInteger(arrayData(8))
        result.balance = conversion.ToDouble(arrayData(5))
        result.amount = conversion.ToDouble(arrayData(4))
        result.transtype = arrayData(9)
        result.accno = conversion.ToLong(arrayData(3))
        result.date = conversion.ToInteger(arrayData(6))
        result.time = conversion.ToInteger(arrayData(7))
        Array(result)
      }
      process_o1()
    }
    // Evaluate messages
    val msgs = execMsgsSet.map(m => m.getFullTypeName -> m).toMap
    val msg1 = msgs.getOrElse("com.ligadata.kamanja.samples.messages.TransactionMsgIn", null).asInstanceOf[com.ligadata.kamanja.samples.messages.V1000000.TransactionMsgIn]
    // Main dependency -> execution check
    //
    val results: Array[MessageInterface] =
      if(msg1!=null) {
        exeGenerated_transactionmsg_1(msg1)
      } else {
        Array.empty[MessageInterface]
      }
    results.asInstanceOf[Array[ContainerOrConcept]]
  }
}
