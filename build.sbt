name := "battleships"

version := "0.1"

scalaVersion := "2.12.6"

resolvers ++= Seq(
  "Confluent" at "http://packages.confluent.io/maven/"
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-streams" % "1.1.0",
  "io.confluent" % "kafka-streams-avro-serde" % "4.1.0",
  "org.apache.avro" % "avro" % "1.8.2"
)

