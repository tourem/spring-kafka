package com.sunilvb.demo;

public interface IKafkaConstants {

 String KAFKA_BROKERS = "broker:9092";
 String KAFKA_SC = "https://schema-registry:8181";
 Integer MESSAGE_COUNT=10;
 String CLIENT_ID="client1";
 String TOPIC_NAME="demo";
 String GROUP_ID_CONFIG="consumerGroup1";
 Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
 String OFFSET_RESET_LATEST="latest";
 String OFFSET_RESET_EARLIER="earliest";
 Integer MAX_POLL_RECORDS=1;

}
