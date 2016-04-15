package spark

import net.liftweb.json._
import spark.http.{HttpRequests, WeatherDTO}

object CheckDatabase extends App {
  implicit val formats = DefaultFormats
  // JSON PARSER
  val body = HttpRequests.getBerlinWeather().asString.body
  val parsedJson = parse(body).extract[WeatherDTO]
  print(body)
  print(parsedJson.weather.description)

  // DATABASE CHECK
  //  val session = HibernateUtil.getSessionFactory.openSession()
  //  session.beginTransaction()
  //  val tweet1 = new TweetEntity("Natalino")
  //  val tweet2 = new TweetEntity("Angelina")
  //  val tweet3 = new TweetEntity("Kate")
  //
  //  session.save(tweet1)
  //  session.save(tweet2)
  //  session.save(tweet3)
  //  session.getTransaction.commit()
}
