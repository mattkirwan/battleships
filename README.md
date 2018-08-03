
# Battleships

A totally overkill real-time implementation of the Battleships boardgame using Apache Kafka and Scala.
This is very early days and I have no idea where it's going, I'm just playing with Scala, the KStreams DSL and accompanying Kafka tooling (KTables etc..)

## Up and Running

- `docker-compose up -d app`

## Produce Messages

From the `schema-registry` docker container:

```
kafka-avro-console-producer --broker-list localhost:29092 \
--topic board1115 \
--property schema.registry.url="http://localhost:8081" \
--property value.schema='{"namespace":"com.mattkirwan.avro","type":"record","name":"Event","fields":[{"name":"playerId","type":"string"},{"name":"action","type":"string"},{"name":"gridRef","type":"string"}]}' \
--property parse.key=true \
--property key.separator=":" \
--property key.schema='{"type":"string"}'


Example:
"1234-uuid-5678":{"playerId":"1234-5678","action":"fire","gridRef":"a:4"}

```

## Consume Messages

### Scala App

From `app` docker container:

`cd app`
`sbt run`

### CLI

From the `schema-registry docker container`:

```
kafka-avro-console-consumer --bootstrap-server localhost:29092 \
--property schema.registry.url="http://localhost:8081" \
--topic board1115 \
--property print.key=true
```

### Avro Schemas

You can add the avro schemas manually:
```
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/subjects/board1115-key/versions


curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{"schema": "{\"namespace\":\"com.mattkirwan.avro\",\"type\":\"record\",\"name\":\"Event\",\"fields\":[{\"name\":\"playerId\",\"type\":\"string\"},{\"name\":\"action\",\"type\":\"string\"},{\"name\":\"gridRef\",\"type\":\"string\"}]}"}' \
    http://localhost:8081/subjects/board1115-value/versions
```

### Kafka Topics

You can add the kafka topics manually

`kafka-topics --zookeeper localhost:22181 --create --topic games --if-not-exists --replication-factor 1 --partitions 1`
