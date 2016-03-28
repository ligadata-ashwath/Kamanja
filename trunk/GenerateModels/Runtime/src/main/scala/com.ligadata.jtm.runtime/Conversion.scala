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
package com.ligadata.runtime

import java.util.Date

import scala.math.Ordering.BigIntOrdering

/*
 jtm loctions

 // Each step can cause an error
 // track back to the originating instruction
 transformation -> grok instance
 transfomation -> [dependency set] -> compute
 transfomation -> [dependency set] -> output -> compute|where|mapping

a) string
b) id -> to map

*/
class Conversion {

  var errors : Map[Integer, String] = Map.empty[Integer, String]

  // Source -> Map[Target, Function]
  val builtin: Map[String, Map[String, String]] = Map(
    "Integer" -> Map("String" -> "ToString"),
    "Double" -> Map("String" -> "ToString"),
    "Boolean" -> Map("String" -> "ToString"),
    "Date" -> Map("String" -> "ToString"),
    "BigDecimal" -> Map("String" -> "ToString"),
    "String" -> Map("Integer" -> "ToInteger",
                    "Double" -> "ToDouble",
                    "Boolean" -> "ToBoolean",
                    "Date" -> "ToDate",
                    "BigDecimal" -> "ToBigDecimal")
  )

  def ToString(context: Integer, v: BigInt): String = {
    null
  }

  def ToDate(context: Integer, s: String): Date = {
    null
  }
  def ToInteger(context: Integer, s: String): Integer = {
    null
  }
  def ToBigInt(context: Integer, s: String): BigInt = {
    null
  }
  def ToDecimal(context: Integer, s: String): BigDecimal = {
    null
  }
  def ToDouble(context: Integer, s: String): Double = {
    0.0
  }

}