package com.nabucco.petshop.serialization;

import com.nabucco.petshop.dto.MetricDTO;
import org.junit.Assert;
import org.junit.Test;

public class SerializationStringObjectTest {

  @Test
  public void shouldSerializeMetricDTOAsString() {
    try {
      MetricDTO metricDTO = new MetricDTO("/pets/cat/5", 13L);
      String serialization = SerializationStringObject.toString(metricDTO);
      MetricDTO desserialization = (MetricDTO) SerializationStringObject.fromString(serialization);

      Assert.assertEquals(metricDTO.getUrl(), desserialization.getUrl());
      Assert.assertEquals(metricDTO.getAmountAccess(), desserialization.getAmountAccess());
    } catch (Exception e) {
      Assert.fail("Should not throw an exception...");
    }
  }
}