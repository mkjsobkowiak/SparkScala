package spark

import java.util.{Properties}

import com.google.common.io.Resources
import domain.{TweetInfo, TweetResult}
import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.log4j.{Level, Logger}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._

object SparkService {

  def main(args: Array[String]) {

    val kafkaProps = Resources.getResource("producer_kafka.properties").openStream()
    val kafkaProperties = new Properties()
    kafkaProperties.load(kafkaProps)

    implicit val formats = DefaultFormats
    System.setProperty("hadoop.home.dir", "C:\\Hadoop")

    val sparkConf = new SparkConf()
      .setMaster("local[3]")
      .setAppName("spark-streaming-etl")
      .setSparkHome("/root/spark/")

    val streamingContext = new StreamingContext(sparkConf, Seconds(2))

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val kafkaConf = Map(
      "metadata.broker.list" -> "localhost:9092",
      "zookeeper.connect" -> "localhost:2181",
      "group.id" -> "kafka-spark-streaming-example",
      "zookeeper.connection.timeout.ms" -> "1000")

    val tweetsStream = KafkaUtils.createStream[Array[Byte], String, DefaultDecoder, StringDecoder](
      streamingContext,
      kafkaConf,
      Map("test" -> 1),
      StorageLevel.MEMORY_ONLY_SER)
      .map({ x =>
        implicit val formats = DefaultFormats
        parse(x._2).extract[TweetInfo]
      })

    streamingContext.checkpoint("/root/checkpoints")

    tweetsStream
      .map(x => (x.keyword, (x.sentiment, 1L)))
      .reduceByKeyAndWindow(
        (x: (Double, Long), y: (Double, Long)) => {
          val newX: Double = x._1 + y._1
          val newY: Long = x._2 + y._2
          (newX, newY)
        },
        Seconds(20), Seconds(20))
      .map(x => if (x._2._2 == 0) (x._1, -1.0) else (x._1, x._2._1 / x._2._2))
      .foreachRDD(x => x.foreachPartition(y => {
        val producer = new KafkaProducer[String, String](kafkaProperties)
        implicit val formats = DefaultFormats
        y.foreach(z => {
          val tweetInfo = write(new TweetResult(z._1, System.currentTimeMillis.toString, z._2))
          println(tweetInfo)
          producer.send(new ProducerRecord[String, String]("test2", tweetInfo))
        })
      }))

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
