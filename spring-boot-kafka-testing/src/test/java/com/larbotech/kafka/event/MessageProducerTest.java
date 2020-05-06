package com.larbotech.kafka.event;

import com.larbotech.kafka.Constants;
import com.larbotech.kafka.UatAbstractTest;
import com.larbotech.kafka.event.dto.Event1;
import com.larbotech.kafka.event.dto.Event2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


 class MessageProducerTest extends UatAbstractTest {

    @Autowired
   KafkaProperties kafkaProperties;
    @Autowired
    private MessageProducer messageProducer;

    @Test
     void should_send_event1() {
        messageProducer.sendEvent1();

        ConsumerRecord<String, Event1> singleRecord = KafkaTestUtils.getSingleRecord(event1Consumer, Constants.EVENT_1_TOPIC);
        assertThat(singleRecord).isNotNull();
    }

    @Test
     void should_send_event2() {
        messageProducer.sendEvent2();

        ConsumerRecord<String, Event2> singleRecord = KafkaTestUtils.getSingleRecord(event2Consumer, Constants.EVENT_2_TOPIC);
        assertThat(singleRecord).isNotNull();
    }
}