package sentiment

object Sentiment extends Enumeration {
  type Sentiment = Value
  val POSITIVE, NEGATIVE, NEUTRAL = Value

  def toSentiment(sentiment: Int): Sentiment = sentiment match {
    case x if x == 0 || x == 1 => Sentiment.NEGATIVE
    case 2 => Sentiment.NEUTRAL
    case x if x == 3 || x == 4 => Sentiment.POSITIVE
  }

  def toSentiment(sentiment: Double): Sentiment = sentiment match {
    case x if x < 1.75 => Sentiment.NEGATIVE
    case x if x > 2.25 => Sentiment.POSITIVE
    case default  => Sentiment.NEUTRAL
  }
}
