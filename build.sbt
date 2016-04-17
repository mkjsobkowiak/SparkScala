name := "SparkProject"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "1.6.1",
  "org.apache.spark" % "spark-streaming_2.11" % "1.6.1",
  "org.apache.spark" % "spark-sql_2.11" % "1.6.1",
  "com.datastax.spark" % "spark-cassandra-connector_2.11" % "1.5.0",
  "org.hibernate" % "hibernate-core" % "5.1.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "net.liftweb" % "lift-json_2.11" % "2.6.3"
)
    