Simple Spark Application
==============

A simple Spark application that writes and reads avros.

tested on a Cloudera Quickstart VM version 5.0:
1) Writing GenericRecords fails until Avro 1.7.7 (in Cloudera CDH 5.x) (see https://github.com/sryza/simplesparkavroapp/issues/2)
2) Writing SpecificRecords can be written as json-esque strings in textfiles, presumably can't write actual avros until Spark 1.0.0 when `saveAsNewAPIHadoopDataset` is available (also in the next Cloudera CDH)

To make a jar:

    mvn package

To run from a gateway node in a CDH5 cluster:

    source /etc/spark/conf/spark-env.sh

    export JAVA_HOME=/usr/java/jdk1.7.0_45-cloudera

    # system jars:
    CLASSPATH=/etc/hadoop/conf
    CLASSPATH=$CLASSPATH:$HADOOP_HOME/*:$HADOOP_HOME/lib/*
    CLASSPATH=$CLASSPATH:$HADOOP_HOME/../hadoop-mapreduce/*:$HADOOP_HOME/../hadoop-mapreduce/lib/*
    CLASSPATH=$CLASSPATH:$HADOOP_HOME/../hadoop-yarn/*:$HADOOP_HOME/../hadoop-yarn/lib/*
    CLASSPATH=$CLASSPATH:$HADOOP_HOME/../hadoop-hdfs/*:$HADOOP_HOME/../hadoop-hdfs/lib/*
    CLASSPATH=$CLASSPATH:$SPARK_HOME/assembly/lib/*

    # app jar:
    CLASSPATH=$CLASSPATH:target/sparkavroapp-0.0.1-SNAPSHOT-jar-with-dependencies.jar

    CONFIG_OPTS="-Dspark.master=local -Dspark.jars=target/sparkavroapp-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
    
    # writing:
    $JAVA_HOME/bin/java -cp $CLASSPATH $CONFIG_OPTS com.cloudera.sparkavro.SparkAvroWriter <output path>
    
    # reading:
    # $JAVA_HOME/bin/java -cp $CLASSPATH $CONFIG_OPTS com.cloudera.sparkavro.SparkAvroReader <input path>



This will run the application in a single local process.  If the cluster is running a Spark standalone
cluster manager, you can replace "-Dspark.master=local" with
"-Dspark.master=spark://`<master host>`:`<master port>`".

If the cluster is running YARN, you can replace "-Dspark.master=local" with "-Dspark.master=yarn-client".

Future releases will include a "spark-submit" script that abstracts away the pain of building the
classpath and specifying the cluster manager.
