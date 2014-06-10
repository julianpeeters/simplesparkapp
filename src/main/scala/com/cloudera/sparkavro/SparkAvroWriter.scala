/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.sparkavro

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.avro.mapreduce.{AvroKeyOutputFormat, AvroJob}
import org.apache.avro.mapred.AvroKey
import org.apache.avro.Schema.Parser

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.Job

import com.miguno.avro.twitter_schema

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator

class MyRegistrator extends KryoRegistrator {
  override def registerClasses(kryo: Kryo) {
    kryo.register(classOf[twitter_schema])
  }
}


object SparkAvroWriter {
  def main(args: Array[String]) {

  val outPath = args(0)
    val sconf = new SparkConf().setAppName("Spark Count")
      sconf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      sconf.set("spark.kryo.registrator", "com.cloudera.sparkavro.MyRegistrator")
    val sctx = new SparkContext(sconf)

    val tweet1 = new twitter_schema("Ludwig", "Words are deeds.", 1L)

    val tweet2 = new twitter_schema("Bernie", "The time you enjoy wasting is not wasted time.", 2L)

      println(tweet2)

    val records = sctx.parallelize(Array(tweet1, tweet2))

      records.foreach(println)

    val withValues = records.map((x) => (new AvroKey(x), NullWritable.get))

    val conf = new Job()
    FileOutputFormat.setOutputPath(conf, new Path(outPath))
    AvroJob.setOutputKeySchema(conf, twitter_schema.SCHEMA$)
    conf.setOutputFormatClass(classOf[AvroKeyOutputFormat[Any]])
    withValues.saveAsTextFile(outPath)

  }
}
