

`docker-compose up -d app`

`kafka-topics --zookeeper localhost:22181 --create --topic games --if-not-exists --replication-factor 1 --partitions 1`

Add avro schemas for key and value
```
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/subjects/games-key/versions


curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{"schema": "{\"type\": \"record\",\"namespace\": \"com.mattkirwan\",\"name\": \"games\",\"fields\": [{\"name\": \"board\",\"type\": {\"type\": \"record\",\"name\": \"board_record\",\"fields\": [{\"name\": \"a1\",\"type\": \"boolean\"},{\"name\": \"a2\",\"type\": \"boolean\"}]}}]}"}' \
    http://localhost:8081/subjects/games-value/versions
```

Listen to the games topic
```
kafka-avro-console-consumer --bootstrap-server localhost:29092 \
--property schema.registry.url="http://localhost:8081" \
--topic games \
--property print.key=true
```

Write games messages
```
kafka-avro-console-producer --broker-list localhost:29092 \
--topic games \
--property schema.registry.url="http://localhost:8081" \
--property value.schema='{"type": "record","namespace": "com.mattkirwan","name": "games","fields": [{"name": "board","type": {"type": "record","name": "board_record","fields": [{"name": "a1","type": "boolean"},{"name": "a2","type": "boolean"}]}}]}' \
--property parse.key=true \
--property key.separator=":" \
--property key.schema='{"type":"string"}

Example:
{"board":"a1":false,"a2":true}}

```