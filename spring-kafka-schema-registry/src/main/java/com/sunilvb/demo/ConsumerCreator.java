package com.sunilvb.demo;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerCreator {

  public static Consumer<String, Order> createConsumer(String topic) {
    System.setProperty("javax.net.ssl.keyStore",
        "/etc/producer/secrets/schema-registry.keystore.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", "datahub");
    System.setProperty("javax.net.ssl.trustStore",
        "/etc/producer/secrets/schema-registry.truststore.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "datahub");

    System.out.println("TOPIC ____________Consumer3__________________"+topic);

    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
    props.setProperty("schema.registry.url", IKafkaConstants.KAFKA_SC);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props
        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_LATEST);

    props.put("security.protocol", "SSL");
    props.put("ssl.truststore.location", "/etc/producer/secrets/consumer.truststore.jks");
    props.put("ssl.truststore.password", "datahub");

    props.put("ssl.key.password", "datahub");
    props.put("ssl.keystore.password", "datahub");
    props.put("ssl.keystore.location", "/etc/producer/secrets/consumer.keystore.jks");
    Consumer<String, Order> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(topic));
    return consumer;
  }
}