name := "twitter-streaming-etl"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.6",
  "com.typesafe.akka" %% "akka-stream" % "2.4.4",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",
  "com.hunorkovacs" %% "koauth" % "1.1.0",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp")),
  "com.google.guava" % "guava" % "19.0",
  "org.apache.kafka" % "kafka-clients" % "0.9.0.1"
)
    