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
package phoenix.tests.V1
import com.ligadata.KamanjaBase._
import com.ligadata.KvBase.TimeRange
import com.ligadata.kamanja.metadata.ModelDef
import com.ligadata.runtime._
import com.ligadata.runtime.Conversion
import com.ligadata.runtime.Validation._
import com.ligadata.Utils._
// Package code start
object MsgConstants{
  val conversion = new com.ligadata.runtime.Conversion

  def HandleError(v1: String, v2: String): Unit ={

  }
}

import MsgConstants._
// Package code end
// READ ME BEFORE YOU MAKE CHANGES TO THE INTERFACE
//
// If you adjust the interface here, you need to fix the code generation as well
//
class test_jtmFactory(modelDef: ModelDef, nodeContext: NodeContext) extends ModelInstanceFactory(modelDef, nodeContext) {
  // Factory code start
  // Factory code end
  override def createModelInstance(): ModelInstance = return new test_jtm(this)
  override def getModelName: String = "phoenix.tests.test_jtm"
  override def getVersion: String = "0.0.1"
  override def createResultObject(): ModelResultBase = new MappedModelResults()
  override def isModelInstanceReusable(): Boolean = true;
}
class common_exeGenerated_AirRefillCS5_1_1(conversion: com.ligadata.runtime.Conversion,
                                           log: com.ligadata.runtime.Log,
                                           context: com.ligadata.runtime.JtmContext,
                                           msg1: com.ligadata.messages.V1000000.AirRefillCS5_input) {
  import log._
  // dummy
  val empty_str: String = ""
  // Split the incoming data
  val inpData: Array[String] = msg1.msg.split(",", -1)
  // value for field (offer5thidentifieraft) after checking for its constraitns : type
  val offer5thidentifieraft___val: Double = if(!isDouble(inpData(531), "offer5thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(531));fieldVal}
  // value for field (unitbalance4thaft) after checking for its constraitns : type
  val unitbalance4thaft___val: Double = if(!isDouble(inpData(458), "unitbalance4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(458));fieldVal}
  // value for field (refilldivunits3rdbef) after checking for its constraitns : type
  val refilldivunits3rdbef___val: Double = if(!isDouble(inpData(308), "refilldivunits3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(308));fieldVal}
  // value for field (temporaryserviceclassaft) after checking for its constraitns : type
  val temporaryserviceclassaft___val: Double = if(!isDouble(inpData(194), "temporaryserviceclassaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(194));fieldVal}
  // value for field (clearedaccount1stvalueaft) after checking for its constraitns : type
  val clearedaccount1stvalueaft___val: Double = if(!isDouble(inpData(137), "clearedaccount1stvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(137));fieldVal}
  // value for field (unitbalance5thbef) after checking for its constraitns : type
  val unitbalance5thbef___val: Double = if(!isDouble(inpData(324), "unitbalance5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(324));fieldVal}
  // value for field (rechargedivpartpda9th) after checking for its constraitns : type
  val rechargedivpartpda9th___val: Double = if(!isDouble(inpData(290), "rechargedivpartpda9th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(290));fieldVal}
  // value for field (dedicatedaccountunit7thaft) after checking for its constraitns : type
  val dedicatedaccountunit7thaft___val: Double = if(!isDouble(inpData(475), "dedicatedaccountunit7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(475));fieldVal}
  // value for field (offer1stidentifieraft) after checking for its constraitns : type
  val offer1stidentifieraft___val: Double = if(!isDouble(inpData(503), "offer1stidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(503));fieldVal}
  // value for field (offerproductidentifier9thaft) after checking for its constraitns : type
  val offerproductidentifier9thaft___val: Double = if(!isDouble(inpData(563), "offerproductidentifier9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(563));fieldVal}
  // value for field (maximumservicefeeperiod) after checking for its constraitns : type
  val maximumservicefeeperiod___val: Double = if(!isDouble(inpData(229), "maximumservicefeeperiod", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(229));fieldVal}
  // value for field (dedicatedaccount5thidbef) after checking for its constraitns : type
  val dedicatedaccount5thidbef___val: Double = if(!isDouble(inpData(57), "dedicatedaccount5thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(57));fieldVal}
  // value for field (temporaryservclasexpirydateaft) after checking for its constraitns : type
  val temporaryservclasexpirydateaft___val: Double = if(!isDouble(inpData(195), "temporaryservclasexpirydateaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(195));fieldVal}
  // value for field (account5threfilldivamountaft) after checking for its constraitns : type
  val account5threfilldivamountaft___val: Double = if(!isDouble(inpData(158), "account5threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(158));fieldVal}
  // value for field (account9threfilldivamountaft) after checking for its constraitns : type
  val account9threfilldivamountaft___val: Double = if(!isDouble(inpData(182), "account9threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(182));fieldVal}
  // value for field (account10threfilpromdivamntbef) after checking for its constraitns : type
  val account10threfilpromdivamntbef___val: Double = if(!isDouble(inpData(90), "account10threfilpromdivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(90));fieldVal}
  // value for field (account1strefillpromdivamntbef) after checking for its constraitns : type
  val account1strefillpromdivamntbef___val: Double = if(!isDouble(inpData(36), "account1strefillpromdivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(36));fieldVal}
  // value for field (refillpromodivunits5thbef) after checking for its constraitns : type
  val refillpromodivunits5thbef___val: Double = if(!isDouble(inpData(323), "refillpromodivunits5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(323));fieldVal}
  // value for field (clearedaccount10thvalueaft) after checking for its constraitns : type
  val clearedaccount10thvalueaft___val: Double = if(!isDouble(inpData(191), "clearedaccount10thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(191));fieldVal}
  // value for field (dedicatedaccount10thidaft) after checking for its constraitns : type
  val dedicatedaccount10thidaft___val: Double = if(!isDouble(inpData(186), "dedicatedaccount10thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(186));fieldVal}
  // value for field (unitbalance7thbef) after checking for its constraitns : type
  val unitbalance7thbef___val: Double = if(!isDouble(inpData(338), "unitbalance7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(338));fieldVal}
  // value for field (clearedunits1stbef) after checking for its constraitns : type
  val clearedunits1stbef___val: Double = if(!isDouble(inpData(297), "clearedunits1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(297));fieldVal}
  // value for field (account1strefilpromodivamntaft) after checking for its constraitns : type
  val account1strefilpromodivamntaft___val: Double = if(!isDouble(inpData(135), "account1strefilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(135));fieldVal}
  // value for field (servicefeedayspromopart) after checking for its constraitns : type
  val servicefeedayspromopart___val: Double = if(!isDouble(inpData(227), "servicefeedayspromopart", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(227));fieldVal}
  // value for field (usageaccumulator2ndvaluebef) after checking for its constraitns : type
  val usageaccumulator2ndvaluebef___val: Double = if(!isDouble(inpData(105), "usageaccumulator2ndvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(105));fieldVal}
  // value for field (usageaccumulator9thvaluebef) after checking for its constraitns : type
  val usageaccumulator9thvaluebef___val: Double = if(!isDouble(inpData(119), "usageaccumulator9thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(119));fieldVal}
  // value for field (account10thbalancebef) after checking for its constraitns : type
  val account10thbalancebef___val: Double = if(!isDouble(inpData(91), "account10thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(91));fieldVal}
  // value for field (usageaccumulator1stidaft) after checking for its constraitns : type
  val usageaccumulator1stidaft___val: Double = if(!isDouble(inpData(201), "usageaccumulator1stidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(201));fieldVal}
  // value for field (account6threfilpromodivamntaft) after checking for its constraitns : type
  val account6threfilpromodivamntaft___val: Double = if(!isDouble(inpData(165), "account6threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(165));fieldVal}
  // value for field (account8thcampaignidentaft) after checking for its constraitns : type
  val account8thcampaignidentaft___val: Double = if(!isDouble(inpData(175), "account8thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(175));fieldVal}
  // value for field (rechargedivpartda3rd) after checking for its constraitns : type
  val rechargedivpartda3rd___val: Double = if(!isDouble(inpData(272), "rechargedivpartda3rd", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(272));fieldVal}
  // value for field (refillpromodivunits3rdaft) after checking for its constraitns : type
  val refillpromodivunits3rdaft___val: Double = if(!isDouble(inpData(450), "refillpromodivunits3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(450));fieldVal}
  // value for field (refilldivunits10thaft) after checking for its constraitns : type
  val refilldivunits10thaft___val: Double = if(!isDouble(inpData(498), "refilldivunits10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(498));fieldVal}
  // value for field (realmoneyflag5thbef) after checking for its constraitns : type
  val realmoneyflag5thbef___val: Double = if(!isDouble(inpData(326), "realmoneyflag5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(326));fieldVal}
  // value for field (welcomestatus) after checking for its constraitns : type
  val welcomestatus___val: Double = if(!isDouble(inpData(232), "welcomestatus", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(232));fieldVal}
  // value for field (temporaryserviceclassbef) after checking for its constraitns : type
  val temporaryserviceclassbef___val: Double = if(!isDouble(inpData(95), "temporaryserviceclassbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(95));fieldVal}
  // value for field (realmoneyflag5thaft) after checking for its constraitns : type
  val realmoneyflag5thaft___val: Double = if(!isDouble(inpData(467), "realmoneyflag5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(467));fieldVal}
  // value for field (clearedunits5thaft) after checking for its constraitns : type
  val clearedunits5thaft___val: Double = if(!isDouble(inpData(466), "clearedunits5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(466));fieldVal}
  // value for field (dedicatedaccount7thidaft) after checking for its constraitns : type
  val dedicatedaccount7thidaft___val: Double = if(!isDouble(inpData(168), "dedicatedaccount7thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(168));fieldVal}
  // value for field (account4threfilpromodivamntbef) after checking for its constraitns : type
  val account4threfilpromodivamntbef___val: Double = if(!isDouble(inpData(54), "account4threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(54));fieldVal}
  // value for field (dedicatedaccount9thidaft) after checking for its constraitns : type
  val dedicatedaccount9thidaft___val: Double = if(!isDouble(inpData(180), "dedicatedaccount9thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(180));fieldVal}
  // value for field (rechargedivpartpmain) after checking for its constraitns : type
  val rechargedivpartpmain___val: Double = if(!isDouble(inpData(276), "rechargedivpartpmain", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(276));fieldVal}
  // value for field (usageaccumulator7thvaluebef) after checking for its constraitns : type
  val usageaccumulator7thvaluebef___val: Double = if(!isDouble(inpData(115), "usageaccumulator7thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(115));fieldVal}
  // value for field (clearedaccount4thvaluebef) after checking for its constraitns : type
  val clearedaccount4thvaluebef___val: Double = if(!isDouble(inpData(56), "clearedaccount4thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(56));fieldVal}
  // value for field (clearedaccount7thvalueaft) after checking for its constraitns : type
  val clearedaccount7thvalueaft___val: Double = if(!isDouble(inpData(173), "clearedaccount7thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(173));fieldVal}
  // value for field (currentserviceclass) after checking for its constraitns : type
  val currentserviceclass___val: Double = if(!isDouble(inpData(9), "currentserviceclass", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(9));fieldVal}
  // value for field (usageaccumulator3rdvaluebef) after checking for its constraitns : type
  val usageaccumulator3rdvaluebef___val: Double = if(!isDouble(inpData(107), "usageaccumulator3rdvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(107));fieldVal}
  // value for field (account8threfilldivamountaft) after checking for its constraitns : type
  val account8threfilldivamountaft___val: Double = if(!isDouble(inpData(176), "account8threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(176));fieldVal}
  // value for field (refillpromodivunits1stbef) after checking for its constraitns : type
  val refillpromodivunits1stbef___val: Double = if(!isDouble(inpData(295), "refillpromodivunits1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(295));fieldVal}
  // value for field (account7thbalancebef) after checking for its constraitns : type
  val account7thbalancebef___val: Double = if(!isDouble(inpData(73), "account7thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(73));fieldVal}
  // value for field (refillpromodivunits8thaft) after checking for its constraitns : type
  val refillpromodivunits8thaft___val: Double = if(!isDouble(inpData(485), "refillpromodivunits8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(485));fieldVal}
  // value for field (localsequencenumber) after checking for its constraitns : type
  val localsequencenumber___val: Double = if(!isDouble(inpData(7), "localsequencenumber", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(7));fieldVal}
  // value for field (refillpromodivunits5thaft) after checking for its constraitns : type
  val refillpromodivunits5thaft___val: Double = if(!isDouble(inpData(464), "refillpromodivunits5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(464));fieldVal}
  // value for field (usageaccumulator5thvaluebef) after checking for its constraitns : type
  val usageaccumulator5thvaluebef___val: Double = if(!isDouble(inpData(111), "usageaccumulator5thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(111));fieldVal}
  // value for field (account7thcampaignidentbef) after checking for its constraitns : type
  val account7thcampaignidentbef___val: Double = if(!isDouble(inpData(70), "account7thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(70));fieldVal}
  // value for field (unitbalance3rdbef) after checking for its constraitns : type
  val unitbalance3rdbef___val: Double = if(!isDouble(inpData(310), "unitbalance3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(310));fieldVal}
  // value for field (refilldivunits7thaft) after checking for its constraitns : type
  val refilldivunits7thaft___val: Double = if(!isDouble(inpData(477), "refilldivunits7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(477));fieldVal}
  // value for field (clearedunits7thbef) after checking for its constraitns : type
  val clearedunits7thbef___val: Double = if(!isDouble(inpData(339), "clearedunits7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(339));fieldVal}
  // value for field (refillpromodivunits2ndbef) after checking for its constraitns : type
  val refillpromodivunits2ndbef___val: Double = if(!isDouble(inpData(302), "refillpromodivunits2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(302));fieldVal}
  // value for field (refilldivunits8thbef) after checking for its constraitns : type
  val refilldivunits8thbef___val: Double = if(!isDouble(inpData(343), "refilldivunits8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(343));fieldVal}
  // value for field (account8thcampaignidentbef) after checking for its constraitns : type
  val account8thcampaignidentbef___val: Double = if(!isDouble(inpData(76), "account8thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(76));fieldVal}
  // value for field (clearedunits8thbef) after checking for its constraitns : type
  val clearedunits8thbef___val: Double = if(!isDouble(inpData(346), "clearedunits8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(346));fieldVal}
  // value for field (refillpromodivunits4thaft) after checking for its constraitns : type
  val refillpromodivunits4thaft___val: Double = if(!isDouble(inpData(457), "refillpromodivunits4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(457));fieldVal}
  // value for field (dedicatedaccountunit5thaft) after checking for its constraitns : type
  val dedicatedaccountunit5thaft___val: Double = if(!isDouble(inpData(461), "dedicatedaccountunit5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(461));fieldVal}
  // value for field (account10threfilldivamountbef) after checking for its constraitns : type
  val account10threfilldivamountbef___val: Double = if(!isDouble(inpData(89), "account10threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(89));fieldVal}
  // value for field (account7threfilldivamountbef) after checking for its constraitns : type
  val account7threfilldivamountbef___val: Double = if(!isDouble(inpData(71), "account7threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(71));fieldVal}
  // value for field (refilldivunits6thbef) after checking for its constraitns : type
  val refilldivunits6thbef___val: Double = if(!isDouble(inpData(329), "refilldivunits6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(329));fieldVal}
  // value for field (usageaccumulator6thidaft) after checking for its constraitns : type
  val usageaccumulator6thidaft___val: Double = if(!isDouble(inpData(211), "usageaccumulator6thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(211));fieldVal}
  // value for field (usageaccumulator2ndvalueaft) after checking for its constraitns : type
  val usageaccumulator2ndvalueaft___val: Double = if(!isDouble(inpData(204), "usageaccumulator2ndvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(204));fieldVal}
  // value for field (account1stbalancebef) after checking for its constraitns : type
  val account1stbalancebef___val: Double = if(!isDouble(inpData(37), "account1stbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(37));fieldVal}
  // value for field (clearedaccount6thvalueaft) after checking for its constraitns : type
  val clearedaccount6thvalueaft___val: Double = if(!isDouble(inpData(167), "clearedaccount6thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(167));fieldVal}
  // value for field (realmoneyflag8thaft) after checking for its constraitns : type
  val realmoneyflag8thaft___val: Double = if(!isDouble(inpData(488), "realmoneyflag8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(488));fieldVal}
  // value for field (permanentserviceclassbef) after checking for its constraitns : type
  val permanentserviceclassbef___val: Double = if(!isDouble(inpData(94), "permanentserviceclassbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(94));fieldVal}
  // value for field (offerproductidentifier7thaft) after checking for its constraitns : type
  val offerproductidentifier7thaft___val: Double = if(!isDouble(inpData(549), "offerproductidentifier7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(549));fieldVal}
  // value for field (dedicatedaccountunit3rdbef) after checking for its constraitns : type
  val dedicatedaccountunit3rdbef___val: Double = if(!isDouble(inpData(306), "dedicatedaccountunit3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(306));fieldVal}
  // value for field (usageaccumulator9thidbef) after checking for its constraitns : type
  val usageaccumulator9thidbef___val: Double = if(!isDouble(inpData(118), "usageaccumulator9thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(118));fieldVal}
  // value for field (usageaccumulator1stvaluebef) after checking for its constraitns : type
  val usageaccumulator1stvaluebef___val: Double = if(!isDouble(inpData(103), "usageaccumulator1stvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(103));fieldVal}
  // value for field (realmoneyflag10thaft) after checking for its constraitns : type
  val realmoneyflag10thaft___val: Double = if(!isDouble(inpData(502), "realmoneyflag10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(502));fieldVal}
  // value for field (offer10thidentifieraft) after checking for its constraitns : type
  val offer10thidentifieraft___val: Double = if(!isDouble(inpData(566), "offer10thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(566));fieldVal}
  // value for field (account9thbalancebef) after checking for its constraitns : type
  val account9thbalancebef___val: Double = if(!isDouble(inpData(85), "account9thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(85));fieldVal}
  // value for field (refillpromodivunits9thbef) after checking for its constraitns : type
  val refillpromodivunits9thbef___val: Double = if(!isDouble(inpData(351), "refillpromodivunits9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(351));fieldVal}
  // value for field (offerproductidentifier1stbef) after checking for its constraitns : type
  val offerproductidentifier1stbef___val: Double = if(!isDouble(inpData(366), "offerproductidentifier1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(366));fieldVal}
  // value for field (account5threfilpromodivamntaft) after checking for its constraitns : type
  val account5threfilpromodivamntaft___val: Double = if(!isDouble(inpData(159), "account5threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(159));fieldVal}
  // value for field (accumulatedrefillcounteraft) after checking for its constraitns : type
  val accumulatedrefillcounteraft___val: Double = if(!isDouble(inpData(128), "accumulatedrefillcounteraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(128));fieldVal}
  // value for field (clearedaccount7thvaluebef) after checking for its constraitns : type
  val clearedaccount7thvaluebef___val: Double = if(!isDouble(inpData(74), "clearedaccount7thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(74));fieldVal}
  // value for field (clearedaccount5thvalueaft) after checking for its constraitns : type
  val clearedaccount5thvalueaft___val: Double = if(!isDouble(inpData(161), "clearedaccount5thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(161));fieldVal}
  // value for field (account10thbalanceaft) after checking for its constraitns : type
  val account10thbalanceaft___val: Double = if(!isDouble(inpData(190), "account10thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(190));fieldVal}
  // value for field (realmoneyflag7thbef) after checking for its constraitns : type
  val realmoneyflag7thbef___val: Double = if(!isDouble(inpData(340), "realmoneyflag7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(340));fieldVal}
  // value for field (realmoneyflag8thbef) after checking for its constraitns : type
  val realmoneyflag8thbef___val: Double = if(!isDouble(inpData(347), "realmoneyflag8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(347));fieldVal}
  // value for field (rechargedivpartda1st) after checking for its constraitns : type
  val rechargedivpartda1st___val: Double = if(!isDouble(inpData(270), "rechargedivpartda1st", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(270));fieldVal}
  // value for field (account4thbalancebef) after checking for its constraitns : type
  val account4thbalancebef___val: Double = if(!isDouble(inpData(55), "account4thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(55));fieldVal}
  // value for field (offer9thidentifieraft) after checking for its constraitns : type
  val offer9thidentifieraft___val: Double = if(!isDouble(inpData(559), "offer9thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(559));fieldVal}
  // value for field (account4thcampaignidentaft) after checking for its constraitns : type
  val account4thcampaignidentaft___val: Double = if(!isDouble(inpData(151), "account4thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(151));fieldVal}
  // value for field (creditclearanceperiodbef) after checking for its constraitns : type
  val creditclearanceperiodbef___val: Double = if(!isDouble(inpData(32), "creditclearanceperiodbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(32));fieldVal}
  // value for field (refilldivunits6thaft) after checking for its constraitns : type
  val refilldivunits6thaft___val: Double = if(!isDouble(inpData(470), "refilldivunits6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(470));fieldVal}
  // value for field (usageaccumulator4thvaluebef) after checking for its constraitns : type
  val usageaccumulator4thvaluebef___val: Double = if(!isDouble(inpData(109), "usageaccumulator4thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(109));fieldVal}
  // value for field (account1stcampaignidentbef) after checking for its constraitns : type
  val account1stcampaignidentbef___val: Double = if(!isDouble(inpData(34), "account1stcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(34));fieldVal}
  // value for field (offerproductidentifier4thbef) after checking for its constraitns : type
  val offerproductidentifier4thbef___val: Double = if(!isDouble(inpData(387), "offerproductidentifier4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(387));fieldVal}
  // value for field (accumulatedprogressionvaluebef) after checking for its constraitns : type
  val accumulatedprogressionvaluebef___val: Double = if(!isDouble(inpData(30), "accumulatedprogressionvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(30));fieldVal}
  // value for field (refillpromodivisionamount) after checking for its constraitns : type
  val refillpromodivisionamount___val: Double = if(!isDouble(inpData(224), "refillpromodivisionamount", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(224));fieldVal}
  // value for field (dedicatedaccountunit5thbef) after checking for its constraitns : type
  val dedicatedaccountunit5thbef___val: Double = if(!isDouble(inpData(320), "dedicatedaccountunit5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(320));fieldVal}
  // value for field (refilldivunits4thaft) after checking for its constraitns : type
  val refilldivunits4thaft___val: Double = if(!isDouble(inpData(456), "refilldivunits4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(456));fieldVal}
  // value for field (dedicatedaccountunit6thbef) after checking for its constraitns : type
  val dedicatedaccountunit6thbef___val: Double = if(!isDouble(inpData(327), "dedicatedaccountunit6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(327));fieldVal}
  // value for field (offer2ndidentifierbef) after checking for its constraitns : type
  val offer2ndidentifierbef___val: Double = if(!isDouble(inpData(369), "offer2ndidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(369));fieldVal}
  // value for field (refilldivunits10thbef) after checking for its constraitns : type
  val refilldivunits10thbef___val: Double = if(!isDouble(inpData(357), "refilldivunits10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(357));fieldVal}
  // value for field (clearedaccount8thvaluebef) after checking for its constraitns : type
  val clearedaccount8thvaluebef___val: Double = if(!isDouble(inpData(80), "clearedaccount8thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(80));fieldVal}
  // value for field (account8threfilpromodivamntaft) after checking for its constraitns : type
  val account8threfilpromodivamntaft___val: Double = if(!isDouble(inpData(177), "account8threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(177));fieldVal}
  // value for field (account5threfilldivamountbef) after checking for its constraitns : type
  val account5threfilldivamountbef___val: Double = if(!isDouble(inpData(59), "account5threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(59));fieldVal}
  // value for field (offer3rdidentifierbef) after checking for its constraitns : type
  val offer3rdidentifierbef___val: Double = if(!isDouble(inpData(376), "offer3rdidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(376));fieldVal}
  // value for field (usageaccumulator9thidaft) after checking for its constraitns : type
  val usageaccumulator9thidaft___val: Double = if(!isDouble(inpData(217), "usageaccumulator9thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(217));fieldVal}
  // value for field (account10thcampaignidentbef) after checking for its constraitns : type
  val account10thcampaignidentbef___val: Double = if(!isDouble(inpData(88), "account10thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(88));fieldVal}
  // value for field (account3rdrefilldivamountaft) after checking for its constraitns : type
  val account3rdrefilldivamountaft___val: Double = if(!isDouble(inpData(146), "account3rdrefilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(146));fieldVal}
  // value for field (account2ndcampaignidentifbef) after checking for its constraitns : type
  val account2ndcampaignidentifbef___val: Double = if(!isDouble(inpData(40), "account2ndcampaignidentifbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(40));fieldVal}
  // value for field (refillpromodivunits4thbef) after checking for its constraitns : type
  val refillpromodivunits4thbef___val: Double = if(!isDouble(inpData(316), "refillpromodivunits4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(316));fieldVal}
  // value for field (refilldivunits2ndaft) after checking for its constraitns : type
  val refilldivunits2ndaft___val: Double = if(!isDouble(inpData(442), "refilldivunits2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(442));fieldVal}
  // value for field (account2ndcampaignidentaft) after checking for its constraitns : type
  val account2ndcampaignidentaft___val: Double = if(!isDouble(inpData(139), "account2ndcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(139));fieldVal}
  // value for field (refilldivunits5thaft) after checking for its constraitns : type
  val refilldivunits5thaft___val: Double = if(!isDouble(inpData(463), "refilldivunits5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(463));fieldVal}
  // value for field (offer3rdidentifieraft) after checking for its constraitns : type
  val offer3rdidentifieraft___val: Double = if(!isDouble(inpData(517), "offer3rdidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(517));fieldVal}
  // value for field (realmoneyflag2ndbef) after checking for its constraitns : type
  val realmoneyflag2ndbef___val: Double = if(!isDouble(inpData(305), "realmoneyflag2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(305));fieldVal}
  // value for field (rechargedivpartda9th) after checking for its constraitns : type
  val rechargedivpartda9th___val: Double = if(!isDouble(inpData(285), "rechargedivpartda9th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(285));fieldVal}
  // value for field (refillpromodivunits6thbef) after checking for its constraitns : type
  val refillpromodivunits6thbef___val: Double = if(!isDouble(inpData(330), "refillpromodivunits6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(330));fieldVal}
  // value for field (usageaccumulator4thvalueaft) after checking for its constraitns : type
  val usageaccumulator4thvalueaft___val: Double = if(!isDouble(inpData(208), "usageaccumulator4thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(208));fieldVal}
  // value for field (realmoneyflag7thaft) after checking for its constraitns : type
  val realmoneyflag7thaft___val: Double = if(!isDouble(inpData(481), "realmoneyflag7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(481));fieldVal}
  // value for field (dedicatedaccount3rdidbef) after checking for its constraitns : type
  val dedicatedaccount3rdidbef___val: Double = if(!isDouble(inpData(45), "dedicatedaccount3rdidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(45));fieldVal}
  // value for field (dedicatedaccount7thidbef) after checking for its constraitns : type
  val dedicatedaccount7thidbef___val: Double = if(!isDouble(inpData(69), "dedicatedaccount7thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(69));fieldVal}
  // value for field (offer8thidentifierbef) after checking for its constraitns : type
  val offer8thidentifierbef___val: Double = if(!isDouble(inpData(411), "offer8thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(411));fieldVal}
  // value for field (serviceremovalgraceperiodaft) after checking for its constraitns : type
  val serviceremovalgraceperiodaft___val: Double = if(!isDouble(inpData(198), "serviceremovalgraceperiodaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(198));fieldVal}
  // value for field (unitbalance10thaft) after checking for its constraitns : type
  val unitbalance10thaft___val: Double = if(!isDouble(inpData(500), "unitbalance10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(500));fieldVal}
  // value for field (account10threfilldivamountaft) after checking for its constraitns : type
  val account10threfilldivamountaft___val: Double = if(!isDouble(inpData(188), "account10threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(188));fieldVal}
  // value for field (account8threfilldivamountbef) after checking for its constraitns : type
  val account8threfilldivamountbef___val: Double = if(!isDouble(inpData(77), "account8threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(77));fieldVal}
  // value for field (rechargedivpartpda2nd) after checking for its constraitns : type
  val rechargedivpartpda2nd___val: Double = if(!isDouble(inpData(278), "rechargedivpartpda2nd", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(278));fieldVal}
  // value for field (usageaccumulator5thidaft) after checking for its constraitns : type
  val usageaccumulator5thidaft___val: Double = if(!isDouble(inpData(209), "usageaccumulator5thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(209));fieldVal}
  // value for field (refilldivunits4thbef) after checking for its constraitns : type
  val refilldivunits4thbef___val: Double = if(!isDouble(inpData(315), "refilldivunits4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(315));fieldVal}
  // value for field (account7threfilldivamountaft) after checking for its constraitns : type
  val account7threfilldivamountaft___val: Double = if(!isDouble(inpData(170), "account7threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(170));fieldVal}
  // value for field (rechargedivpartda2nd) after checking for its constraitns : type
  val rechargedivpartda2nd___val: Double = if(!isDouble(inpData(271), "rechargedivpartda2nd", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(271));fieldVal}
  // value for field (usageaccumulator7thidaft) after checking for its constraitns : type
  val usageaccumulator7thidaft___val: Double = if(!isDouble(inpData(213), "usageaccumulator7thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(213));fieldVal}
  // value for field (dedicatedaccount2ndidaft) after checking for its constraitns : type
  val dedicatedaccount2ndidaft___val: Double = if(!isDouble(inpData(138), "dedicatedaccount2ndidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(138));fieldVal}
  // value for field (offerproductidentifier1staft) after checking for its constraitns : type
  val offerproductidentifier1staft___val: Double = if(!isDouble(inpData(507), "offerproductidentifier1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(507));fieldVal}
  // value for field (offerproductidentifier3rdbef) after checking for its constraitns : type
  val offerproductidentifier3rdbef___val: Double = if(!isDouble(inpData(380), "offerproductidentifier3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(380));fieldVal}
  // value for field (serviceremovalgraceperiodbef) after checking for its constraitns : type
  val serviceremovalgraceperiodbef___val: Double = if(!isDouble(inpData(99), "serviceremovalgraceperiodbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(99));fieldVal}
  // value for field (creditclearanceperiodaft) after checking for its constraitns : type
  val creditclearanceperiodaft___val: Double = if(!isDouble(inpData(131), "creditclearanceperiodaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(131));fieldVal}
  // value for field (dedicatedaccountunit3rdaft) after checking for its constraitns : type
  val dedicatedaccountunit3rdaft___val: Double = if(!isDouble(inpData(447), "dedicatedaccountunit3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(447));fieldVal}
  // value for field (offerproductidentifier7thbef) after checking for its constraitns : type
  val offerproductidentifier7thbef___val: Double = if(!isDouble(inpData(408), "offerproductidentifier7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(408));fieldVal}
  // value for field (realmoneyflag6thaft) after checking for its constraitns : type
  val realmoneyflag6thaft___val: Double = if(!isDouble(inpData(474), "realmoneyflag6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(474));fieldVal}
  // value for field (clearedunits3rdbef) after checking for its constraitns : type
  val clearedunits3rdbef___val: Double = if(!isDouble(inpData(311), "clearedunits3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(311));fieldVal}
  // value for field (aggregatedbalancebef) after checking for its constraitns : type
  val aggregatedbalancebef___val: Double = if(!isDouble(inpData(432), "aggregatedbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(432));fieldVal}
  // value for field (account9thbalanceaft) after checking for its constraitns : type
  val account9thbalanceaft___val: Double = if(!isDouble(inpData(184), "account9thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(184));fieldVal}
  // value for field (unitbalance9thaft) after checking for its constraitns : type
  val unitbalance9thaft___val: Double = if(!isDouble(inpData(493), "unitbalance9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(493));fieldVal}
  // value for field (offerproductidentifier3rdaft) after checking for its constraitns : type
  val offerproductidentifier3rdaft___val: Double = if(!isDouble(inpData(521), "offerproductidentifier3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(521));fieldVal}
  // value for field (offer6thidentifieraft) after checking for its constraitns : type
  val offer6thidentifieraft___val: Double = if(!isDouble(inpData(538), "offer6thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(538));fieldVal}
  // value for field (dedicatedaccount4thidbef) after checking for its constraitns : type
  val dedicatedaccount4thidbef___val: Double = if(!isDouble(inpData(51), "dedicatedaccount4thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(51));fieldVal}
  // value for field (account1stcampaignidentaft) after checking for its constraitns : type
  val account1stcampaignidentaft___val: Double = if(!isDouble(inpData(133), "account1stcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(133));fieldVal}
  // value for field (dedicatedaccountunit10thbef) after checking for its constraitns : type
  val dedicatedaccountunit10thbef___val: Double = if(!isDouble(inpData(355), "dedicatedaccountunit10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(355));fieldVal}
  // value for field (realmoneyflag3rdaft) after checking for its constraitns : type
  val realmoneyflag3rdaft___val: Double = if(!isDouble(inpData(453), "realmoneyflag3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(453));fieldVal}
  // value for field (offer5thidentifierbef) after checking for its constraitns : type
  val offer5thidentifierbef___val: Double = if(!isDouble(inpData(390), "offer5thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(390));fieldVal}
  // value for field (usageaccumulator10thidbef) after checking for its constraitns : type
  val usageaccumulator10thidbef___val: Double = if(!isDouble(inpData(120), "usageaccumulator10thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(120));fieldVal}
  // value for field (unitbalance3rdaft) after checking for its constraitns : type
  val unitbalance3rdaft___val: Double = if(!isDouble(inpData(451), "unitbalance3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(451));fieldVal}
  // value for field (dedicatedaccountunit9thaft) after checking for its constraitns : type
  val dedicatedaccountunit9thaft___val: Double = if(!isDouble(inpData(489), "dedicatedaccountunit9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(489));fieldVal}
  // value for field (dedicatedaccountunit4thaft) after checking for its constraitns : type
  val dedicatedaccountunit4thaft___val: Double = if(!isDouble(inpData(454), "dedicatedaccountunit4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(454));fieldVal}
  // value for field (rechargedivpartpda6th) after checking for its constraitns : type
  val rechargedivpartpda6th___val: Double = if(!isDouble(inpData(287), "rechargedivpartpda6th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(287));fieldVal}
  // value for field (account4thcampaignidentbef) after checking for its constraitns : type
  val account4thcampaignidentbef___val: Double = if(!isDouble(inpData(52), "account4thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(52));fieldVal}
  // value for field (realmoneyflag2ndaft) after checking for its constraitns : type
  val realmoneyflag2ndaft___val: Double = if(!isDouble(inpData(446), "realmoneyflag2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(446));fieldVal}
  // value for field (usageaccumulator2ndidaft) after checking for its constraitns : type
  val usageaccumulator2ndidaft___val: Double = if(!isDouble(inpData(203), "usageaccumulator2ndidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(203));fieldVal}
  // value for field (usageaccumulator5thidbef) after checking for its constraitns : type
  val usageaccumulator5thidbef___val: Double = if(!isDouble(inpData(110), "usageaccumulator5thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(110));fieldVal}
  // value for field (accumulatedrefillvalueaft) after checking for its constraitns : type
  val accumulatedrefillvalueaft___val: Double = if(!isDouble(inpData(127), "accumulatedrefillvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(127));fieldVal}
  // value for field (dedicatedaccount4thidaft) after checking for its constraitns : type
  val dedicatedaccount4thidaft___val: Double = if(!isDouble(inpData(150), "dedicatedaccount4thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(150));fieldVal}
  // value for field (account3rdrefilldivamountbef) after checking for its constraitns : type
  val account3rdrefilldivamountbef___val: Double = if(!isDouble(inpData(47), "account3rdrefilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(47));fieldVal}
  // value for field (account7thbalanceaft) after checking for its constraitns : type
  val account7thbalanceaft___val: Double = if(!isDouble(inpData(172), "account7thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(172));fieldVal}
  // value for field (clearedunits2ndaft) after checking for its constraitns : type
  val clearedunits2ndaft___val: Double = if(!isDouble(inpData(445), "clearedunits2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(445));fieldVal}
  // value for field (account8threfilpromodivamntbef) after checking for its constraitns : type
  val account8threfilpromodivamntbef___val: Double = if(!isDouble(inpData(78), "account8threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(78));fieldVal}
  // value for field (accumulatedprogressionvalueres) after checking for its constraitns : type
  val accumulatedprogressionvalueres___val: Double = if(!isDouble(inpData(275), "accumulatedprogressionvalueres", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(275));fieldVal}
  // value for field (offer7thidentifierbef) after checking for its constraitns : type
  val offer7thidentifierbef___val: Double = if(!isDouble(inpData(404), "offer7thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(404));fieldVal}
  // value for field (realmoneyflag1staft) after checking for its constraitns : type
  val realmoneyflag1staft___val: Double = if(!isDouble(inpData(439), "realmoneyflag1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(439));fieldVal}
  // value for field (realmoneyflag10thbef) after checking for its constraitns : type
  val realmoneyflag10thbef___val: Double = if(!isDouble(inpData(361), "realmoneyflag10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(361));fieldVal}
  // value for field (accumulatedprogressionvalueaft) after checking for its constraitns : type
  val accumulatedprogressionvalueaft___val: Double = if(!isDouble(inpData(129), "accumulatedprogressionvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(129));fieldVal}
  // value for field (clearedunits7thaft) after checking for its constraitns : type
  val clearedunits7thaft___val: Double = if(!isDouble(inpData(480), "clearedunits7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(480));fieldVal}
  // value for field (account7threfilpromodivamntbef) after checking for its constraitns : type
  val account7threfilpromodivamntbef___val: Double = if(!isDouble(inpData(72), "account7threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(72));fieldVal}
  // value for field (accountbalanceaft) after checking for its constraitns : type
  val accountbalanceaft___val: Double = if(!isDouble(inpData(126), "accountbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(126));fieldVal}
  // value for field (clearedunits9thbef) after checking for its constraitns : type
  val clearedunits9thbef___val: Double = if(!isDouble(inpData(353), "clearedunits9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(353));fieldVal}
  // value for field (account6thcampaignidentaft) after checking for its constraitns : type
  val account6thcampaignidentaft___val: Double = if(!isDouble(inpData(163), "account6thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(163));fieldVal}
  // value for field (unitbalance9thbef) after checking for its constraitns : type
  val unitbalance9thbef___val: Double = if(!isDouble(inpData(352), "unitbalance9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(352));fieldVal}
  // value for field (account9threfilpromodivamntbef) after checking for its constraitns : type
  val account9threfilpromodivamntbef___val: Double = if(!isDouble(inpData(84), "account9threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(84));fieldVal}
  // value for field (refilldivunits9thbef) after checking for its constraitns : type
  val refilldivunits9thbef___val: Double = if(!isDouble(inpData(350), "refilldivunits9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(350));fieldVal}
  // value for field (offer6thidentifierbef) after checking for its constraitns : type
  val offer6thidentifierbef___val: Double = if(!isDouble(inpData(397), "offer6thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(397));fieldVal}
  // value for field (realmoneyflag9thaft) after checking for its constraitns : type
  val realmoneyflag9thaft___val: Double = if(!isDouble(inpData(495), "realmoneyflag9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(495));fieldVal}
  // value for field (refillpromodivunits7thbef) after checking for its constraitns : type
  val refillpromodivunits7thbef___val: Double = if(!isDouble(inpData(337), "refillpromodivunits7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(337));fieldVal}
  // value for field (account4threfilldivamountaft) after checking for its constraitns : type
  val account4threfilldivamountaft___val: Double = if(!isDouble(inpData(152), "account4threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(152));fieldVal}
  // value for field (rechargedivpartpda5th) after checking for its constraitns : type
  val rechargedivpartpda5th___val: Double = if(!isDouble(inpData(281), "rechargedivpartpda5th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(281));fieldVal}
  // value for field (dedicatedaccount2ndidbef) after checking for its constraitns : type
  val dedicatedaccount2ndidbef___val: Double = if(!isDouble(inpData(39), "dedicatedaccount2ndidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(39));fieldVal}
  // value for field (offerproductidentifier6thbef) after checking for its constraitns : type
  val offerproductidentifier6thbef___val: Double = if(!isDouble(inpData(401), "offerproductidentifier6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(401));fieldVal}
  // value for field (rechargedivpartda4th) after checking for its constraitns : type
  val rechargedivpartda4th___val: Double = if(!isDouble(inpData(273), "rechargedivpartda4th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(273));fieldVal}
  // value for field (dedicatedaccount5thidaft) after checking for its constraitns : type
  val dedicatedaccount5thidaft___val: Double = if(!isDouble(inpData(156), "dedicatedaccount5thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(156));fieldVal}
  // value for field (account2ndrefilldivamountaft) after checking for its constraitns : type
  val account2ndrefilldivamountaft___val: Double = if(!isDouble(inpData(140), "account2ndrefilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(140));fieldVal}
  // value for field (clearedunits3rdaft) after checking for its constraitns : type
  val clearedunits3rdaft___val: Double = if(!isDouble(inpData(452), "clearedunits3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(452));fieldVal}
  // value for field (dedicatedaccount6thidbef) after checking for its constraitns : type
  val dedicatedaccount6thidbef___val: Double = if(!isDouble(inpData(63), "dedicatedaccount6thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(63));fieldVal}
  // value for field (account4threfilpromodivamntaft) after checking for its constraitns : type
  val account4threfilpromodivamntaft___val: Double = if(!isDouble(inpData(153), "account4threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(153));fieldVal}
  // value for field (usageaccumulator7thvalueaft) after checking for its constraitns : type
  val usageaccumulator7thvalueaft___val: Double = if(!isDouble(inpData(214), "usageaccumulator7thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(214));fieldVal}
  // value for field (refilldivunits5thbef) after checking for its constraitns : type
  val refilldivunits5thbef___val: Double = if(!isDouble(inpData(322), "refilldivunits5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(322));fieldVal}
  // value for field (offer10thidentifierbef) after checking for its constraitns : type
  val offer10thidentifierbef___val: Double = if(!isDouble(inpData(425), "offer10thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(425));fieldVal}
  // value for field (account2ndbalancebef) after checking for its constraitns : type
  val account2ndbalancebef___val: Double = if(!isDouble(inpData(43), "account2ndbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(43));fieldVal}
  // value for field (offerproductidentifier10thaft) after checking for its constraitns : type
  val offerproductidentifier10thaft___val: Double = if(!isDouble(inpData(570), "offerproductidentifier10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(570));fieldVal}
  // value for field (account3rdrefilpromodivamntbef) after checking for its constraitns : type
  val account3rdrefilpromodivamntbef___val: Double = if(!isDouble(inpData(48), "account3rdrefilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(48));fieldVal}
  // value for field (usageaccumulator6thvalueaft) after checking for its constraitns : type
  val usageaccumulator6thvalueaft___val: Double = if(!isDouble(inpData(212), "usageaccumulator6thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(212));fieldVal}
  // value for field (account1strefilldivamountbef) after checking for its constraitns : type
  val account1strefilldivamountbef___val: Double = if(!isDouble(inpData(35), "account1strefilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(35));fieldVal}
  // value for field (rechargedivpartpda8th) after checking for its constraitns : type
  val rechargedivpartpda8th___val: Double = if(!isDouble(inpData(289), "rechargedivpartpda8th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(289));fieldVal}
  // value for field (unitbalance2ndaft) after checking for its constraitns : type
  val unitbalance2ndaft___val: Double = if(!isDouble(inpData(444), "unitbalance2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(444));fieldVal}
  // value for field (refillpromodivunits10thaft) after checking for its constraitns : type
  val refillpromodivunits10thaft___val: Double = if(!isDouble(inpData(499), "refillpromodivunits10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(499));fieldVal}
  // value for field (account3rdbalancebef) after checking for its constraitns : type
  val account3rdbalancebef___val: Double = if(!isDouble(inpData(49), "account3rdbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(49));fieldVal}
  // value for field (dedicatedaccountunit1staft) after checking for its constraitns : type
  val dedicatedaccountunit1staft___val: Double = if(!isDouble(inpData(433), "dedicatedaccountunit1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(433));fieldVal}
  // value for field (usageaccumulator5thvalueaft) after checking for its constraitns : type
  val usageaccumulator5thvalueaft___val: Double = if(!isDouble(inpData(210), "usageaccumulator5thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(210));fieldVal}
  // value for field (rechargedivpartda7th) after checking for its constraitns : type
  val rechargedivpartda7th___val: Double = if(!isDouble(inpData(283), "rechargedivpartda7th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(283));fieldVal}
  // value for field (clearedunits4thbef) after checking for its constraitns : type
  val clearedunits4thbef___val: Double = if(!isDouble(inpData(318), "clearedunits4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(318));fieldVal}
  // value for field (unitbalance8thbef) after checking for its constraitns : type
  val unitbalance8thbef___val: Double = if(!isDouble(inpData(345), "unitbalance8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(345));fieldVal}
  // value for field (account6threfilldivamountbef) after checking for its constraitns : type
  val account6threfilldivamountbef___val: Double = if(!isDouble(inpData(65), "account6threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(65));fieldVal}
  // value for field (offer4thidentifieraft) after checking for its constraitns : type
  val offer4thidentifieraft___val: Double = if(!isDouble(inpData(524), "offer4thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(524));fieldVal}
  // value for field (realmoneyflag1stbef) after checking for its constraitns : type
  val realmoneyflag1stbef___val: Double = if(!isDouble(inpData(298), "realmoneyflag1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(298));fieldVal}
  // value for field (dedicatedaccountunit2ndaft) after checking for its constraitns : type
  val dedicatedaccountunit2ndaft___val: Double = if(!isDouble(inpData(440), "dedicatedaccountunit2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(440));fieldVal}
  // value for field (realmoneyflag9thbef) after checking for its constraitns : type
  val realmoneyflag9thbef___val: Double = if(!isDouble(inpData(354), "realmoneyflag9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(354));fieldVal}
  // value for field (account6thcampaignidentbef) after checking for its constraitns : type
  val account6thcampaignidentbef___val: Double = if(!isDouble(inpData(64), "account6thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(64));fieldVal}
  // value for field (refillpromodivunits10thbef) after checking for its constraitns : type
  val refillpromodivunits10thbef___val: Double = if(!isDouble(inpData(358), "refillpromodivunits10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(358));fieldVal}
  // value for field (dedicatedaccountunit10thaft) after checking for its constraitns : type
  val dedicatedaccountunit10thaft___val: Double = if(!isDouble(inpData(496), "dedicatedaccountunit10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(496));fieldVal}
  // value for field (dedicatedaccount9thidbef) after checking for its constraitns : type
  val dedicatedaccount9thidbef___val: Double = if(!isDouble(inpData(81), "dedicatedaccount9thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(81));fieldVal}
  // value for field (clearedunits10thbef) after checking for its constraitns : type
  val clearedunits10thbef___val: Double = if(!isDouble(inpData(360), "clearedunits10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(360));fieldVal}
  // value for field (refillpromodivunits1staft) after checking for its constraitns : type
  val refillpromodivunits1staft___val: Double = if(!isDouble(inpData(436), "refillpromodivunits1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(436));fieldVal}
  // value for field (unitbalance6thaft) after checking for its constraitns : type
  val unitbalance6thaft___val: Double = if(!isDouble(inpData(472), "unitbalance6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(472));fieldVal}
  // value for field (unitbalance2ndbef) after checking for its constraitns : type
  val unitbalance2ndbef___val: Double = if(!isDouble(inpData(303), "unitbalance2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(303));fieldVal}
  // value for field (account5thcampaignidentbef) after checking for its constraitns : type
  val account5thcampaignidentbef___val: Double = if(!isDouble(inpData(58), "account5thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(58));fieldVal}
  // value for field (clearedaccount6thvaluebef) after checking for its constraitns : type
  val clearedaccount6thvaluebef___val: Double = if(!isDouble(inpData(68), "clearedaccount6thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(68));fieldVal}
  // value for field (account2ndrefilldivamountbef) after checking for its constraitns : type
  val account2ndrefilldivamountbef___val: Double = if(!isDouble(inpData(41), "account2ndrefilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(41));fieldVal}
  // value for field (account8thbalanceaft) after checking for its constraitns : type
  val account8thbalanceaft___val: Double = if(!isDouble(inpData(178), "account8thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(178));fieldVal}
  // value for field (usageaccumulator6thvaluebef) after checking for its constraitns : type
  val usageaccumulator6thvaluebef___val: Double = if(!isDouble(inpData(113), "usageaccumulator6thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(113));fieldVal}
  // value for field (refilldivunits3rdaft) after checking for its constraitns : type
  val refilldivunits3rdaft___val: Double = if(!isDouble(inpData(449), "refilldivunits3rdaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(449));fieldVal}
  // value for field (account5thcampaignidentaft) after checking for its constraitns : type
  val account5thcampaignidentaft___val: Double = if(!isDouble(inpData(157), "account5thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(157));fieldVal}
  // value for field (dedicatedaccount10thidbef) after checking for its constraitns : type
  val dedicatedaccount10thidbef___val: Double = if(!isDouble(inpData(87), "dedicatedaccount10thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(87));fieldVal}
  // value for field (rechargedivpartpda4th) after checking for its constraitns : type
  val rechargedivpartpda4th___val: Double = if(!isDouble(inpData(280), "rechargedivpartpda4th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(280));fieldVal}
  // value for field (account6thbalancebef) after checking for its constraitns : type
  val account6thbalancebef___val: Double = if(!isDouble(inpData(67), "account6thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(67));fieldVal}
  // value for field (account3rdcampaignidentbef) after checking for its constraitns : type
  val account3rdcampaignidentbef___val: Double = if(!isDouble(inpData(46), "account3rdcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(46));fieldVal}
  // value for field (maximumsupervisionperiod) after checking for its constraitns : type
  val maximumsupervisionperiod___val: Double = if(!isDouble(inpData(230), "maximumsupervisionperiod", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(230));fieldVal}
  // value for field (offerproductidentifier8thbef) after checking for its constraitns : type
  val offerproductidentifier8thbef___val: Double = if(!isDouble(inpData(415), "offerproductidentifier8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(415));fieldVal}
  // value for field (usageaccumulator10thvalueaft) after checking for its constraitns : type
  val usageaccumulator10thvalueaft___val: Double = if(!isDouble(inpData(220), "usageaccumulator10thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(220));fieldVal}
  // value for field (offerproductidentifier10thbef) after checking for its constraitns : type
  val offerproductidentifier10thbef___val: Double = if(!isDouble(inpData(429), "offerproductidentifier10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(429));fieldVal}
  // value for field (refilldivunits9thaft) after checking for its constraitns : type
  val refilldivunits9thaft___val: Double = if(!isDouble(inpData(491), "refilldivunits9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(491));fieldVal}
  // value for field (accumulatedrefillcounterbef) after checking for its constraitns : type
  val accumulatedrefillcounterbef___val: Double = if(!isDouble(inpData(29), "accumulatedrefillcounterbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(29));fieldVal}
  // value for field (accountbalancebef) after checking for its constraitns : type
  val accountbalancebef___val: Double = if(!isDouble(inpData(27), "accountbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(27));fieldVal}
  // value for field (refillpromodivunits2ndaft) after checking for its constraitns : type
  val refillpromodivunits2ndaft___val: Double = if(!isDouble(inpData(443), "refillpromodivunits2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(443));fieldVal}
  // value for field (account3rdcampaignidentaft) after checking for its constraitns : type
  val account3rdcampaignidentaft___val: Double = if(!isDouble(inpData(145), "account3rdcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(145));fieldVal}
  // value for field (rechargedivpartda6th) after checking for its constraitns : type
  val rechargedivpartda6th___val: Double = if(!isDouble(inpData(282), "rechargedivpartda6th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(282));fieldVal}
  // value for field (account2ndrefilpromodivamntbef) after checking for its constraitns : type
  val account2ndrefilpromodivamntbef___val: Double = if(!isDouble(inpData(42), "account2ndrefilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(42));fieldVal}
  // value for field (usageaccumulator8thvalueaft) after checking for its constraitns : type
  val usageaccumulator8thvalueaft___val: Double = if(!isDouble(inpData(216), "usageaccumulator8thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(216));fieldVal}
  // value for field (offerproductidentifier6thaft) after checking for its constraitns : type
  val offerproductidentifier6thaft___val: Double = if(!isDouble(inpData(542), "offerproductidentifier6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(542));fieldVal}
  // value for field (clearedunits9thaft) after checking for its constraitns : type
  val clearedunits9thaft___val: Double = if(!isDouble(inpData(494), "clearedunits9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(494));fieldVal}
  // value for field (unitbalance5thaft) after checking for its constraitns : type
  val unitbalance5thaft___val: Double = if(!isDouble(inpData(465), "unitbalance5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(465));fieldVal}
  // value for field (clearedunits10thaft) after checking for its constraitns : type
  val clearedunits10thaft___val: Double = if(!isDouble(inpData(501), "clearedunits10thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(501));fieldVal}
  // value for field (clearedaccount3rdvalueaft) after checking for its constraitns : type
  val clearedaccount3rdvalueaft___val: Double = if(!isDouble(inpData(149), "clearedaccount3rdvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(149));fieldVal}
  // value for field (clearedunits6thbef) after checking for its constraitns : type
  val clearedunits6thbef___val: Double = if(!isDouble(inpData(332), "clearedunits6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(332));fieldVal}
  // value for field (offerproductidentifier2ndaft) after checking for its constraitns : type
  val offerproductidentifier2ndaft___val: Double = if(!isDouble(inpData(514), "offerproductidentifier2ndaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(514));fieldVal}
  // value for field (offerproductidentifier5thbef) after checking for its constraitns : type
  val offerproductidentifier5thbef___val: Double = if(!isDouble(inpData(394), "offerproductidentifier5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(394));fieldVal}
  // value for field (usageaccumulator7thidbef) after checking for its constraitns : type
  val usageaccumulator7thidbef___val: Double = if(!isDouble(inpData(114), "usageaccumulator7thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(114));fieldVal}
  // value for field (refilldivunits8thaft) after checking for its constraitns : type
  val refilldivunits8thaft___val: Double = if(!isDouble(inpData(484), "refilldivunits8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(484));fieldVal}
  // value for field (usageaccumulator8thidaft) after checking for its constraitns : type
  val usageaccumulator8thidaft___val: Double = if(!isDouble(inpData(215), "usageaccumulator8thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(215));fieldVal}
  // value for field (offer1stidentifierbef) after checking for its constraitns : type
  val offer1stidentifierbef___val: Double = if(!isDouble(inpData(362), "offer1stidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(362));fieldVal}
  // value for field (supervisiondayssurplus) after checking for its constraitns : type
  val supervisiondayssurplus___val: Double = if(!isDouble(inpData(226), "supervisiondayssurplus", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(226));fieldVal}
  // value for field (offer7thidentifieraft) after checking for its constraitns : type
  val offer7thidentifieraft___val: Double = if(!isDouble(inpData(545), "offer7thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(545));fieldVal}
  // value for field (dedicatedaccountunit7thbef) after checking for its constraitns : type
  val dedicatedaccountunit7thbef___val: Double = if(!isDouble(inpData(334), "dedicatedaccountunit7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(334));fieldVal}
  // value for field (clearedunits5thbef) after checking for its constraitns : type
  val clearedunits5thbef___val: Double = if(!isDouble(inpData(325), "clearedunits5thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(325));fieldVal}
  // value for field (usageaccumulator3rdvalueaft) after checking for its constraitns : type
  val usageaccumulator3rdvalueaft___val: Double = if(!isDouble(inpData(206), "usageaccumulator3rdvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(206));fieldVal}
  // value for field (usageaccumulator2ndidbef) after checking for its constraitns : type
  val usageaccumulator2ndidbef___val: Double = if(!isDouble(inpData(104), "usageaccumulator2ndidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(104));fieldVal}
  // value for field (offer8thidentifieraft) after checking for its constraitns : type
  val offer8thidentifieraft___val: Double = if(!isDouble(inpData(552), "offer8thidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(552));fieldVal}
  // value for field (unitbalance8thaft) after checking for its constraitns : type
  val unitbalance8thaft___val: Double = if(!isDouble(inpData(486), "unitbalance8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(486));fieldVal}
  // value for field (usageaccumulator8thidbef) after checking for its constraitns : type
  val usageaccumulator8thidbef___val: Double = if(!isDouble(inpData(116), "usageaccumulator8thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(116));fieldVal}
  // value for field (dedicatedaccount1stidaft) after checking for its constraitns : type
  val dedicatedaccount1stidaft___val: Double = if(!isDouble(inpData(132), "dedicatedaccount1stidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(132));fieldVal}
  // value for field (account6thbalanceaft) after checking for its constraitns : type
  val account6thbalanceaft___val: Double = if(!isDouble(inpData(166), "account6thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(166));fieldVal}
  // value for field (account6threfilpromodivamntbef) after checking for its constraitns : type
  val account6threfilpromodivamntbef___val: Double = if(!isDouble(inpData(66), "account6threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(66));fieldVal}
  // value for field (offerproductidentifier2ndbef) after checking for its constraitns : type
  val offerproductidentifier2ndbef___val: Double = if(!isDouble(inpData(373), "offerproductidentifier2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(373));fieldVal}
  // value for field (offer2ndidentifieraft) after checking for its constraitns : type
  val offer2ndidentifieraft___val: Double = if(!isDouble(inpData(510), "offer2ndidentifieraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(510));fieldVal}
  // value for field (unitbalance10thbef) after checking for its constraitns : type
  val unitbalance10thbef___val: Double = if(!isDouble(inpData(359), "unitbalance10thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(359));fieldVal}
  // value for field (clearedaccount3rdvaluebef) after checking for its constraitns : type
  val clearedaccount3rdvaluebef___val: Double = if(!isDouble(inpData(50), "clearedaccount3rdvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(50));fieldVal}
  // value for field (usageaccumulator6thidbef) after checking for its constraitns : type
  val usageaccumulator6thidbef___val: Double = if(!isDouble(inpData(112), "usageaccumulator6thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(112));fieldVal}
  // value for field (account4threfilldivamountbef) after checking for its constraitns : type
  val account4threfilldivamountbef___val: Double = if(!isDouble(inpData(53), "account4threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(53));fieldVal}
  // value for field (account10threfilpromdivamntaft) after checking for its constraitns : type
  val account10threfilpromdivamntaft___val: Double = if(!isDouble(inpData(189), "account10threfilpromdivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(189));fieldVal}
  // value for field (clearedaccount8thvalueaft) after checking for its constraitns : type
  val clearedaccount8thvalueaft___val: Double = if(!isDouble(inpData(179), "clearedaccount8thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(179));fieldVal}
  // value for field (account7thcampaignidentaft) after checking for its constraitns : type
  val account7thcampaignidentaft___val: Double = if(!isDouble(inpData(169), "account7thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(169));fieldVal}
  // value for field (dedicatedaccountunit1stbef) after checking for its constraitns : type
  val dedicatedaccountunit1stbef___val: Double = if(!isDouble(inpData(292), "dedicatedaccountunit1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(292));fieldVal}
  // value for field (account3rdbalanceaft) after checking for its constraitns : type
  val account3rdbalanceaft___val: Double = if(!isDouble(inpData(148), "account3rdbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(148));fieldVal}
  // value for field (refillpromodivunits7thaft) after checking for its constraitns : type
  val refillpromodivunits7thaft___val: Double = if(!isDouble(inpData(478), "refillpromodivunits7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(478));fieldVal}
  // value for field (account1stbalanceaft) after checking for its constraitns : type
  val account1stbalanceaft___val: Double = if(!isDouble(inpData(136), "account1stbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(136));fieldVal}
  // value for field (clearedaccount1stvaluebef) after checking for its constraitns : type
  val clearedaccount1stvaluebef___val: Double = if(!isDouble(inpData(38), "clearedaccount1stvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(38));fieldVal}
  // value for field (dedicatedaccountunit6thaft) after checking for its constraitns : type
  val dedicatedaccountunit6thaft___val: Double = if(!isDouble(inpData(468), "dedicatedaccountunit6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(468));fieldVal}
  // value for field (supervisiondayspromopart) after checking for its constraitns : type
  val supervisiondayspromopart___val: Double = if(!isDouble(inpData(225), "supervisiondayspromopart", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(225));fieldVal}
  // value for field (account1strefilldivamountaft) after checking for its constraitns : type
  val account1strefilldivamountaft___val: Double = if(!isDouble(inpData(134), "account1strefilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(134));fieldVal}
  // value for field (refillpromodivunits8thbef) after checking for its constraitns : type
  val refillpromodivunits8thbef___val: Double = if(!isDouble(inpData(344), "refillpromodivunits8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(344));fieldVal}
  // value for field (dedicatedaccount8thidaft) after checking for its constraitns : type
  val dedicatedaccount8thidaft___val: Double = if(!isDouble(inpData(174), "dedicatedaccount8thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(174));fieldVal}
  // value for field (accumulatedprogrcounterbef) after checking for its constraitns : type
  val accumulatedprogrcounterbef___val: Double = if(!isDouble(inpData(31), "accumulatedprogrcounterbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(31));fieldVal}
  // value for field (unitbalance1staft) after checking for its constraitns : type
  val unitbalance1staft___val: Double = if(!isDouble(inpData(437), "unitbalance1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(437));fieldVal}
  // value for field (refillpromodivunits9thaft) after checking for its constraitns : type
  val refillpromodivunits9thaft___val: Double = if(!isDouble(inpData(492), "refillpromodivunits9thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(492));fieldVal}
  // value for field (account10thcampaignidentaft) after checking for its constraitns : type
  val account10thcampaignidentaft___val: Double = if(!isDouble(inpData(187), "account10thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(187));fieldVal}
  // value for field (permanentserviceclassaft) after checking for its constraitns : type
  val permanentserviceclassaft___val: Double = if(!isDouble(inpData(193), "permanentserviceclassaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(193));fieldVal}
  // value for field (usageaccumulator3rdidbef) after checking for its constraitns : type
  val usageaccumulator3rdidbef___val: Double = if(!isDouble(inpData(106), "usageaccumulator3rdidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(106));fieldVal}
  // value for field (accumulatedrefillvaluebef) after checking for its constraitns : type
  val accumulatedrefillvaluebef___val: Double = if(!isDouble(inpData(28), "accumulatedrefillvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(28));fieldVal}
  // value for field (offer4thidentifierbef) after checking for its constraitns : type
  val offer4thidentifierbef___val: Double = if(!isDouble(inpData(383), "offer4thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(383));fieldVal}
  // value for field (refillpromodivunits3rdbef) after checking for its constraitns : type
  val refillpromodivunits3rdbef___val: Double = if(!isDouble(inpData(309), "refillpromodivunits3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(309));fieldVal}
  // value for field (clearedaccount4thvalueaft) after checking for its constraitns : type
  val clearedaccount4thvalueaft___val: Double = if(!isDouble(inpData(155), "clearedaccount4thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(155));fieldVal}
  // value for field (usageaccumulator8thvaluebef) after checking for its constraitns : type
  val usageaccumulator8thvaluebef___val: Double = if(!isDouble(inpData(117), "usageaccumulator8thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(117));fieldVal}
  // value for field (clearedaccount5thvaluebef) after checking for its constraitns : type
  val clearedaccount5thvaluebef___val: Double = if(!isDouble(inpData(62), "clearedaccount5thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(62));fieldVal}
  // value for field (aggregatedbalanceaft) after checking for its constraitns : type
  val aggregatedbalanceaft___val: Double = if(!isDouble(inpData(573), "aggregatedbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(573));fieldVal}
  // value for field (dedicatedaccount1stidbef) after checking for its constraitns : type
  val dedicatedaccount1stidbef___val: Double = if(!isDouble(inpData(33), "dedicatedaccount1stidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(33));fieldVal}
  // value for field (usageaccumulator4thidbef) after checking for its constraitns : type
  val usageaccumulator4thidbef___val: Double = if(!isDouble(inpData(108), "usageaccumulator4thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(108));fieldVal}
  // value for field (clearedaccount2ndvaluebef) after checking for its constraitns : type
  val clearedaccount2ndvaluebef___val: Double = if(!isDouble(inpData(44), "clearedaccount2ndvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(44));fieldVal}
  // value for field (rechargedivpartpda7th) after checking for its constraitns : type
  val rechargedivpartpda7th___val: Double = if(!isDouble(inpData(288), "rechargedivpartpda7th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(288));fieldVal}
  // value for field (hub_id) after checking for its constraitns : type
  val hub_id___val: Int = if(!isInt(inpData(576), "hub_id", HandleError )) 0  else { val fieldVal = conversion.ToInteger(inpData(576));fieldVal}
  // value for field (clearedunits4thaft) after checking for its constraitns : type
  val clearedunits4thaft___val: Double = if(!isDouble(inpData(459), "clearedunits4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(459));fieldVal}
  // value for field (account5thbalancebef) after checking for its constraitns : type
  val account5thbalancebef___val: Double = if(!isDouble(inpData(61), "account5thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(61));fieldVal}
  // value for field (dedicatedaccount3rdidaft) after checking for its constraitns : type
  val dedicatedaccount3rdidaft___val: Double = if(!isDouble(inpData(144), "dedicatedaccount3rdidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(144));fieldVal}
  // value for field (usageaccumulator3rdidaft) after checking for its constraitns : type
  val usageaccumulator3rdidaft___val: Double = if(!isDouble(inpData(205), "usageaccumulator3rdidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(205));fieldVal}
  // value for field (rechargedivpartpda3rd) after checking for its constraitns : type
  val rechargedivpartpda3rd___val: Double = if(!isDouble(inpData(279), "rechargedivpartpda3rd", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(279));fieldVal}
  // value for field (dedicatedaccountunit4thbef) after checking for its constraitns : type
  val dedicatedaccountunit4thbef___val: Double = if(!isDouble(inpData(313), "dedicatedaccountunit4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(313));fieldVal}
  // value for field (unitbalance4thbef) after checking for its constraitns : type
  val unitbalance4thbef___val: Double = if(!isDouble(inpData(317), "unitbalance4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(317));fieldVal}
  // value for field (rechargedivpartpda10th) after checking for its constraitns : type
  val rechargedivpartpda10th___val: Double = if(!isDouble(inpData(291), "rechargedivpartpda10th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(291));fieldVal}
  // value for field (rechargedivpartda10th) after checking for its constraitns : type
  val rechargedivpartda10th___val: Double = if(!isDouble(inpData(286), "rechargedivpartda10th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(286));fieldVal}
  // value for field (refilldivunits2ndbef) after checking for its constraitns : type
  val refilldivunits2ndbef___val: Double = if(!isDouble(inpData(301), "refilldivunits2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(301));fieldVal}
  // value for field (realmoneyflag4thaft) after checking for its constraitns : type
  val realmoneyflag4thaft___val: Double = if(!isDouble(inpData(460), "realmoneyflag4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(460));fieldVal}
  // value for field (servicefeedayssurplus) after checking for its constraitns : type
  val servicefeedayssurplus___val: Double = if(!isDouble(inpData(228), "servicefeedayssurplus", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(228));fieldVal}
  // value for field (account9threfilldivamountbef) after checking for its constraitns : type
  val account9threfilldivamountbef___val: Double = if(!isDouble(inpData(83), "account9threfilldivamountbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(83));fieldVal}
  // value for field (account4thbalanceaft) after checking for its constraitns : type
  val account4thbalanceaft___val: Double = if(!isDouble(inpData(154), "account4thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(154));fieldVal}
  // value for field (usageaccumulator1stidbef) after checking for its constraitns : type
  val usageaccumulator1stidbef___val: Double = if(!isDouble(inpData(102), "usageaccumulator1stidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(102));fieldVal}
  // value for field (usageaccumulator9thvalueaft) after checking for its constraitns : type
  val usageaccumulator9thvalueaft___val: Double = if(!isDouble(inpData(218), "usageaccumulator9thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(218));fieldVal}
  // value for field (refilldivunits1stbef) after checking for its constraitns : type
  val refilldivunits1stbef___val: Double = if(!isDouble(inpData(294), "refilldivunits1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(294));fieldVal}
  // value for field (offerproductidentifier5thaft) after checking for its constraitns : type
  val offerproductidentifier5thaft___val: Double = if(!isDouble(inpData(535), "offerproductidentifier5thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(535));fieldVal}
  // value for field (refillamountconverted) after checking for its constraitns : type
  val refillamountconverted___val: Double = if(!isDouble(inpData(15), "refillamountconverted", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(15));fieldVal}
  // value for field (dedicatedaccount6thidaft) after checking for its constraitns : type
  val dedicatedaccount6thidaft___val: Double = if(!isDouble(inpData(162), "dedicatedaccount6thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(162));fieldVal}
  // value for field (usageaccumulator10thvaluebef) after checking for its constraitns : type
  val usageaccumulator10thvaluebef___val: Double = if(!isDouble(inpData(121), "usageaccumulator10thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(121));fieldVal}
  // value for field (refilldivunits7thbef) after checking for its constraitns : type
  val refilldivunits7thbef___val: Double = if(!isDouble(inpData(336), "refilldivunits7thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(336));fieldVal}
  // value for field (clearedunits6thaft) after checking for its constraitns : type
  val clearedunits6thaft___val: Double = if(!isDouble(inpData(473), "clearedunits6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(473));fieldVal}
  // value for field (refilldivunits1staft) after checking for its constraitns : type
  val refilldivunits1staft___val: Double = if(!isDouble(inpData(435), "refilldivunits1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(435));fieldVal}
  // value for field (offer9thidentifierbef) after checking for its constraitns : type
  val offer9thidentifierbef___val: Double = if(!isDouble(inpData(418), "offer9thidentifierbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(418));fieldVal}
  // value for field (market_id) after checking for its constraitns : type
  val market_id___val: Int = if(!isInt(inpData(575), "market_id", HandleError )) 0  else { val fieldVal = conversion.ToInteger(inpData(575));fieldVal}
  // value for field (realmoneyflag4thbef) after checking for its constraitns : type
  val realmoneyflag4thbef___val: Double = if(!isDouble(inpData(319), "realmoneyflag4thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(319));fieldVal}
  // value for field (account8thbalancebef) after checking for its constraitns : type
  val account8thbalancebef___val: Double = if(!isDouble(inpData(79), "account8thbalancebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(79));fieldVal}
  // value for field (account9threfilpromodivamntaft) after checking for its constraitns : type
  val account9threfilpromodivamntaft___val: Double = if(!isDouble(inpData(183), "account9threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(183));fieldVal}
  // value for field (account5thbalanceaft) after checking for its constraitns : type
  val account5thbalanceaft___val: Double = if(!isDouble(inpData(160), "account5thbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(160));fieldVal}
  // value for field (rechargedivpartda5th) after checking for its constraitns : type
  val rechargedivpartda5th___val: Double = if(!isDouble(inpData(274), "rechargedivpartda5th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(274));fieldVal}
  // value for field (unitbalance7thaft) after checking for its constraitns : type
  val unitbalance7thaft___val: Double = if(!isDouble(inpData(479), "unitbalance7thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(479));fieldVal}
  // value for field (rechargedivpartda8th) after checking for its constraitns : type
  val rechargedivpartda8th___val: Double = if(!isDouble(inpData(284), "rechargedivpartda8th", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(284));fieldVal}
  // value for field (clearedunits2ndbef) after checking for its constraitns : type
  val clearedunits2ndbef___val: Double = if(!isDouble(inpData(304), "clearedunits2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(304));fieldVal}
  // value for field (clearedunits8thaft) after checking for its constraitns : type
  val clearedunits8thaft___val: Double = if(!isDouble(inpData(487), "clearedunits8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(487));fieldVal}
  // value for field (unitbalance6thbef) after checking for its constraitns : type
  val unitbalance6thbef___val: Double = if(!isDouble(inpData(331), "unitbalance6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(331));fieldVal}
  // value for field (offerproductidentifier4thaft) after checking for its constraitns : type
  val offerproductidentifier4thaft___val: Double = if(!isDouble(inpData(528), "offerproductidentifier4thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(528));fieldVal}
  // value for field (account3rdrefilpromodivamntaft) after checking for its constraitns : type
  val account3rdrefilpromodivamntaft___val: Double = if(!isDouble(inpData(147), "account3rdrefilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(147));fieldVal}
  // value for field (usageaccumulator1stvalueaft) after checking for its constraitns : type
  val usageaccumulator1stvalueaft___val: Double = if(!isDouble(inpData(202), "usageaccumulator1stvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(202));fieldVal}
  // value for field (usageaccumulator4thidaft) after checking for its constraitns : type
  val usageaccumulator4thidaft___val: Double = if(!isDouble(inpData(207), "usageaccumulator4thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(207));fieldVal}
  // value for field (dedicatedaccount8thidbef) after checking for its constraitns : type
  val dedicatedaccount8thidbef___val: Double = if(!isDouble(inpData(75), "dedicatedaccount8thidbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(75));fieldVal}
  // value for field (dedicatedaccountunit8thbef) after checking for its constraitns : type
  val dedicatedaccountunit8thbef___val: Double = if(!isDouble(inpData(341), "dedicatedaccountunit8thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(341));fieldVal}
  // value for field (clearedaccount10thvaluebef) after checking for its constraitns : type
  val clearedaccount10thvaluebef___val: Double = if(!isDouble(inpData(92), "clearedaccount10thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(92));fieldVal}
  // value for field (clearedaccount9thvalueaft) after checking for its constraitns : type
  val clearedaccount9thvalueaft___val: Double = if(!isDouble(inpData(185), "clearedaccount9thvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(185));fieldVal}
  // value for field (unitbalance1stbef) after checking for its constraitns : type
  val unitbalance1stbef___val: Double = if(!isDouble(inpData(296), "unitbalance1stbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(296));fieldVal}
  // value for field (rechargedivpartmain) after checking for its constraitns : type
  val rechargedivpartmain___val: Double = if(!isDouble(inpData(269), "rechargedivpartmain", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(269));fieldVal}
  // value for field (accumulatedprogconteraft) after checking for its constraitns : type
  val accumulatedprogconteraft___val: Double = if(!isDouble(inpData(130), "accumulatedprogconteraft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(130));fieldVal}
  // value for field (clearedaccount9thvaluebef) after checking for its constraitns : type
  val clearedaccount9thvaluebef___val: Double = if(!isDouble(inpData(86), "clearedaccount9thvaluebef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(86));fieldVal}
  // value for field (account9thcampaignidentaft) after checking for its constraitns : type
  val account9thcampaignidentaft___val: Double = if(!isDouble(inpData(181), "account9thcampaignidentaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(181));fieldVal}
  // value for field (clearedaccount2ndvalueaft) after checking for its constraitns : type
  val clearedaccount2ndvalueaft___val: Double = if(!isDouble(inpData(143), "clearedaccount2ndvalueaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(143));fieldVal}
  // value for field (refilldivisionamount) after checking for its constraitns : type
  val refilldivisionamount___val: Double = if(!isDouble(inpData(16), "refilldivisionamount", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(16));fieldVal}
  // value for field (account5threfilpromodivamntbef) after checking for its constraitns : type
  val account5threfilpromodivamntbef___val: Double = if(!isDouble(inpData(60), "account5threfilpromodivamntbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(60));fieldVal}
  // value for field (transactionamount) after checking for its constraitns : type
  val transactionamount___val: Double = if(!isDouble(inpData(13), "transactionamount", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(13));fieldVal}
  // value for field (usageaccumulator10thidaft) after checking for its constraitns : type
  val usageaccumulator10thidaft___val: Double = if(!isDouble(inpData(219), "usageaccumulator10thidaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(219));fieldVal}
  // value for field (refilltype) after checking for its constraitns : type
  val refilltype___val: Double = if(!isDouble(inpData(17), "refilltype", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(17));fieldVal}
  // value for field (account2ndrefilpromodivamntaft) after checking for its constraitns : type
  val account2ndrefilpromodivamntaft___val: Double = if(!isDouble(inpData(141), "account2ndrefilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(141));fieldVal}
  // value for field (offerproductidentifier8thaft) after checking for its constraitns : type
  val offerproductidentifier8thaft___val: Double = if(!isDouble(inpData(556), "offerproductidentifier8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(556));fieldVal}
  // value for field (dedicatedaccountunit8thaft) after checking for its constraitns : type
  val dedicatedaccountunit8thaft___val: Double = if(!isDouble(inpData(482), "dedicatedaccountunit8thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(482));fieldVal}
  // value for field (realmoneyflag3rdbef) after checking for its constraitns : type
  val realmoneyflag3rdbef___val: Double = if(!isDouble(inpData(312), "realmoneyflag3rdbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(312));fieldVal}
  // value for field (clearedunits1staft) after checking for its constraitns : type
  val clearedunits1staft___val: Double = if(!isDouble(inpData(438), "clearedunits1staft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(438));fieldVal}
  // value for field (account6threfilldivamountaft) after checking for its constraitns : type
  val account6threfilldivamountaft___val: Double = if(!isDouble(inpData(164), "account6threfilldivamountaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(164));fieldVal}
  // value for field (offerproductidentifier9thbef) after checking for its constraitns : type
  val offerproductidentifier9thbef___val: Double = if(!isDouble(inpData(422), "offerproductidentifier9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(422));fieldVal}
  // value for field (account2ndbalanceaft) after checking for its constraitns : type
  val account2ndbalanceaft___val: Double = if(!isDouble(inpData(142), "account2ndbalanceaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(142));fieldVal}
  // value for field (dedicatedaccountunit9thbef) after checking for its constraitns : type
  val dedicatedaccountunit9thbef___val: Double = if(!isDouble(inpData(348), "dedicatedaccountunit9thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(348));fieldVal}
  // value for field (refillpromodivunits6thaft) after checking for its constraitns : type
  val refillpromodivunits6thaft___val: Double = if(!isDouble(inpData(471), "refillpromodivunits6thaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(471));fieldVal}
  // value for field (account9thcampaignidentbef) after checking for its constraitns : type
  val account9thcampaignidentbef___val: Double = if(!isDouble(inpData(82), "account9thcampaignidentbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(82));fieldVal}
  // value for field (dedicatedaccountunit2ndbef) after checking for its constraitns : type
  val dedicatedaccountunit2ndbef___val: Double = if(!isDouble(inpData(299), "dedicatedaccountunit2ndbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(299));fieldVal}
  // value for field (realmoneyflag6thbef) after checking for its constraitns : type
  val realmoneyflag6thbef___val: Double = if(!isDouble(inpData(333), "realmoneyflag6thbef", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(333));fieldVal}
  // value for field (account7threfilpromodivamntaft) after checking for its constraitns : type
  val account7threfilpromodivamntaft___val: Double = if(!isDouble(inpData(171), "account7threfilpromodivamntaft", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(171));fieldVal}
  // value for field (rechargedivpartpda1st) after checking for its constraitns : type
  val rechargedivpartpda1st___val: Double = if(!isDouble(inpData(277), "rechargedivpartpda1st", HandleError )) 0  else { val fieldVal = conversion.ToDouble(inpData(277));fieldVal}
}
class common_exeGenerated_AirRefillCS5_1_1_process_o1(conversion: com.ligadata.runtime.Conversion,
                                                      log : com.ligadata.runtime.Log,
                                                      context: com.ligadata.runtime.JtmContext,
                                                      common: common_exeGenerated_AirRefillCS5_1_1,
                                                      msg1: com.ligadata.messages.V1000000.AirRefillCS5_input) {
  import log._
  import common._
  val result: Array[MessageInterface]= try {
    if (!((context.CurrentErrors()==0))) {
      Debug("Filtered: AirRefillCS5_1@o1")
      Array.empty[MessageInterface]
    } else {
      val result = com.ligadata.messages.V1000000.AirRefillCS5.createInstance
      result.account7thcampaignidentbef = account7thcampaignidentbef___val
      result.offerproductidentifier10thbef = offerproductidentifier10thbef___val
      result.offertype2ndbef = inpData(372)
      result.unitbalance7thaft = unitbalance7thaft___val
      result.offerexpirydatetime5thaft = inpData(537)
      result.account10threfilldivamountbef = account10threfilldivamountbef___val
      result.unitbalance7thbef = unitbalance7thbef___val
      result.rechargedivpartda2nd = rechargedivpartda2nd___val
      result.promotionplanallocstartdate = inpData(234)
      result.offertype1stbef = inpData(365)
      result.transactioncode = inpData(12)
      result.dedicatedaccount5thidbef = dedicatedaccount5thidbef___val
      result.maximumservicefeeperiod = maximumservicefeeperiod___val
      result.accumulatedrefillcounteraft = accumulatedrefillcounteraft___val
      result.externaldata4 = inpData(239)
      result.offer3rdidentifieraft = offer3rdidentifieraft___val
      result.rechargedivpartda4th = rechargedivpartda4th___val
      result.offertype10thbef = inpData(428)
      result.clearedunits7thbef = clearedunits7thbef___val
      result.account5thbalancebef = account5thbalancebef___val
      result.offer6thidentifierbef = offer6thidentifierbef___val
      result.offerexpirydatetime7thbef = inpData(410)
      result.accountexpiry6thdateaft = inpData(260)
      result.communityidbef2 = inpData(123)
      result.refillpromodivisionamount = refillpromodivisionamount___val
      result.accumulatedrefillcounterbef = accumulatedrefillcounterbef___val
      result.offerexpirydatetime2ndaft = inpData(516)
      result.dedicatedaccount6thidaft = dedicatedaccount6thidaft___val
      result.dedicatedaccount9thidaft = dedicatedaccount9thidaft___val
      result.offerstartdatetime8thbef = inpData(416)
      result.refillpromodivunits1stbef = refillpromodivunits1stbef___val
      result.account6threfilldivamountaft = account6threfilldivamountaft___val
      result.offerstartdate10thaft = inpData(567)
      result.offer6thidentifieraft = offer6thidentifieraft___val
      result.usageaccumulator8thidaft = usageaccumulator8thidaft___val
      result.refillpromodivunits6thbef = refillpromodivunits6thbef___val
      result.originfileid = inpData(2)
      result.promotionplanaft = inpData(192)
      result.rechargedivpartpda9th = rechargedivpartpda9th___val
      result.refillpromodivunits9thbef = refillpromodivunits9thbef___val
      result.accountexpiry8thdateaft = inpData(264)
      result.offerexpirydate7thbef = inpData(406)
      result.offerexpirydate8thaft = inpData(554)
      result.clearedunits8thaft = clearedunits8thaft___val
      result.dedicatedaccount1stidbef = dedicatedaccount1stidbef___val
      result.accumulatedprogressionvalueaft = accumulatedprogressionvalueaft___val
      result.rechargedivpartmain = rechargedivpartmain___val
      result.accountgroupid = inpData(235)
      result.account1stcampaignidentbef = account1stcampaignidentbef___val
      result.usageaccumulator8thidbef = usageaccumulator8thidbef___val
      result.accountexpiry2nddatebef = inpData(251)
      result.usageaccumulator9thvaluebef = usageaccumulator9thvaluebef___val
      result.clearedunits9thbef = clearedunits9thbef___val
      result.requestedrefilltype = inpData(248)
      result.account9thbalancebef = account9thbalancebef___val
      result.offerstartdate8thaft = inpData(553)
      result.usageaccumulator3rdvalueaft = usageaccumulator3rdvalueaft___val
      result.realmoneyflag7thaft = realmoneyflag7thaft___val
      result.realmoneyflag1staft = realmoneyflag1staft___val
      result.account7threfilldivamountbef = account7threfilldivamountbef___val
      result.account8threfilldivamountbef = account8threfilldivamountbef___val
      result.accountstartdate10thbef = inpData(356)
      result.offertype1staft = inpData(506)
      result.account4thbalanceaft = account4thbalanceaft___val
      result.realmoneyflag2ndbef = realmoneyflag2ndbef___val
      result.account5threfilldivamountaft = account5threfilldivamountaft___val
      result.dedicatedaccount9thidbef = dedicatedaccount9thidbef___val
      result.accountstartdate6thbef = inpData(328)
      result.offerexpirydatetime9thbef = inpData(424)
      result.accountflagsaft = inpData(125)
      result.offerexpirydate6thaft = inpData(540)
      result.account8threfilldivamountaft = account8threfilldivamountaft___val
      result.offerexpirydatetime3rdbef = inpData(382)
      result.refillpromodivunits4thbef = refillpromodivunits4thbef___val
      result.hostname = inpData(6)
      result.clearedunits5thbef = clearedunits5thbef___val
      result.unitbalance2ndbef = unitbalance2ndbef___val
      result.accountexpiry6thdatebef = inpData(259)
      result.refillpromodivunits3rdbef = refillpromodivunits3rdbef___val
      result.timestamp = inpData(8)
      result.clearedunits3rdaft = clearedunits3rdaft___val
      result.accountflagsbef = inpData(26)
      result.refillpromodivunits5thbef = refillpromodivunits5thbef___val
      result.clearedunits8thbef = clearedunits8thbef___val
      result.offerstartdatetime9thbef = inpData(423)
      result.usageaccumulator5thvalueaft = usageaccumulator5thvalueaft___val
      result.dedicatedaccountunit4thbef = dedicatedaccountunit4thbef___val
      result.clearedaccount6thvaluebef = clearedaccount6thvaluebef___val
      result.offerexpirydate2ndaft = inpData(512)
      result.transactionamount = transactionamount___val
      result.usageaccumulator2ndvalueaft = usageaccumulator2ndvalueaft___val
      result.offertype5thbef = inpData(393)
      result.unitbalance1staft = unitbalance1staft___val
      result.usageaccumulator8thvalueaft = usageaccumulator8thvalueaft___val
      result.offertype10thaft = inpData(569)
      result.offerproductidentifier6thbef = offerproductidentifier6thbef___val
      result.offer10thidentifierbef = offer10thidentifierbef___val
      result.dedicatedaccountunit3rdbef = dedicatedaccountunit3rdbef___val
      result.dedicatedaccount7thidbef = dedicatedaccount7thidbef___val
      result.realmoneyflag7thbef = realmoneyflag7thbef___val
      result.offer2ndidentifierbef = offer2ndidentifierbef___val
      result.clearedaccount3rdvalueaft = clearedaccount3rdvalueaft___val
      result.dedicatedaccountunit4thaft = dedicatedaccountunit4thaft___val
      result.refilldivunits6thbef = refilldivunits6thbef___val
      result.offerexpirydatetime7thaft = inpData(551)
      result.unitbalance10thaft = unitbalance10thaft___val
      result.offerstartdate7thaft = inpData(546)
      result.account4threfilpromodivamntbef = account4threfilpromodivamntbef___val
      result.offerstartdatetime7thaft = inpData(550)
      result.unitbalance9thbef = unitbalance9thbef___val
      result.promotionannouncementcode = inpData(25)
      result.account2ndbalancebef = account2ndbalancebef___val
      result.account5thbalanceaft = account5thbalanceaft___val
      result.offerstartdatetime3rdbef = inpData(381)
      result.clearedunits10thaft = clearedunits10thaft___val
      result.temporaryservclasexpirydateaft = temporaryservclasexpirydateaft___val
      result.offertype7thbef = inpData(407)
      result.refillpromodivunits3rdaft = refillpromodivunits3rdaft___val
      result.offerstartdate8thbef = inpData(412)
      result.accountexpiry10thdatebef = inpData(267)
      result.account3rdrefilldivamountaft = account3rdrefilldivamountaft___val
      result.account10thbalancebef = account10thbalancebef___val
      result.serviceremovalgraceperiodbef = serviceremovalgraceperiodbef___val
      result.creditclearanceperiodbef = creditclearanceperiodbef___val
      result.offerstartdate5thbef = inpData(391)
      result.offerstartdatetime4thaft = inpData(529)
      result.dedicatedaccount7thidaft = dedicatedaccount7thidaft___val
      result.refilldivunits10thbef = refilldivunits10thbef___val
      result.refilldivunits1staft = refilldivunits1staft___val
      result.offerexpirydate2ndbef = inpData(371)
      result.refillpromodivunits8thaft = refillpromodivunits8thaft___val
      result.refilldivunits5thaft = refilldivunits5thaft___val
      result.unitbalance5thbef = unitbalance5thbef___val
      result.dedicatedaccountunit9thaft = dedicatedaccountunit9thaft___val
      result.usageaccumulator10thvaluebef = usageaccumulator10thvaluebef___val
      result.account10threfilpromdivamntaft = account10threfilpromdivamntaft___val
      result.clearedaccount6thvalueaft = clearedaccount6thvalueaft___val
      result.clearedaccount8thvaluebef = clearedaccount8thvaluebef___val
      result.refilldivunits7thaft = refilldivunits7thaft___val
      result.supervisionexpirydateaft = inpData(200)
      result.account9thcampaignidentaft = account9thcampaignidentaft___val
      result.clearedaccount9thvaluebef = clearedaccount9thvaluebef___val
      result.offerstartdatetime8thaft = inpData(557)
      result.offerproductidentifier2ndbef = offerproductidentifier2ndbef___val
      result.refilldivunits3rdaft = refilldivunits3rdaft___val
      result.account1stbalanceaft = account1stbalanceaft___val
      result.offertype8thbef = inpData(414)
      result.usageaccumulator4thidaft = usageaccumulator4thidaft___val
      result.account8thcampaignidentaft = account8thcampaignidentaft___val
      result.dedicatedaccountunit8thaft = dedicatedaccountunit8thaft___val
      result.communityidaft1 = inpData(221)
      result.usageaccumulator10thidbef = usageaccumulator10thidbef___val
      result.offerexpirydate9thaft = inpData(561)
      result.offer10thidentifieraft = offer10thidentifieraft___val
      result.clearedunits5thaft = clearedunits5thaft___val
      result.accountstartdate7thbef = inpData(335)
      result.offerproductidentifier4thaft = offerproductidentifier4thaft___val
      result.offerproductidentifier4thbef = offerproductidentifier4thbef___val
      result.account7threfilpromodivamntaft = account7threfilpromodivamntaft___val
      result.refilloptionaft = inpData(196)
      result.usageaccumulator9thidaft = usageaccumulator9thidaft___val
      result.dedicatedaccountunit5thbef = dedicatedaccountunit5thbef___val
      result.account9thbalanceaft = account9thbalanceaft___val
      result.account4threfilldivamountbef = account4threfilldivamountbef___val
      result.realmoneyflag3rdaft = realmoneyflag3rdaft___val
      result.dedicatedaccount4thidaft = dedicatedaccount4thidaft___val
      result.refilldivunits9thbef = refilldivunits9thbef___val
      result.refillpromodivunits9thaft = refillpromodivunits9thaft___val
      result.offertype9thbef = inpData(421)
      result.accountstartdate8thaft = inpData(483)
      result.offer8thidentifieraft = offer8thidentifieraft___val
      result.account5threfilpromodivamntbef = account5threfilpromodivamntbef___val
      result.unitbalance6thbef = unitbalance6thbef___val
      result.offer2ndidentifieraft = offer2ndidentifieraft___val
      result.usageaccumulator2ndidbef = usageaccumulator2ndidbef___val
      result.unitbalance10thbef = unitbalance10thbef___val
      result.account2ndbalanceaft = account2ndbalanceaft___val
      result.account6thcampaignidentbef = account6thcampaignidentbef___val
      result.offerstartdatetime1staft = inpData(508)
      result.account2ndrefilpromodivamntaft = account2ndrefilpromodivamntaft___val
      result.realmoneyflag4thbef = realmoneyflag4thbef___val
      result.unitbalance9thaft = unitbalance9thaft___val
      result.accounthomeregion = inpData(244)
      result.usageaccumulator7thvaluebef = usageaccumulator7thvaluebef___val
      result.account9threfilpromodivamntaft = account9threfilpromodivamntaft___val
      result.offerstartdatetime6thaft = inpData(543)
      result.offerstartdatetime5thbef = inpData(395)
      result.realmoneyflag5thaft = realmoneyflag5thaft___val
      result.servicefeeexpirydatebef = inpData(98)
      result.offerstartdatetime2ndbef = inpData(374)
      result.rechargedivpartda7th = rechargedivpartda7th___val
      result.offerproductidentifier9thbef = offerproductidentifier9thbef___val
      result.accountstartdate2ndaft = inpData(441)
      result.offerexpirydate10thbef = inpData(427)
      result.accountexpiry5thdatebef = inpData(257)
      result.offerproductidentifier3rdaft = offerproductidentifier3rdaft___val
      result.serviceremovalgraceperiodaft = serviceremovalgraceperiodaft___val
      result.account6threfilldivamountbef = account6threfilldivamountbef___val
      result.usageaccumulator1stidbef = usageaccumulator1stidbef___val
      result.clearedunits6thaft = clearedunits6thaft___val
      result.rechargedivpartpda8th = rechargedivpartpda8th___val
      result.clearedaccount1stvalueaft = clearedaccount1stvalueaft___val
      result.offertype3rdbef = inpData(379)
      result.dedicatedaccountunit10thbef = dedicatedaccountunit10thbef___val
      result.dedicatedaccountunit9thbef = dedicatedaccountunit9thbef___val
      result.offerstartdate4thaft = inpData(525)
      result.refilldivunits6thaft = refilldivunits6thaft___val
      result.offerstartdate5thaft = inpData(532)
      result.offer8thidentifierbef = offer8thidentifierbef___val
      result.offerstartdatetime6thbef = inpData(402)
      result.communityidbef3 = inpData(124)
      result.usageaccumulator1stidaft = usageaccumulator1stidaft___val
      result.dedicatedaccountunit8thbef = dedicatedaccountunit8thbef___val
      result.servicefeedayssurplus = servicefeedayssurplus___val
      result.clearedunits7thaft = clearedunits7thaft___val
      result.usageaccumulator7thvalueaft = usageaccumulator7thvalueaft___val
      result.offerexpirydatetime1stbef = inpData(368)
      result.usageaccumulator8thvaluebef = usageaccumulator8thvaluebef___val
      result.clearedaccount2ndvaluebef = clearedaccount2ndvaluebef___val
      result.usageaccumulator3rdidaft = usageaccumulator3rdidaft___val
      result.offerexpirydate6thbef = inpData(399)
      result.market_id = market_id___val
      result.subscribernumber = inpData(24)
      result.offerproductidentifier1staft = offerproductidentifier1staft___val
      result.account9threfilldivamountaft = account9threfilldivamountaft___val
      result.offerexpirydatetime8thaft = inpData(558)
      result.account7threfilldivamountaft = account7threfilldivamountaft___val
      result.offer4thidentifieraft = offer4thidentifieraft___val
      result.offerexpirydatetime3rdaft = inpData(523)
      result.offerproductidentifier7thbef = offerproductidentifier7thbef___val
      result.clearedaccount8thvalueaft = clearedaccount8thvalueaft___val
      result.accumulatedprogconteraft = accumulatedprogconteraft___val
      result.accountstartdate10thaft = inpData(497)
      result.promotionplanallocenddate = inpData(247)
      result.offerexpirydatetime8thbef = inpData(417)
      result.offerproductidentifier5thbef = offerproductidentifier5thbef___val
      result.offerstartdate1stbef = inpData(363)
      result.activationdate = inpData(231)
      result.account3rdrefilpromodivamntaft = account3rdrefilpromodivamntaft___val
      result.accountstartdate5thaft = inpData(462)
      result.dedicatedaccountunit6thaft = dedicatedaccountunit6thaft___val
      result.clearedaccount1stvaluebef = clearedaccount1stvaluebef___val
      result.offerproductidentifier8thaft = offerproductidentifier8thaft___val
      result.accountexpiry4thdateaft = inpData(256)
      result.usageaccumulator6thidaft = usageaccumulator6thidaft___val
      result.clearedunits4thbef = clearedunits4thbef___val
      result.accountcurrencycleared = inpData(242)
      result.temporaryserviceclassaft = temporaryserviceclassaft___val
      result.refillpromodivunits1staft = refillpromodivunits1staft___val
      result.dedicatedaccountunit1staft = dedicatedaccountunit1staft___val
      result.clearedaccount2ndvalueaft = clearedaccount2ndvalueaft___val
      result.clearedunits1staft = clearedunits1staft___val
      result.usageaccumulator9thvalueaft = usageaccumulator9thvalueaft___val
      result.refillpromodivunits4thaft = refillpromodivunits4thaft___val
      result.offerexpirydate5thaft = inpData(533)
      result.refilldivunits5thbef = refilldivunits5thbef___val
      result.accountstartdate9thaft = inpData(490)
      result.accumulatedprogressionvalueres = accumulatedprogressionvalueres___val
      result.account9thcampaignidentbef = account9thcampaignidentbef___val
      result.usageaccumulator4thidbef = usageaccumulator4thidbef___val
      result.offertype6thbef = inpData(400)
      result.account2ndrefilpromodivamntbef = account2ndrefilpromodivamntbef___val
      result.offerstartdate2ndaft = inpData(511)
      result.voucheragent = inpData(233)
      result.account4threfilpromodivamntaft = account4threfilpromodivamntaft___val
      result.usageaccumulator9thidbef = usageaccumulator9thidbef___val
      result.dedicatedaccountunit6thbef = dedicatedaccountunit6thbef___val
      result.accountnumber = inpData(22)
      result.offer1stidentifierbef = offer1stidentifierbef___val
      result.currentserviceclass = currentserviceclass___val
      result.localsequencenumber = localsequencenumber___val
      result.account1strefillpromdivamntbef = account1strefillpromdivamntbef___val
      result.offerexpirydate4thaft = inpData(526)
      result.refillamountconverted = refillamountconverted___val
      result.accountcurrency = inpData(23)
      result.dedicatedaccountunit2ndaft = dedicatedaccountunit2ndaft___val
      result.offertype7thaft = inpData(548)
      result.clearedaccount3rdvaluebef = clearedaccount3rdvaluebef___val
      result.dedicatedaccount3rdidaft = dedicatedaccount3rdidaft___val
      result.transactiontype = inpData(11)
      result.account10threfilldivamountaft = account10threfilldivamountaft___val
      result.dedicatedaccountunit7thbef = dedicatedaccountunit7thbef___val
      result.refillpromodivunits10thbef = refillpromodivunits10thbef___val
      result.account2ndcampaignidentaft = account2ndcampaignidentaft___val
      result.usageaccumulator4thvaluebef = usageaccumulator4thvaluebef___val
      result.servicefeeexpirydateaft = inpData(197)
      result.clearedaccount5thvalueaft = clearedaccount5thvalueaft___val
      result.account9threfilpromodivamntbef = account9threfilpromodivamntbef___val
      result.voucheractivationcode = inpData(241)
      result.account9threfilldivamountbef = account9threfilldivamountbef___val
      result.refillpromodivunits2ndbef = refillpromodivunits2ndbef___val
      result.offerstartdatetime4thbef = inpData(388)
      result.clearedaccount7thvalueaft = clearedaccount7thvalueaft___val
      result.realmoneyflag6thbef = realmoneyflag6thbef___val
      result.account2ndrefilldivamountaft = account2ndrefilldivamountaft___val
      result.offerstartdate3rdbef = inpData(377)
      result.account4thbalancebef = account4thbalancebef___val
      result.unitbalance2ndaft = unitbalance2ndaft___val
      result.ignoreserviceclasshierarchy = inpData(243)
      result.usageaccumulator7thidaft = usageaccumulator7thidaft___val
      result.clearedaccount9thvalueaft = clearedaccount9thvalueaft___val
      result.offerproductidentifier8thbef = offerproductidentifier8thbef___val
      result.accountstartdate7thaft = inpData(476)
      result.accountexpiry3rddatebef = inpData(253)
      result.account8thcampaignidentbef = account8thcampaignidentbef___val
      result.account5thcampaignidentaft = account5thcampaignidentaft___val
      result.unitbalance8thaft = unitbalance8thaft___val
      result.dedicatedaccount8thidbef = dedicatedaccount8thidbef___val
      result.usageaccumulator10thvalueaft = usageaccumulator10thvalueaft___val
      result.account4threfilldivamountaft = account4threfilldivamountaft___val
      result.refillprofileid = inpData(18)
      result.transactioncurrency = inpData(14)
      result.aggregatedbalanceaft = aggregatedbalanceaft___val
      result.unitbalance1stbef = unitbalance1stbef___val
      result.offerstartdate6thbef = inpData(398)
      result.account8thbalanceaft = account8thbalanceaft___val
      result.rechargedivpartda1st = rechargedivpartda1st___val
      result.dedicatedaccountunit7thaft = dedicatedaccountunit7thaft___val
      result.offerproductidentifier1stbef = offerproductidentifier1stbef___val
      result.usageaccumulator6thvalueaft = usageaccumulator6thvalueaft___val
      result.offer3rdidentifierbef = offer3rdidentifierbef___val
      result.accountexpiry9thdatebef = inpData(265)
      result.locationnumber = inpData(240)
      result.rechargedivpartpda5th = rechargedivpartpda5th___val
      result.rechargedivpartpda6th = rechargedivpartpda6th___val
      result.offerexpirydatetime4thaft = inpData(530)
      result.account10threfilpromdivamntbef = account10threfilpromdivamntbef___val
      result.rechargedivpartpda7th = rechargedivpartpda7th___val
      result.usageaccumulator1stvalueaft = usageaccumulator1stvalueaft___val
      result.clearedunits4thaft = clearedunits4thaft___val
      result.rechargedivpartda8th = rechargedivpartda8th___val
      result.rechargedivpartda6th = rechargedivpartda6th___val
      result.offerproductidentifier6thaft = offerproductidentifier6thaft___val
      result.voucherserialnumber = inpData(20)
      result.offerstartdatetime10thaft = inpData(571)
      result.accountexpiry7thdatebef = inpData(261)
      result.account3rdcampaignidentbef = account3rdcampaignidentbef___val
      result.refilldivunits8thaft = refilldivunits8thaft___val
      result.realmoneyflag10thbef = realmoneyflag10thbef___val
      result.communityidaft2 = inpData(222)
      result.refillpromodivunits2ndaft = refillpromodivunits2ndaft___val
      result.accountexpiry2nddateaft = inpData(252)
      result.offerexpirydatetime1staft = inpData(509)
      result.offerexpirydate3rdbef = inpData(378)
      result.refilldivunits2ndaft = refilldivunits2ndaft___val
      result.dedicatedaccountunit3rdaft = dedicatedaccountunit3rdaft___val
      result.creditclearanceperiodaft = creditclearanceperiodaft___val
      result.account5thcampaignidentbef = account5thcampaignidentbef___val
      result.offerproductidentifier2ndaft = offerproductidentifier2ndaft___val
      result.accumulatedprogressionvaluebef = accumulatedprogressionvaluebef___val
      result.offerexpirydate1staft = inpData(505)
      result.realmoneyflag9thaft = realmoneyflag9thaft___val
      result.accumulatedprogrcounterbef = accumulatedprogrcounterbef___val
      result.serviceofferingbef = inpData(100)
      result.accountstartdate4thaft = inpData(455)
      result.accountstartdate9thbef = inpData(349)
      result.dedicatedaccount6thidbef = dedicatedaccount6thidbef___val
      result.account8threfilpromodivamntbef = account8threfilpromodivamntbef___val
      result.offer5thidentifierbef = offer5thidentifierbef___val
      result.offerexpirydatetime6thaft = inpData(544)
      result.realmoneyflag2ndaft = realmoneyflag2ndaft___val
      result.clearedunits1stbef = clearedunits1stbef___val
      result.originnodetype = inpData(0)
      result.realmoneyflag8thbef = realmoneyflag8thbef___val
      result.account4thcampaignidentaft = account4thcampaignidentaft___val
      result.offerexpirydate4thbef = inpData(385)
      result.vouchergroupid = inpData(21)
      result.offer1stidentifieraft = offer1stidentifieraft___val
      result.refillpromodivunits6thaft = refillpromodivunits6thaft___val
      result.offerstartdate9thbef = inpData(419)
      result.account10thbalanceaft = account10thbalanceaft___val
      result.permanentserviceclassbef = permanentserviceclassbef___val
      result.rechargedivpartpmain = rechargedivpartpmain___val
      result.offerproductidentifier5thaft = offerproductidentifier5thaft___val
      result.rechargedivpartpda2nd = rechargedivpartpda2nd___val
      result.offerstartdatetime3rdaft = inpData(522)
      result.accountbalanceaft = accountbalanceaft___val
      result.offer9thidentifieraft = offer9thidentifieraft___val
      result.account1strefilldivamountaft = account1strefilldivamountaft___val
      result.offerexpirydate1stbef = inpData(364)
      result.account10thcampaignidentaft = account10thcampaignidentaft___val
      result.offerexpirydate3rdaft = inpData(519)
      result.clearedunits3rdbef = clearedunits3rdbef___val
      result.filename = inpData(577)
      result.rechargedivpartpda4th = rechargedivpartpda4th___val
      result.offerexpirydate7thaft = inpData(547)
      result.offerstartdate3rdaft = inpData(518)
      result.offertype4thbef = inpData(386)
      result.supervisionexpirydatebef = inpData(101)
      result.offerproductidentifier7thaft = offerproductidentifier7thaft___val
      result.usageaccumulator5thvaluebef = usageaccumulator5thvaluebef___val
      result.refilldivunits10thaft = refilldivunits10thaft___val
      result.serviceofferingaft = inpData(199)
      result.offerstartdate10thbef = inpData(426)
      result.account2ndrefilldivamountbef = account2ndrefilldivamountbef___val
      result.externaldata3 = inpData(238)
      result.accountstartdate3rdaft = inpData(448)
      result.permanentserviceclassaft = permanentserviceclassaft___val
      result.realmoneyflag10thaft = realmoneyflag10thaft___val
      result.account3rdbalanceaft = account3rdbalanceaft___val
      result.subscriberregion = inpData(245)
      result.offerstartdatetime2ndaft = inpData(515)
      result.account2ndcampaignidentifbef = account2ndcampaignidentifbef___val
      result.unitbalance4thaft = unitbalance4thaft___val
      result.offertype9thaft = inpData(562)
      result.usageaccumulator3rdidbef = usageaccumulator3rdidbef___val
      result.account3rdcampaignidentaft = account3rdcampaignidentaft___val
      result.unitbalance4thbef = unitbalance4thbef___val
      result.welcomestatus = welcomestatus___val
      result.offerstartdatetime1stbef = inpData(367)
      result.dedicatedaccountunit5thaft = dedicatedaccountunit5thaft___val
      result.offerproductidentifier3rdbef = offerproductidentifier3rdbef___val
      result.account5threfilpromodivamntaft = account5threfilpromodivamntaft___val
      result.voucherbasedrefill = inpData(10)
      result.dedicatedaccount5thidaft = dedicatedaccount5thidaft___val
      result.account1stcampaignidentaft = account1stcampaignidentaft___val
      result.dedicatedaccount4thidbef = dedicatedaccount4thidbef___val
      result.refillpromodivunits7thbef = refillpromodivunits7thbef___val
      result.offerexpirydatetime10thaft = inpData(572)
      result.offertype3rdaft = inpData(520)
      result.offerproductidentifier9thaft = offerproductidentifier9thaft___val
      result.usageaccumulator4thvalueaft = usageaccumulator4thvalueaft___val
      result.offerexpirydatetime10thbef = inpData(431)
      result.usageaccumulator10thidaft = usageaccumulator10thidaft___val
      result.usageaccumulator1stvaluebef = usageaccumulator1stvaluebef___val
      result.refilldivisionamount = refilldivisionamount___val
      result.accountexpiry8thdatebef = inpData(263)
      result.account4thcampaignidentbef = account4thcampaignidentbef___val
      result.offer7thidentifierbef = offer7thidentifierbef___val
      result.offerexpirydate5thbef = inpData(392)
      result.refillpromodivunits10thaft = refillpromodivunits10thaft___val
      result.rechargedivpartda10th = rechargedivpartda10th___val
      result.accountstartdate2ndbef = inpData(300)
      result.offerexpirydatetime2ndbef = inpData(375)
      result.offerexpirydatetime5thbef = inpData(396)
      result.origintransactionid = inpData(3)
      result.account3rdrefilldivamountbef = account3rdrefilldivamountbef___val
      result.cellidentifier = inpData(574)
      result.refilldivunits4thbef = refilldivunits4thbef___val
      result.hub_id = hub_id___val
      result.temporaryservclassexpdatebef = inpData(96)
      result.offerstartdatetime9thaft = inpData(564)
      result.refilldivunits4thaft = refilldivunits4thaft___val
      result.promotionplan = inpData(93)
      result.usageaccumulator2ndvaluebef = usageaccumulator2ndvaluebef___val
      result.realmoneyflag5thbef = realmoneyflag5thbef___val
      result.refilltype = refilltype___val
      result.offerstartdatetime10thbef = inpData(430)
      result.refilldivunits1stbef = refilldivunits1stbef___val
      result.communityidbef1 = inpData(122)
      result.accountexpiry1stdateaft = inpData(250)
      result.refillpromodivunits7thaft = refillpromodivunits7thaft___val
      result.accountbalancebef = accountbalancebef___val
      result.clearedunits10thbef = clearedunits10thbef___val
      result.segmentationid = inpData(19)
      result.originoperatorid = inpData(4)
      result.accountexpiry1stdatebef = inpData(249)
      result.accountstartdate5thbef = inpData(321)
      result.unitbalance8thbef = unitbalance8thbef___val
      result.account1strefilpromodivamntaft = account1strefilpromodivamntaft___val
      result.origintimestamp = inpData(5)
      result.offertype4thaft = inpData(527)
      result.account6thcampaignidentaft = account6thcampaignidentaft___val
      result.refilldivunits3rdbef = refilldivunits3rdbef___val
      result.dedicatedaccount2ndidaft = dedicatedaccount2ndidaft___val
      result.offertype8thaft = inpData(555)
      result.unitbalance5thaft = unitbalance5thaft___val
      result.accumulatedrefillvalueaft = accumulatedrefillvalueaft___val
      result.account6threfilpromodivamntaft = account6threfilpromodivamntaft___val
      result.rechargedivpartda5th = rechargedivpartda5th___val
      result.account7thbalancebef = account7thbalancebef___val
      result.refilldivunits8thbef = refilldivunits8thbef___val
      result.usageaccumulator6thvaluebef = usageaccumulator6thvaluebef___val
      result.offerproductidentifier10thaft = offerproductidentifier10thaft___val
      result.account8threfilpromodivamntaft = account8threfilpromodivamntaft___val
      result.account7thcampaignidentaft = account7thcampaignidentaft___val
      result.unitbalance3rdbef = unitbalance3rdbef___val
      result.account10thcampaignidentbef = account10thcampaignidentbef___val
      result.account6thbalancebef = account6thbalancebef___val
      result.accountexpiry5thdateaft = inpData(258)
      result.clearedaccount5thvaluebef = clearedaccount5thvaluebef___val
      result.account3rdrefilpromodivamntbef = account3rdrefilpromodivamntbef___val
      result.account6threfilpromodivamntbef = account6threfilpromodivamntbef___val
      result.rechargedivpartpda1st = rechargedivpartpda1st___val
      result.account7thbalanceaft = account7thbalanceaft___val
      result.offerstartdate4thbef = inpData(384)
      result.offertype6thaft = inpData(541)
      result.dedicatedaccount1stidaft = dedicatedaccount1stidaft___val
      result.usageaccumulator5thidbef = usageaccumulator5thidbef___val
      result.unitbalance3rdaft = unitbalance3rdaft___val
      result.accountstartdate1staft = inpData(434)
      result.offerstartdate2ndbef = inpData(370)
      result.communityidaft3 = inpData(223)
      result.dedicatedaccount8thidaft = dedicatedaccount8thidaft___val
      result.offerstartdate1staft = inpData(504)
      result.offerstartdate6thaft = inpData(539)
      result.offerexpirydate9thbef = inpData(420)
      result.account1strefilldivamountbef = account1strefilldivamountbef___val
      result.originhostname = inpData(1)
      result.accountexpiry3rddateaft = inpData(254)
      result.voucherregion = inpData(246)
      result.usageaccumulator7thidbef = usageaccumulator7thidbef___val
      result.accountexpiry9thdateaft = inpData(266)
      result.refilldivunits7thbef = refilldivunits7thbef___val
      result.clearedunits6thbef = clearedunits6thbef___val
      result.usageaccumulator3rdvaluebef = usageaccumulator3rdvaluebef___val
      result.accumulatedrefillvaluebef = accumulatedrefillvaluebef___val
      result.supervisiondayssurplus = supervisiondayssurplus___val
      result.clearedunits9thaft = clearedunits9thaft___val
      result.dedicatedaccount10thidbef = dedicatedaccount10thidbef___val
      result.accountstartdate1stbef = inpData(293)
      result.unitbalance6thaft = unitbalance6thaft___val
      result.refilloptionbef = inpData(97)
      result.offerstartdatetime7thbef = inpData(409)
      result.realmoneyflag6thaft = realmoneyflag6thaft___val
      result.servicefeedayspromopart = servicefeedayspromopart___val
      result.externaldata1 = inpData(236)
      result.dedicatedaccountunit10thaft = dedicatedaccountunit10thaft___val
      result.account1stbalancebef = account1stbalancebef___val
      result.refillpromodivunits8thbef = refillpromodivunits8thbef___val
      result.account5threfilldivamountbef = account5threfilldivamountbef___val
      result.usageaccumulator6thidbef = usageaccumulator6thidbef___val
      result.clearedunits2ndaft = clearedunits2ndaft___val
      result.dedicatedaccount2ndidbef = dedicatedaccount2ndidbef___val
      result.accountstartdate4thbef = inpData(314)
      result.accountstartdate6thaft = inpData(469)
      result.temporaryserviceclassbef = temporaryserviceclassbef___val
      result.aggregatedbalancebef = aggregatedbalancebef___val
      result.offerexpirydatetime4thbef = inpData(389)
      result.offerexpirydate8thbef = inpData(413)
      result.accountstartdate8thbef = inpData(342)
      result.realmoneyflag9thbef = realmoneyflag9thbef___val
      result.offer4thidentifierbef = offer4thidentifierbef___val
      result.account8thbalancebef = account8thbalancebef___val
      result.clearedaccount7thvaluebef = clearedaccount7thvaluebef___val
      result.clearedaccount10thvaluebef = clearedaccount10thvaluebef___val
      result.usageaccumulator2ndidaft = usageaccumulator2ndidaft___val
      result.rechargedivpartda3rd = rechargedivpartda3rd___val
      result.dedicatedaccount10thidaft = dedicatedaccount10thidaft___val
      result.externaldata2 = inpData(237)
      result.realmoneyflag4thaft = realmoneyflag4thaft___val
      result.rechargedivpartda9th = rechargedivpartda9th___val
      result.supervisiondayspromopart = supervisiondayspromopart___val
      result.offerstartdatetime5thaft = inpData(536)
      result.refillpromodivunits5thaft = refillpromodivunits5thaft___val
      result.maximumsupervisionperiod = maximumsupervisionperiod___val
      result.account7threfilpromodivamntbef = account7threfilpromodivamntbef___val
      result.refilldivunits9thaft = refilldivunits9thaft___val
      result.clearedaccount10thvalueaft = clearedaccount10thvalueaft___val
      result.realmoneyflag8thaft = realmoneyflag8thaft___val
      result.offer9thidentifierbef = offer9thidentifierbef___val
      result.clearedaccount4thvaluebef = clearedaccount4thvaluebef___val
      result.dedicatedaccountunit2ndbef = dedicatedaccountunit2ndbef___val
      result.offer7thidentifieraft = offer7thidentifieraft___val
      result.realmoneyflag1stbef = realmoneyflag1stbef___val
      result.offerexpirydate10thaft = inpData(568)
      result.accountexpiry10thdateaft = inpData(268)
      result.clearedunits2ndbef = clearedunits2ndbef___val
      result.refilldivunits2ndbef = refilldivunits2ndbef___val
      result.dedicatedaccountunit1stbef = dedicatedaccountunit1stbef___val
      result.rechargedivpartpda3rd = rechargedivpartpda3rd___val
      result.offerexpirydatetime6thbef = inpData(403)
      result.offertype2ndaft = inpData(513)
      result.account3rdbalancebef = account3rdbalancebef___val
      result.offerexpirydatetime9thaft = inpData(565)
      result.offertype5thaft = inpData(534)
      result.clearedaccount4thvalueaft = clearedaccount4thvalueaft___val
      result.usageaccumulator5thidaft = usageaccumulator5thidaft___val
      result.offerstartdate9thaft = inpData(560)
      result.accountexpiry7thdateaft = inpData(262)
      result.dedicatedaccount3rdidbef = dedicatedaccount3rdidbef___val
      result.account6thbalanceaft = account6thbalanceaft___val
      result.offer5thidentifieraft = offer5thidentifieraft___val
      result.rechargedivpartpda10th = rechargedivpartpda10th___val
      result.accountstartdate3rdbef = inpData(307)
      result.accountexpiry4thdatebef = inpData(255)
      result.offerstartdate7thbef = inpData(405)
      result.realmoneyflag3rdbef = realmoneyflag3rdbef___val
      if (result.hasTimePartitionInfo) result.setTimePartitionData ;
      if(context.CurrentErrors()==0) {
        Array(result)
      } else {
        Array.empty[MessageInterface]
      }
    }
  } catch {
    case e: AbortOutputException => {
      context.AddError(e.getMessage)
      Array.empty[MessageInterface]
    }
    case e: Exception => {
      Debug("Exception: o1:" + e.getMessage)
      throw e
    }
  }
}
class common_exeGenerated_AirRefillCS5_1_1_process_oo(conversion: com.ligadata.runtime.Conversion,
                                                      log : com.ligadata.runtime.Log,
                                                      context: com.ligadata.runtime.JtmContext,
                                                      common: common_exeGenerated_AirRefillCS5_1_1,
                                                      msg1: com.ligadata.messages.V1000000.AirRefillCS5_input) {
  import log._
  import common._
  val result: Array[MessageInterface]= try {
    if (!((context.CurrentErrors() > 0))) {
      Debug("Filtered: AirRefillCS5_1@oo")
      Array.empty[MessageInterface]
    } else {
      val result = com.ligadata.gen.V1000000.RejectedDataMsg.createInstance
      result.reasons = context.CurrentErrorList() :+ empty_str
      result.origmsg = msg1.msg
      if (result.hasTimePartitionInfo) result.setTimePartitionData ;
      Array(result)
    }
  } catch {
    case e: AbortOutputException => {
      context.AddError(e.getMessage)
      Array.empty[MessageInterface]
    }
    case e: Exception => {
      Debug("Exception: oo:" + e.getMessage)
      throw e
    }
  }
}
class test_jtm(factory: ModelInstanceFactory) extends ModelInstance(factory) {
  val conversion = new com.ligadata.runtime.Conversion
  val log = new com.ligadata.runtime.Log(this.getClass.getName)
  val context = new com.ligadata.runtime.JtmContext
  import log._
  // Model code start
  def HandleError(fieldName : String, checkName : String) : Unit = {
    val err = fieldName + " - check (" + checkName + ") violated "
    context.AddError(err)
  }
  // Model code end
  override def execute(txnCtxt: TransactionContext, execMsgsSet: Array[ContainerOrConcept], triggerdSetIndex: Int, outputDefault: Boolean): Array[ContainerOrConcept] = {
    context.Reset(); // Resetting the JtmContext before executing the model
    if (isTraceEnabled)
      Trace(s"Model::execute transid=%d triggeredset=%d outputdefault=%s".format(txnCtxt.transId, triggerdSetIndex, outputDefault.toString))
    if(isDebugEnabled)
    {
      execMsgsSet.foreach(m => Debug( s"Input: %s -> %s".format(m.getFullTypeName, m.toString())))
    }
    // Grok parts
    // Model methods
    def exeGenerated_AirRefillCS5_1_1(msg1: com.ligadata.messages.V1000000.AirRefillCS5_input): Array[MessageInterface] = {
      Debug("exeGenerated_AirRefillCS5_1_1")
      context.SetSection("AirRefillCS5_1")
      val common = new common_exeGenerated_AirRefillCS5_1_1(conversion, log, context, msg1)
      def process_o1(): Array[MessageInterface] = {
        Debug("exeGenerated_AirRefillCS5_1_1::process_o1")
        context.SetScope("o1")
        val result = new common_exeGenerated_AirRefillCS5_1_1_process_o1(conversion, log, context, common, msg1)
        result.result
      }
      def process_oo(): Array[MessageInterface] = {
        Debug("exeGenerated_AirRefillCS5_1_1::process_oo")
        context.SetScope("oo")
        val result = new common_exeGenerated_AirRefillCS5_1_1_process_oo(conversion, log, context, common, msg1)
        result.result
      }
      try {
        process_o1()++
          process_oo()
      } catch {
        case e: AbortTransformationException => {
          return Array.empty[MessageInterface]
        }
      }
    }
    // Evaluate messages
    val msgs = execMsgsSet.map(m => m.getFullTypeName -> m).toMap
    val msg1 = msgs.getOrElse("com.ligadata.messages.AirRefillCS5_input", null).asInstanceOf[com.ligadata.messages.V1000000.AirRefillCS5_input]
    // Main dependency -> execution check
    // Create result object
    val results: Array[MessageInterface] =
    try {
      (if(msg1!=null) {
        exeGenerated_AirRefillCS5_1_1(msg1)
      } else {
        Array.empty[MessageInterface]
      }) ++
        Array.empty[MessageInterface]
    } catch {
      case e: AbortExecuteException => {
        Array.empty[MessageInterface]
      }
    }
    if(isDebugEnabled)
    {
      results.foreach(m => Debug( s"Output: %s -> %s".format(m.getFullTypeName, m.toString())))
    }
    results.asInstanceOf[Array[ContainerOrConcept]]
  }
}
