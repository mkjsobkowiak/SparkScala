package spark


import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.InputDStream

import scala.collection.mutable

object QueueStream {

  def main(args: Array[String]) {
    // local[3] means => run on 3 thread
    val sparkConf = new SparkConf().setMaster("local[3]").setAppName("Spark stream app").setSparkHome("/root/spark/")
    val streamingContext = new StreamingContext(sparkConf, Seconds(1))
    val lines = mutable.Queue[RDD[Int]]()
    val inputStream = streamingContext.queueStream(lines)
    mapReduce(inputStream, lines)
    new Scheduler(lines, streamingContext)

    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def mapReduce(inputStream: InputDStream[Int], lines: mutable.Queue[RDD[Int]]): Unit = {
    val mappedStream = inputStream.map(x => (x % 10, 1))
    val reducedStream = mappedStream.reduceByKey(_ + _)
    reducedStream.print()
    reducedStream.foreachRDD(it => {
      GenericDAO.begin
      it.foreach(values => {
        GenericDAO.save(new SimpleFun(values._1.toString, values._2.toString))
      })
      GenericDAO.end
    })
  }
}


