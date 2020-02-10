package com.nabucco.petshop.domain;

import org.junit.Assert;
import org.junit.Test;

public class LogTest {

  @Test
  public void shouldValidateTheNullValues() {
    try {
      new Log(null, null, null, null);
    } catch (Exception e) {
      e.getMessage().contains("Url");
      e.getMessage().contains("DateTime");
      e.getMessage().contains("UserUuid");
      e.getMessage().contains("RegionCode");
    }
  }

  @Test
  public void shouldValidateEqualsAndHashCode() {
    Log log1 = new Log();
    Log log2 = new Log();

    Assert.assertTrue(log1.equals(log2));
    Assert.assertEquals(log1.hashCode(), log2.hashCode());
  }
}