package spark


import org.apache.spark.SparkConf
import org.apache.spark.streaming._

object QueueStream {

  def main(args: Array[String]) {
    // local[3] means => run on 3 thread
    val sparkConf = new SparkConf()
      .setMaster("local[3]")
      .setAppName("Spark stream app")
      .setSparkHome("/root/spark/")
      .set("spark.cassandra.connection.host", "192.168.56.101")

    val streamingContext = new StreamingContext(sparkConf, Seconds(1))
    val config = new twitter4j.conf.ConfigurationBuilder()
      .setOAuthConsumerKey("X")
      .setOAuthConsumerSecret("X")
      .setOAuthAccessToken("X")
      .setOAuthAccessTokenSecret("X")
      .build

    val authorization = new twitter4j.auth.OAuthAuthorization(config)
    val atwitter = new twitter4j.TwitterFactory(config).getInstance(authorization).getAuthorization()

    val inputStream = org.apache.spark.streaming.twitter.TwitterUtils.createStream(streamingContext, Some(atwitter))
    mapReduce(inputStream)
    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def mapReduce(inputStream: org.apache.spark.streaming.dstream.ReceiverInputDStream[twitter4j.Status]): Unit = {
    val mappedStream = inputStream.filter(s => s.getPlace != null && s.getPlace.getCountryCode == "GB").map(s => (s
      .getPlace.getName, s.getPlace.getCountry))
    mappedStream.print()
    //reducedStream.print()
    //reducedStream.foreachRDD(it => it.saveToCassandra("test", "kv1", SomeColumns("word", "count")))
  }
}


