

name := "spark-streaming-etl"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.6",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp")),
  "org.apache.spark" % "spark-core_2.11" % "1.6.1",
  "org.apache.spark" % "spark-streaming_2.11" % "1.6.1",
  "org.apache.spark" % "spark-sql_2.11" % "1.6.1",
  "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.1",
  "com.datastax.spark" % "spark-cassandra-connector_2.11" % "1.5.0",
  "org.json4s" % "json4s-native_2.11" % "3.3.0",
  "org.apache.kafka" % "kafka-clients" % "0.9.0.1"
)