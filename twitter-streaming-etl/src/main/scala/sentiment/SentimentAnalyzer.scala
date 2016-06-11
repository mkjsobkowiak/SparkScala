package sentiment

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import sentiment.Sentiment.Sentiment
import scala.collection.convert.wrapAll._

class SentimentAnalyzer {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  def mainSentiment(input: String): Sentiment = Option(input) match {
    case Some(text) if !text.isEmpty => extractSentiment(text)
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  def extractSentiment(text: String): Sentiment = {
    val sentimentValues = extractSentiments(text)
    if(sentimentValues.length > 0)
      return Sentiment.toSentiment(sentimentValues.sum / sentimentValues.length.toDouble)
    else
      Sentiment.NEUTRAL
  }

  def extractSentimentDouble(text: String): Double = {
    val sentiment = extractSentiment(text)
    sentiment match {
      case Sentiment.NEGATIVE => 0.0
      case Sentiment.NEUTRAL => 0.5
      case Sentiment.POSITIVE => 1.0
    }
  }

  private def extractSentiments(text: String): List[Int] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences
      .map(sentence => sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree]))
      .map {tree => RNNCoreAnnotations.getPredictedClass(tree) }
      .toList
  }

}