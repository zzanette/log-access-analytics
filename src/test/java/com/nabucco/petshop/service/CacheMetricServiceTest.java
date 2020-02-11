package com.nabucco.petshop.service;

import static org.mockito.ArgumentMatchers.anyString;

import com.nabucco.petshop.config.RedisConfig;
import com.nabucco.petshop.dto.MetricDTO;
import com.nabucco.petshop.serialization.SerializationStringObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

public class CacheMetricServiceTest {

  private RedisConfig redisConfig;
  private CacheMetricService service;

  @Before
  public void settUp() {
    this.redisConfig = Mockito.mock(RedisConfig.class);
    this.service = new CacheMetricService(redisConfig);
  }

  @Test
  public void shouldReturnEmptyList() {
    Assert.assertTrue(service.getCacheMetricByURL(null).isEmpty());
    Assert.assertTrue(service.getCacheMetricByURL("").isEmpty());
    Assert.assertTrue(service.setChacheMetricByURL("", Arrays.asList(new MetricDTO())).isEmpty());
    Assert.assertTrue(service.setChacheMetricByURL(null, Arrays.asList(new MetricDTO())).isEmpty());
    Assert.assertTrue(service.setChacheMetricByURL("/pets/cat/1", null).isEmpty());
    Assert
        .assertTrue(service.setChacheMetricByURL("/pets/cat/1", Collections.emptyList()).isEmpty());
  }

  @Test
  public void shouldGetCacheFromUrl() throws IOException {
    Jedis jedis = Mockito.mock(Jedis.class);
    MetricDTO metricDTO = new MetricDTO("/pets/cat/2", 33L);
    String metricString = SerializationStringObject.toString(metricDTO);
    Set<String> metricsCached = new HashSet<>();
    metricsCached.add(metricString);
    Mockito.when(redisConfig.getConnection()).thenReturn(jedis);
    Mockito.when(jedis.smembers(anyString())).thenReturn(new HashSet<>(metricsCached));

    MetricDTO metricReturned = service.getCacheMetricByURL("/pets/cat/2").stream().findFirst().get();
    Assert.assertEquals(metricDTO.getUrl(), metricReturned.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), metricReturned.getAmountAccess());
  }

  @Test
  public void shouldRetornEmptyListWhenGetCacheThrowAnExpcetion() {
    Mockito.when(redisConfig.getConnection()).thenThrow(new RuntimeException("Error"));
    Assert.assertTrue(service.getCacheMetricByURL("/pets/rat/33").isEmpty());
  }

  @Test
  public void shouldReturnTheSameListAsPassedInParameter() {
    Jedis jedis = Mockito.mock(Jedis.class);
    MetricDTO metricDTO = new MetricDTO("/pets/cat/2", 22L);
    List<MetricDTO> list = Arrays.asList(metricDTO);
    Mockito.when(redisConfig.getConnection()).thenReturn(jedis);

    MetricDTO metric = service.setChacheMetricByURL("/pets/cat/2", list).stream().findFirst().get();
    Assert.assertEquals(metricDTO.getUrl(), metric.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), metric.getAmountAccess());
  }

  @Test
  public void shouldReturnTheSameListAsPassedInParameterWhenThrowException() {
    Jedis jedis = Mockito.mock(Jedis.class);
    MetricDTO metricDTO = new MetricDTO("/pets/cat/2", 22L);
    List<MetricDTO> list = Arrays.asList(metricDTO);
    Mockito.when(redisConfig.getConnection()).thenReturn(jedis);
    Mockito.when(jedis.sadd(anyString())).thenThrow(new RuntimeException("Error"));

    MetricDTO metric = service.setChacheMetricByURL("/pets/cat/2", list).stream().findFirst().get();
    Assert.assertEquals(metricDTO.getUrl(), metric.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), metric.getAmountAccess());
  }
}