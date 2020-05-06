package com.sunilvb.demo;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaRegistryApplicationTests {

	@Value("classpath:order")
	private Resource myResource;

	@Test
	public void contextLoads() throws IOException {

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

		byte[] bytes = Files.readAllBytes(Paths.get(myResource.getFile().getAbsolutePath()));

		byte[] resu = AvroSerializer.serialize(order, true);

		System.out.println(new String(resu));
	}

}
