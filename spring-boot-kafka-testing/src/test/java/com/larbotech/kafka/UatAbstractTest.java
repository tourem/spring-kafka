package com.larbotech.kafka;

import com.larbotech.kafka.event.dto.Event1;
import com.larbotech.kafka.event.dto.Event2;
import com.larbotech.kafka.event.dto.Event3;
import com.larbotech.kafka.event.dto.Event4;
import com.larbotech.kafka.mock.CustomKafkaAvroDeserializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest(classes = {
    ShowcaseApp.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Tag("IntegrationTest")
@EmbeddedKafka()
public abstract class UatAbstractTest {

  @Autowired
  private KafkaProperties kafkaProperties;
  @Autowired
  private EmbeddedKafkaBroker kafkaEmbedded;
  protected Producer<String, Event3> event3Producer;
  protected Consumer<String, Event3> event3Consumer;
  protected Consumer<String, Event1> event1Consumer;
  protected Consumer<String, Event2> event2Consumer;

  protected Producer<String, Event4> event4Producer;
  protected Consumer<String, Event4> event4Consumer;

  @BeforeEach
  public void setUp() {
    Map<String, Object> senderProps = kafkaProperties.buildProducerProperties();

    event3Producer = new KafkaProducer<>(senderProps);
    event4Producer = new KafkaProducer<>(senderProps);

    //consumers used in test code needs to be created like this in code because otherwise it won't work
    Map<String, Object> configs = new HashMap<>(
        KafkaTestUtils.consumerProps("in-test-consumer", "false", kafkaEmbedded));
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        org.apache.kafka.common.serialization.StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomKafkaAvroDeserializer.class);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    configs.put("schema.registry.url", "not-used");

    event3Consumer = new DefaultKafkaConsumerFactory<String, Event3>(configs)
        .createConsumer("in-test-consumer", "10");
    event4Consumer = new DefaultKafkaConsumerFactory<String, Event4>(configs)
        .createConsumer("in-test-consumer", "10");
    event1Consumer = new DefaultKafkaConsumerFactory<String, Event1>(configs)
        .createConsumer("in-test-consumer", "10");
    event2Consumer = new DefaultKafkaConsumerFactory<String, Event2>(configs)
        .createConsumer("in-test-consumer", "10");

    kafkaProperties.buildConsumerProperties();
    event3Consumer.subscribe(Lists.newArrayList(Constants.EVENT_3_TOPIC));
    event4Consumer.subscribe(Lists.newArrayList(Constants.EVENT_4_TOPIC));
    event1Consumer.subscribe(Lists.newArrayList(Constants.EVENT_1_TOPIC));
    event2Consumer.subscribe(Lists.newArrayList(Constants.EVENT_2_TOPIC));
  }

  @AfterEach
  public void reset() {
    //consumers needs to be closed because new one are created before every test
    event3Consumer.close();
    event4Consumer.close();
    event1Consumer.close();
    event2Consumer.close();
  }

}
