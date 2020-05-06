package com.sunilvb.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class AvroSerializer {


  public static byte[] serialize(Order data, boolean useBinaryEncoding) throws SerializationException {
    try {
      byte[] result = null;

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Encoder encoder = useBinaryEncoding ?
          EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null) :
          EncoderFactory.get().jsonEncoder(data.getSchema(), byteArrayOutputStream);
      ;

      DatumWriter<Order> datumWriter = new SpecificDatumWriter<>(data.getSchema());
      datumWriter.write(data, encoder);

      encoder.flush();
      byteArrayOutputStream.close();

      result = byteArrayOutputStream.toByteArray();
        System.out.println("serialized data='{}' ({}) " + DatatypeConverter.printHexBinary(result) +"  "+ new String(result));

      return result;
    } catch (IOException e) {
      throw new SerializationException("Can't serialize data='" + data + "'", e);
    }
  }

}
