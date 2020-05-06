package com.larbotech.kafka.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("UnitTest")
 class KafkaUtilTest {


  @Test
   void toInt() {

    assertThat(KafkaUtil.toInt("2")).isEqualTo(2);
  }
}
