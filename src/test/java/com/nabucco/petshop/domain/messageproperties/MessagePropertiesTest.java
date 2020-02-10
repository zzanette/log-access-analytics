package com.nabucco.petshop.domain.messageproperties;

import org.junit.Assert;
import org.junit.Test;

public class MessagePropertiesTest {

  @Test
  public void shouldReturnTheMessageProperty() {
    String expectedMessage = "Error in delete cache.";
    Assert.assertEquals(expectedMessage,
        MessageProperties.getMensagem(MessagePropertiesEnum.ERROR_FLUSH_CACHE));
  }
}