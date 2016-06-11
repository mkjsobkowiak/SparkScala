package twitter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.hunorkovacs.koauth.domain.KoauthRequest
import com.hunorkovacs.koauth.service.consumer.DefaultConsumerService
import com.typesafe.config.ConfigFactory
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import java.util.Properties

import scala.concurrent.duration._
import com.google.common.io.Resources
import domain.TweetInfo
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import sentiment.SentimentAnalyzer

object TwitterStreamer extends App {

  val conf = ConfigFactory.load()

  val twitterProps = Resources.getResource("twitter.properties").openStream()
  val twitterProperties = new Properties()
  twitterProperties.load(twitterProps)

  val kafkaProps = Resources.getResource("producer_kafka.properties").openStream()
  val kafkaProperties = new Properties()
  kafkaProperties.load(kafkaProps)

  //Get your credentials from https://apps.twitter.com and replace the values below
  private val consumerKey = twitterProperties.getProperty("consumerKey")
  private val consumerSecret = twitterProperties.getProperty("consumerSecret")
  private val accessToken = twitterProperties.getProperty("accessToken")
  private val accessTokenSecret = twitterProperties.getProperty("accessTokenSecret")
  private val apiUrl = twitterProperties.getProperty("apiUrl")
  private val keyWord = twitterProperties.getProperty("keyWord")

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val formats = DefaultFormats

  private val consumer = new DefaultConsumerService(system.dispatcher)

  val body = "track=" + keyWord
  val source = Uri(apiUrl)
  var producer = new KafkaProducer[String, String](kafkaProperties)

  //Create Oauth 1a header
  val oauthHeader: Future[String] = consumer.createOauthenticatedRequest(
    KoauthRequest(
      method = "POST",
      url = apiUrl,
      authorizationHeader = None,
      body = Some(body)
    ),
    consumerKey,
    consumerSecret,
    accessToken,
    accessTokenSecret
  ) map (_.header)

  val sentimizer = new SentimentAnalyzer()

  oauthHeader.onComplete {
    case Success(header) =>
      val httpHeaders: List[HttpHeader] = List(
        HttpHeader.parse("Authorization", header) match {
          case ParsingResult.Ok(h, _) => Some(h)
          case _ => None
        },
        HttpHeader.parse("Accept", "*/*") match {
          case ParsingResult.Ok(h, _) => Some(h)
          case _ => None
        }
      ).flatten

      while (true) {
        val httpRequest: HttpRequest = HttpRequest(
          method = HttpMethods.POST,
          uri = source,
          headers = httpHeaders,
          entity = HttpEntity(contentType = ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`), string = body)
        )

        val request = Http().singleRequest(httpRequest)
        Await.ready(request.flatMap { response =>
          if (response.status.intValue() != 200) {
            println(response.entity.dataBytes.runForeach(_.utf8String))
            Future(Unit)
          } else {
            response.entity.dataBytes
              .scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
              .filter(_.contains("\r\n"))
              .filter(_ != "\r\n")
              .map(json => Try(parse(json).extract[Tweet]))
              .runForeach {
                case Success(tweet) =>
                  val tweetInfo = write(new TweetInfo(sentimizer.extractSentimentDouble(tweet.text), keyWord))
                  producer.send(new ProducerRecord[String, String]("test", tweetInfo))
                case Failure(e) =>
                  println(e.getStackTrace)
              }
          }
        }, Duration.Inf);
      }
    case Failure(failure) => println(failure.getMessage)
  }

}