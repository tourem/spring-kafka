package com.sunilvb.demo;

import static java.lang.String.*;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRY_BACKOFF_MS_CONFIG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.util.Random;
import java.util.UUID;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

@SpringBootApplication
@RestController
public class SpringKafkaRegistryApplication {


  private static final String LINGER_MS_CONFIG = "linger.ms";
  private static final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";
  private static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG = "max.in.flight.requests.per.connection";


  final static Logger logger = Logger.getLogger(SpringKafkaRegistryApplication.class);
  //@Value("${spring.kafka.bootstrap-servers}")
  List<String> bootstrap;
  //@Value("${spring.kafka.properties.schema.registry.url}")
  String registry;


  @Autowired
  private KafkaProperties kafkaProperties;

  static {
    //for localhost testing only
    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
        new javax.net.ssl.HostnameVerifier(){

          public boolean verify(String hostname,
              javax.net.ssl.SSLSession sslSession) {
            if (hostname.equals("localhost")) {
              return true;
            }
            return false;
          }
        });
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaRegistryApplication.class, args);

  }

  @RequestMapping("/orders")
  public String doIt(@RequestParam(value="name", defaultValue="Order-avro") String name)
  {

    String ret=name;
    try
    {

      System.setProperty("javax.net.ssl.keyStore", "/etc/kafka/secrets/kafka.schema-registry.keystore.jks");
      System.setProperty("javax.net.ssl.keyStorePassword", "awesomekafka");
      System.setProperty("javax.net.ssl.trustStore", "/etc/kafka/secrets/kafka.schema-registry.truststore.jks");
      System.setProperty("javax.net.ssl.trustStorePassword", "awesomekafka");
//System.setProperty("javax.net.debug", "ssl");

      ret += "<br>Using Bootstrap : " + bootstrap;
      ret += "<br>Using Bootstrap : " + registry;

      Properties properties = new Properties();


      properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, join(",", kafkaProperties.getBootstrapServers()));


      properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getProducer().getSsl().getProtocol());
      properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaProperties.getProducer().getSsl().getTruststoreLocation().getFile().getAbsolutePath());
      properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  kafkaProperties.getProducer().getSsl().getTruststorePassword());

      properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaProperties.getProducer().getSsl().getKeystoreLocation().getFile().getAbsolutePath());
      properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,  kafkaProperties.getProducer().getSsl().getKeystorePassword());
      properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG,  kafkaProperties.getProducer().getSsl().getKeyPassword());


      properties.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
      properties.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

      properties.putAll(getAdditionalProperties());

      ret += sendMsg(properties, name);
    }
    catch(Exception ex){
      ret+="<br>"+ex.getMessage() + " "+ex.getCause() + " "+ex.fillInStackTrace();
    }

    return ret;
  }

  private Order sendMsg(Properties properties, String topic)
  {
    Producer<String, Order> producer = new KafkaProducer<String, Order>(properties);

    Order order = Order.newBuilder()
        .setOrderId("OId234")
        .setCustomerId("CId432")
        .setSupplierId("SId543")
        .setItems(4)
        .setFirstName("Sunil")
        .setLastName("V")
        .setPrice(178f)
        .setWeight(75f)
        .build();

    List<Header> headers = new ArrayList<>();
    headers.add(new RecordHeader("X-Custom-HeaderK1", "1 Sending Custom Header with Spring Kafka".getBytes()));
    headers.add(new RecordHeader("X-Custom-HeaderK2", "2 Sending Custom Header with Spring Kafka".getBytes()));

    ProducerRecord<String, Order> producerRecord = new ProducerRecord<String, Order>(topic,0, valueOf(new Random().nextInt()), order, headers);



    producer.send(producerRecord, new Callback() {
      @Override
      public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
          logger.info(metadata);
        } else {
          logger.error(exception.getMessage());
        }
      }
    });

    producer.flush();
    producer.close();

    return order;
  }


  public Map<String, String> getAdditionalProperties(){

    Map<String, String> additionalProperties = new HashMap<>();

    Map<String, String> properties = kafkaProperties.getProducer().getProperties();

    if (properties.containsKey(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG)){
      additionalProperties.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG, properties.get(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG));
    }

    if (properties.containsKey(REQUEST_TIMEOUT_MS_CONFIG)){
      additionalProperties.put(REQUEST_TIMEOUT_MS_CONFIG, properties.get(REQUEST_TIMEOUT_MS_CONFIG));
    }

    if (properties.containsKey(RETRY_BACKOFF_MS_CONFIG)){
      additionalProperties.put(RETRY_BACKOFF_MS_CONFIG, properties.get(RETRY_BACKOFF_MS_CONFIG));
    }

    if (properties.containsKey(SCHEMA_REGISTRY_URL_CONFIG)){
      additionalProperties.put(SCHEMA_REGISTRY_URL_CONFIG, properties.get(SCHEMA_REGISTRY_URL_CONFIG));
    }

    if (properties.containsKey(LINGER_MS_CONFIG)){
      additionalProperties.put(LINGER_MS_CONFIG, properties.get(LINGER_MS_CONFIG));
    }

    return additionalProperties;
  }

}