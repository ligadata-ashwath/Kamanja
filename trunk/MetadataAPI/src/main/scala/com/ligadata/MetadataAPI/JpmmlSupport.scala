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


import com.ligadata.KamanjaBase.FactoryOfModelInstanceFactory
import com.ligadata.kamanja.metadata.MiningModelType._

import com.ligadata.Serialize._
import org.dmg.pmml._

import scala.collection.JavaConverters._

import java.io.{ByteArrayInputStream, PushbackInputStream, InputStream}
import java.nio.charset.StandardCharsets
import javax.xml.bind.{ValidationEvent, ValidationEventHandler}
import javax.xml.transform.sax.SAXSource
import java.util.{List => JList}

import com.ligadata.kamanja.metadata._
import org.jpmml.model.{JAXBUtil, ImportFilter}
import org.jpmml.evaluator._
import org.xml.sax.InputSource
import org.xml.sax.helpers.XMLReaderFactory
import org.jpmml.sas._


/**
  * JpmmlSupport - Add, rebuild, and remove of PMML based models from the Kamanja metadata store.
  *
  * It builds an instance of the shim model with a JPMML evaluator appropriate for the supplied InputStream
  * containing the pmml model text.
  *
  * @param mgr            the active metadata manager instance
  * @param modelNamespace the namespace for the model
  * @param modelName      the name of the model
  * @param version        the version of the model in the form "MMMMMM.NNNNNN.mmmmmmm"
  * @param msgNamespace   the message namespace of the message that will be consumed by this model
  * @param msgName        the message name
  * @param msgVersion     the version of the message to be used for this model
  * @param pmmlText       the pmml to be ingested.
  */
class JpmmlSupport(mgr: MdMgr
                   , val modelNamespace: String
                   , val modelName: String
                   , val version: String
                   , val msgNamespace: String
                   , val msgName: String
                   , val msgVersion: String
                   , val pmmlText: String
                   , val ownerId: String
                   , val tenantId: String) extends LogTrait {

  /** Answer a ModelDef based upon the arguments supplied to the class constructor.
    *
    * @param recompile certain callers are creating a model to recompile the model when the message it consumes changes.
    *                  pass this flag as true in those cases to avoid com.ligadata.Exceptions.AlreadyExistsException
    * @return a ModelDef
    */
  def CreateModel(recompile: Boolean = false): ModelDef = {
    val reasonable: Boolean = (
      mgr != null &&
        modelNamespace != null && modelNamespace.nonEmpty &&
        modelName != null && modelName.nonEmpty &&
        version != null && version.nonEmpty &&
        msgNamespace != null && msgNamespace.nonEmpty &&
        msgName != null && msgName.nonEmpty &&
        pmmlText != null && pmmlText.nonEmpty
      )
    val modelDef: ModelDef = if (reasonable) {
      val inputStream: InputStream = new ByteArrayInputStream(pmmlText.getBytes(StandardCharsets.UTF_8))
      val is = new PushbackInputStream(inputStream)

      val reader = XMLReaderFactory.createXMLReader()
      reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
      val filter = new ImportFilter(reader)
      val source = new SAXSource(filter, new InputSource(is))
      val unmarshaller = JAXBUtil.createUnmarshaller
      unmarshaller.setEventHandler(SimpleValidationEventHandler)

      var pmml: PMML = unmarshaller.unmarshal(source).asInstanceOf[PMML]

      // 1320, 1313 Changes begin

      if (pmml.getHeader().getApplication().getName().contains("SAS")) {
        val visitor : Visitor  = new org.jpmml.sas.visitors.ExpressionCorrector()
        visitor.applyTo(pmml);

      }
      // 1320, 1313 Changes end
      
      val modelEvaluatorFactory = ModelEvaluatorFactory.newInstance()
      val modelEvaluator = modelEvaluatorFactory.newModelManager(pmml)


      val modelDe: ModelDef = if (modelEvaluator != null) {
        /*
         * Construct a ModelDef instance of com.ligadata.jpmml.JpmmlAdapter and the JPMML evaluator that will
         * be used to interpret messages for the modelNamespace.modelName.version supplied here.  The supplied
         * pmml is parsed to an org.dmg.pmml tree
         *
         * The FactoryOfModelInstanceFactoryDef instance contains the necessary jar and dependency info.
         *
         * An instance of the model is created with the appropriate evaluator is needed to
         *  a) obtain the output dependencies for the model, and
         *  b) generally vet this model to verify it is ingestable by the JpmmlEvaluator selected.
         */
        val onlyActive: Boolean = true
        val latestVersion: Boolean = true
        val facFacDefs: scala.collection.immutable.Set[FactoryOfModelInstanceFactoryDef] = mgr.ActiveFactoryOfMdlInstFactories
        val optJpmmlFacFac: Option[FactoryOfModelInstanceFactoryDef] = facFacDefs.filter(facfac => {
          facfac.ModelRepSupported == ModelRepresentation.PMML
        }).headOption
        val jpmmlFacFac: FactoryOfModelInstanceFactoryDef = optJpmmlFacFac.orNull

        val modelDefinition: ModelDef = if (jpmmlFacFac == null) {
          logger.error(s"While building model metadata for $modelNamespace.$modelName, it was discovered that there is no factory for this model representation (${ModelRepresentation.PMML}")
          null
        } else {
          val jarName: String = jpmmlFacFac.jarName
          val jarDeps: scala.Array[String] = jpmmlFacFac.dependencyJarNames
          val phyName: String = jpmmlFacFac.physicalName

          /** make sure new msg is there. */
          val msgver: Long = MdMgr.ConvertVersionToLong(msgVersion)
          val optInputMsg: Option[MessageDef] = mgr.Message(msgNamespace, msgName, msgver, onlyActive)
          val inputMsg: MessageDef = optInputMsg.orNull
          val activeFieldNames: JList[FieldName] = modelEvaluator.getActiveFields
          val outputFieldNames: JList[FieldName] = modelEvaluator.getOutputFields
          val targetFieldNames: JList[FieldName] = modelEvaluator.getTargetFields

          /** target|predicted usage types */

          /** NOTE: The following arrays depict the field names extracted from the model by the evaluator.
            * The activeFields DataField names are used at runtime to prepare the data for the model execution.
            * All the arrays are printed here reveal how to get the name and dataType from the activeFields,
            * targetFields, and outputFields.  These will be needed by the tool that will generate a message
            * definitions for the input and output.  We may still want to leave the fields logging
            * here... perhaps reducing the priority to debug or trace.
            */
            val activeFields: scala.Array[DataField] = {
                activeFieldNames.asScala.map(nm => modelEvaluator.getDataField(nm))
            }.toArray
            val targetFields: scala.Array[Target] = {
                targetFieldNames.asScala.filter(nm => modelEvaluator.getTarget(nm) != null).map(nm => modelEvaluator.getTarget(nm))
            }.toArray
            val outputFields: scala.Array[OutputField] = {
                outputFieldNames.asScala.filter(nm => modelEvaluator.getOutputField(nm) != null).map(nm => modelEvaluator.getOutputField(nm))
            }.toArray

            val activeFieldContent : scala.Array[(String,String)] = activeFields.map(fld => {
                (fld.getName.getValue, fld.getDataType.value)
            })
            val targetFieldContent : scala.Array[(String,String)] = if (targetFields != null && targetFields.size > 0) {
                targetFields.map(fld => {
                    val field : FieldName = fld.getField
                    val name : String = field.getValue
                    val datafield : DataField =  modelEvaluator.getDataField(field)
                    (datafield.getName.getValue, datafield.getDataType.value)

                })
            } else {
                scala.Array[(String,String)](("no","targetFields"))
            }
            val outputFieldContent : scala.Array[(String,String)] = if (outputFields != null && outputFields.size > 0) {
              outputFields.map(fld => {
                // 1323, 1319 Change begins
                  val fldName: String = if (fld != null && fld.getName != null) fld.getName.getValue else null
                  val fldTyp: String = if (fld != null && fld.getDataType != null) fld.getDataType.value else null
                  (fldName, fldTyp)
                // 1323, 1319 Change ends
                })
            } else {
                scala.Array[(String,String)](("no","outputFields"))
            }

            logger.info(s"\nActive Fields for $modelNamespace.$modelName: \n${activeFieldContent.toMap.toString}\n")
            logger.info(s"\nTarget Fields for $modelNamespace.$modelName: \n${targetFieldContent.toMap.toString}\n")
            logger.info(s"\nOutput Fields for $modelNamespace.$modelName: \n${outputFieldContent.toMap.toString}\n")

          val modelD: ModelDef = if (inputMsg != null) {

            /*
                                    val inVars: List[(String, String, String, String, Boolean, String)] =
                                        List[(String, String, String, String, Boolean, String)](("msg"
                                            , inputMsg.typeString
                                            , inputMsg.NameSpace
                                            , inputMsg.Name
                                            , false
                                            , null))

                                    /** fields found in the output section */
                                    val outputFields: scala.Array[OutputField] = {
                                        outputFieldNames.asScala.map(nm => modelEvaluator.getOutputField(nm))
                                    }.toArray
                                    val outputFieldVars: List[(String, String, String)] = outputFields.map(fld => {
                                        val fldName: String = fld.getName.getValue
                                        val dataType: String = if (fld.getDataType != null) fld.getDataType.value else "String"
                                        (fldName, MdMgr.SysNS, dataType)
                                    }).toList

                                    /** get the concrete data fields for either 'target' or 'predicted' ... type info found there. */
                                    val targetDataFields: scala.Array[DataField] = {
                                        targetFieldNames.asScala.map(nm => {
                                            modelEvaluator.getDataField(nm)
                                        })
                                    }.toArray
                                    val targVars: List[(String, String, String)] = targetDataFields.map(fld => {
                                        val fldName: String = fld.getName.getValue
                                        val dataType: String =  if (fld.getDataType != null) fld.getDataType.value else "String"
                                        (fldName, MdMgr.SysNS, dataType)
                                    }).toList

                                    /**
                                      * Model output fields will consist of the target variables (either target or predicted fields from mining
                                      * schema) and the fields found in the output section (if any)
                                      */
                                    val outVars: List[(String, String, String)] = (targVars ++ outputFieldVars).distinct
            */
            val inpMsgs = if (inputMsg != null) {
                val t = new MessageAndAttributes
                t.origin = "" //FIXME:- Fill this if looking for specific input
                t.message = inputMsg.FullName
                t.attributes = scala.Array[String]()
                scala.Array(t)
            }
            else {
                scala.Array[MessageAndAttributes]()
            }


            val isReusable: Boolean = true
            val supportsInstanceSerialization: Boolean = false // FIXME: not yet

            val withDots: Boolean = true
            val msgVersionFormatted: String = MdMgr.ConvertLongVersionToString(inputMsg.Version, !withDots)
            val model: ModelDef = mgr.MakeModelDef(modelNamespace
              , modelName
              , phyName
              , ownerId, tenantId, 0, 0
              , ModelRepresentation.PMML
              , scala.Array(inpMsgs)
              , scala.Array[String]()
              , isReusable
              , pmmlText
              , DetermineMiningModelType(modelEvaluator)
              , MdMgr.ConvertVersionToLong(version)
              , jarName
              , jarDeps
              , recompile
              , supportsInstanceSerialization)

            /** dump the model def to the log for time being */
            logger.debug(modelDefToString(model))
            model
          } else {
            logger.error(s"The supplied message def is not available in the metadata... msgName=$msgNamespace.$msgName.$msgVersion ... a model definition will not be created for model name=$modelNamespace.$modelName.$version")
            null
          }
          modelD
        }
        modelDefinition
      } else {
        logger.error(s"The JPMML evaluator could not be created for model $modelNamespace.$modelName.$version ... a model definition will not be created for model name=$modelNamespace.$modelName.$version")
        null
      }
      modelDe
    } else {
      logger.error(s"One or more arguments to JpmmlSupport.CreateModel were bad .. model name = $modelNamespace.$modelName, message name=$msgNamespace.$msgName, version=$version, pmmlText=$pmmlText")
      null
    }
    modelDef
  }

  /** Prepare a new model with the new PMML source supplied in the constructor.
    *
    * @return a newly constructed model def that reflects the new PMML source
    */
  def UpdateModel: ModelDef = {
    logger.debug("UpdateModel is a synonym for CreateModel")
    val recompile: Boolean = false
    CreateModel(recompile)
  }

  /**
    * Answer the kind of model that this is based upon the factory returned
    *
    * @param evaluator a ModelEvaluator
    * @return the MiningModelType
    */
  private def DetermineMiningModelType(evaluator: ModelEvaluator[_]): MiningModelType = {

    val modelType: MiningModelType = evaluator match {
      case a: AssociationModelEvaluator => ASSOCIATIONMODEL
      case c: ClusteringModelEvaluator => CLUSTERINGMODEL
      case g: GeneralRegressionModelEvaluator => GENERALREGRESSIONMODEL
      case m: MiningModelEvaluator => MININGMODEL
      case n: NaiveBayesModelEvaluator => NAIVEBAYESMODEL
      case nn: NearestNeighborModelEvaluator => NEARESTNEIGHBORMODEL
      case nn1: NeuralNetworkEvaluator => NEURALNETWORK
      case r: RegressionModelEvaluator => REGRESSIONMODEL
      case rs: RuleSetModelEvaluator => RULESETMODEL
      case sc: ScorecardEvaluator => SCORECARD
      case svm: SupportVectorMachineModelEvaluator => SUPPORTVECTORMACHINEMODEL
      case sc: TreeModelEvaluator => TREEMODEL
      case _ => UNKNOWN
    }
    modelType
  }

  /**
    * SimpleValidationEventHandler used by the JAXB Util that decomposes the PMML string supplied to CreateModel.
    */
  private object SimpleValidationEventHandler extends ValidationEventHandler {
    /**
      * Answer false whenever the validation event severity is ERROR or FATAL_ERROR.
      *
      * @param event a ValidationEvent issued by the JAXB SAX utility that is parsing the PMML source text.
      * @return flag to indicate whether to continue with the parse or not.
      */
    def handleEvent(event: ValidationEvent): Boolean = {
      val severity: Int = event.getSeverity
      severity match {
        case ValidationEvent.ERROR => false
        case ValidationEvent.FATAL_ERROR => false
        case _ => true
      }
    }
  }

  /** diagnostic... generate a JSON string to print to the log for the supplied ModelDef.
    *
    * @param modelDef the model def of interest
    * @return a JSON string representation of the ModelDef almost suitable for printing to log or console.
    */
  def modelDefToString(modelDef: ModelDef): String = {
    val abbreviatedModelSrc: String = if (modelDef.objectDefinition != null && modelDef.objectDefinition.length > 100) {
      modelDef.objectDefinition.take(99)
    } else {
      if (modelDef.objectDefinition != null) {
        modelDef.objectDefinition
      } else {
        "no source"
      }
    }

    var jsonStr: String = MetadataAPISerialization.serializeObjectToJson(modelDef)
    jsonStr
  }

}
