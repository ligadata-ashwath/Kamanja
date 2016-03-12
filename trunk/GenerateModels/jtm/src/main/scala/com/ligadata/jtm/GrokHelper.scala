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
package com.ligadata.jtm

import java.io.{StringReader, File}
import com.ligadata.jtm.nodes.{Root, Grok}
import org.aicer.grok.dictionary.GrokDictionary

/**
  *
  */
class GrokHelper extends LogTrait {

  /** Returns the list of files after validating
    *
    * @param root
    * @return
    */
  def FileList(root: Root): Array[String] = {
      Validate(root.grok)
      FileList(root.grok)
  }

  /**
    *
    * @param grok
    * @return
    */
  def Validate(grok: Grok): GrokDictionary = {

    val dict = new GrokDictionary

    if(grok.builtInDictionary) {
      dict.addBuiltInDictionaries()
    }

    // Add all the grok dictionaries
    val filecheck = grok.file.foldLeft(Array.empty[String])( (r, f) => {
      val file = new File(f)
      if( file.exists) {
        dict.addDictionary(file)
        r
      } else {
        r :+ "Grok file %s not found".format(f)
      }
    })

    if(filecheck.length > 0) {
      throw new Exception(filecheck.mkString("\n"))
    }

    // Walk through the dictionay
    grok.patterns.map( p =>
      dict.addDictionary(new StringReader(p._1 + " " + p._2))
    )

    dict.bind
    dict
  }

  /** Extracts the set with the unique files names to load
    *
    * @param groks
    * @return
    */
  private def FileList(groks: scala.collection.Map[String, Grok]): Array[String] = {

    val filelist = groks.foldLeft(Set.empty[String]) ( (r, grok) => {
        grok._2.file.foldLeft(r) ((r, f) => {
          r + f
      })
    })

    filelist.toArray
  }

  /** Validates all the element in the map with the grok configuration
    *
    * @param groks
    * @return
    */
  private def Validate(groks: scala.collection.Map[String, Grok]): Unit= {
      groks.map(grok => Validate(grok._2))
  }

}
