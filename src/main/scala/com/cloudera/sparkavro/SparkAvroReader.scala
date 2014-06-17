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

package com.miguno.avro
//package com.cloudera.sparkavro


import org.apache.spark.{SparkContext, SparkConf}
import org.apache.hadoop.mapreduce.Job
import org.apache.avro.mapreduce.AvroJob
import org.apache.avro.mapreduce.AvroKeyInputFormat
import org.apache.avro.mapred.AvroKey
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.conf.Configuration
import com.julianpeeters.avro.annotations._

@AvroTypeProvider("data/twitter.avro")
@AvroRecord
case class twitter_schema()
//case class twitter_schema(var username: String, var tweet: String, var timestamp: Long)

object SparkAvroReader {
  def main(args: Array[String]) {
    val inPath = args(0)

    val sparkConf = new SparkConf().setAppName("Spark Avro")
    val sc = new SparkContext(sparkConf)

    // A Schema must be specified, else `avro.mapreduce` tries to make one reflectively
    val c = new Configuration
    val job = new Job(c)
    val cf = job.getConfiguration
    FileInputFormat.setInputPaths(job, inPath)
    AvroJob.setInputKeySchema(job, twitter_schema().getSchema)

    val records = sc.newAPIHadoopRDD(job.getConfiguration,
      classOf[AvroKeyInputFormat[twitter_schema]],
      classOf[AvroKey[twitter_schema]],
      classOf[NullWritable])

    val tweetsAndNames = records.map(x =>
      (x._1.datum.username, x._1.datum.tweet))

    println("tweets and names: " + tweetsAndNames.collect().mkString(","))
  }
}
