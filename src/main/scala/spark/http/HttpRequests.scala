package spark.http

import scalaj.http.{HttpOptions, Http}

case class WeatherDTO(weather: SkyDTO, main: TempDTO)

case class TempDTO(val temp: Double, val pressure: Int, val humidity: Int, val temp_min: Double, val temp_max: Double)

case class SkyDTO(val main: String, val description: String)

object HttpRequests {

  private[this] val berlinWeatherUrl = "http://api.openweathermap.org/data/2" +
    ".5/weather?id=2950157&APPID=782997e53eb19259de4bce90a4fe0758"

  def getBerlinWeather() = {
    Http(berlinWeatherUrl)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(20000))
  }

  def getTwitterData = {

  }
}
