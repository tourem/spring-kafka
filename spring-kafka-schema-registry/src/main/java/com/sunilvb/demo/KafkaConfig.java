package com.sunilvb.demo;

import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRY_BACKOFF_MS_CONFIG;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/*
 * @version 1.0 created by Carol on 2018/9/13 17:27
 */
@Configuration
@Getter
@Setter
//@EnableKafka
public class KafkaConfig {

  private static final String LINGER_MS_CONFIG = "linger.ms";
  private static final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";
  private static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG = "max.in.flight.requests.per.connection";

  //@Value("${spring.kafka.bootstrap-servers}")
  private String hosts;

  //@Value("${spring.kafka.producer.acks:all}")
  private String acks;

  //@Value("${spring.kafka.producer.retries}")
  private String retries;

  //@Value("${spring.kafka.producer.ssl.protocol}")
  private String sslProtocol;

  //@Value("${spring.kafka.producer.ssl.key-store-location}")
  private String sslKeyStoreLocation;

  //@Value("${spring.kafka.producer.ssl.key-store-password}")
  private String sslKeyStorePassword;

  //@Value("${spring.kafka.producer.ssl.key-store-type}")
  private String sslKeyStoreType;

  //@Value("${spring.kafka.producer.ssl.key-password}")
  private String sslKeyPassword;

  //@Value("${spring.kafka.producer.ssl.trust-store-location}")
  private String sslTrustStoreLocation;

  //@Value("${spring.kafka.producer.ssl.trust-store-password}")
  private String sslTrustStorePassword;

  //@Value("${spring.kafka.producer.ssl.trust-store-type}")
  private String sslTrustStoreType;

  //@Value("${spring.kafka.producer.properties.linger.ms}")
  private Integer lingerMs;

  //@Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
  private Integer maxInFlightRequestPerConnection;

  //@Value("${spring.kafka.producer.properties.request.timeout.ms:20000}")
  private Integer requestTimeoutMs;

  //@Value("${spring.kafka.producer.properties.retry.backoff.ms:500}")
  private Integer retryBackoffMs;

  //@Value("${spring.kafka.producer.properties.schema.registry.url}")
  private Integer schemaRegistryUrl;

  // ----------------producer---------------
  //@Bean
  public Properties producerConfigs() {

    Properties properties = new Properties();

    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);

    properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
    properties.put(CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
    properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTrustStoreLocation);
    properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTrustStorePassword);

    properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeyStoreLocation);
    properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeyStorePassword);
    properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKeyPassword);
    properties.put(LINGER_MS_CONFIG, lingerMs);

    properties.put(RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
    properties.put(ProducerConfig.ACKS_CONFIG, acks);
    properties.put(ProducerConfig.RETRIES_CONFIG, retries);
    properties
        .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    properties.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG, maxInFlightRequestPerConnection);
    return properties;
  }

}