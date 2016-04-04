name := "SparkProject"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "1.6.1",
  "org.apache.spark" % "spark-streaming_2.11" % "1.6.1",
  "org.hibernate" % "hibernate-core" % "5.1.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final"
)
    