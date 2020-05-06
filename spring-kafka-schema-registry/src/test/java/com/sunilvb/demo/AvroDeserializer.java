package com.sunilvb.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;

public class AvroDeserializer {

  public static Order deserialize(byte[] data, boolean useBinaryEncoding) throws SerializationException {
    try {
      Order result = null;
      if (data != null) {

        Schema schema = new Order().getSchema();
        DatumReader<Order> datumReader =
            new SpecificDatumReader<>(schema);
        Decoder decoder = useBinaryEncoding ?
            DecoderFactory.get().binaryDecoder(data, null) :
            DecoderFactory.get().jsonDecoder(schema, new ByteArrayInputStream(data));
        ;

        result = datumReader.read(null, decoder);

      }
      return result;
    } catch (IOException e) {
      throw new SerializationException("Can't deserialize data '" + Arrays.toString(data) + "'", e);
    }
  }
}
