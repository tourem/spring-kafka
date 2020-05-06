package com.larbotech.kafka.mock;

import com.larbotech.kafka.Constants;
import com.larbotech.kafka.event.dto.Event1;
import com.larbotech.kafka.event.dto.Event2;
import com.larbotech.kafka.event.dto.Event3;
import com.larbotech.kafka.event.dto.Event4;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.Schema;

/**
 * This code is not thread safe and should not be used in production environment
 */
public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {
    @Override
    public Object deserialize(String topic, byte[] bytes) {
        if (topic.equals(Constants.EVENT_1_TOPIC)) {
            this.schemaRegistry = getMockClient(Event1.SCHEMA$);
        }
        if (topic.equals(Constants.EVENT_2_TOPIC)) {
            this.schemaRegistry = getMockClient(Event2.SCHEMA$);
        }
        if (topic.equals(Constants.EVENT_3_TOPIC)) {
            this.schemaRegistry = getMockClient(Event3.SCHEMA$);
        }
        if (topic.equals(Constants.EVENT_4_TOPIC)) {
            this.schemaRegistry = getMockClient(Event4.SCHEMA$);
        }
        return super.deserialize(topic, bytes);
    }

    private static SchemaRegistryClient getMockClient(final Schema schema$) {
        return new MockSchemaRegistryClient() {
            @Override
            public synchronized Schema getById(int id) {
                return schema$;
            }
        };
    }
}
