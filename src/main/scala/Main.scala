
import java.util.concurrent.TimeUnit
import java.util.{Collections, Properties}

import com.mattkirwan.avro.Event
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig, Topology}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.streams.kstream.{KStream, Printed}

object Main extends App {

  val config: Properties = {
    val props = new Properties
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "appid")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName())
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[SpecificAvroSerde[_ <: SpecificRecord]])
//    props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler")
    props
  }

  val builder = new StreamsBuilder

  val specificAvroSerde: Serde[Event] = new SpecificAvroSerde[Event]

  val isKeySerde: Boolean = false

  specificAvroSerde.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"), isKeySerde)

  val stream: KStream[String, Event] = builder.stream("board1115")

  stream.print(Printed.toSysOut[String, Event])

  val topology: Topology = builder.build()

  println(topology.describe())

  val kafkaStreams = new KafkaStreams(topology, config)

  kafkaStreams.start()

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    kafkaStreams.close(10, TimeUnit.SECONDS)
  }))

}