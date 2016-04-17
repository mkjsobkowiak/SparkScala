package spark


import org.apache.spark.{SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.InputDStream
import com.datastax.spark.connector._

import scala.collection.mutable

object QueueStream {

  def main(args: Array[String]) {
    // local[3] means => run on 3 thread
    val sparkConf = new SparkConf()
      .setMaster("local[3]")
      .setAppName("Spark stream app")
      .setSparkHome("/root/spark/")
      .set("spark.cassandra.connection.host", "192.168.56.101")

    val streamingContext = new StreamingContext(sparkConf, Seconds(1))
    val lines = mutable.Queue[RDD[Int]]()
    val inputStream = streamingContext.queueStream(lines)
    mapReduce(inputStream)
    new Scheduler(lines, streamingContext)
    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def mapReduce(inputStream: InputDStream[Int]): Unit = {
    val mappedStream = inputStream.map((_, 1))
    val reducedStream = mappedStream.reduceByKey(_ + _)
    reducedStream.print()
    reducedStream.foreachRDD(it => it.saveToCassandra("test", "kv1", SomeColumns("word", "count")))
  }
}


