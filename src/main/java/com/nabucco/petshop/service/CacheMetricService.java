package com.nabucco.petshop.service;

import com.nabucco.petshop.config.RedisConnection;
import com.nabucco.petshop.domain.messageproperties.MessageProperties;
import com.nabucco.petshop.domain.messageproperties.MessagePropertiesEnum;
import com.nabucco.petshop.dto.MetricDTO;
import com.nabucco.petshop.serialization.SerializationStringObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheMetricService {

  private RedisConnection redisConnection;

  public CacheMetricService(RedisConnection redisConnection) {
    this.redisConnection = redisConnection;
  }

  public List<MetricDTO> setChacheMetricByURL(String url, List<MetricDTO> metricDTOList) {
    if (metricDTOList == null || url == null || metricDTOList.isEmpty() || url.trim().isEmpty()) {
      return Collections.emptyList();
    }

    try {
      for (MetricDTO metricDTO : metricDTOList) {
        redisConnection.getConnection().sadd(url, SerializationStringObject.toString(metricDTO));
      }
    } catch (Exception e) {
      log.error(MessageProperties.getMensagem(MessagePropertiesEnum.ERROR_ADD_CACHE_TO_URL, url));
      redisConnection.getConnection().del(url);
    }

    return metricDTOList;
  }

  public List<MetricDTO> getCacheMetricByURL(String url) {
    if (url == null || url.trim().isEmpty()) {
      return Collections.emptyList();
    }

    List<MetricDTO> metricDTOS = new ArrayList<>();
    try {
      Set<String> redisMetrics = redisConnection.getConnection().smembers(url);
      for (String metric : redisMetrics) {
        metricDTOS.add((MetricDTO) SerializationStringObject.fromString(metric));
      }

      metricDTOS.sort((o1, o2) -> o2.getAmountAccess().compareTo(o1.getAmountAccess()));
      return metricDTOS;
    } catch (Exception e) {
      log.error(
          MessageProperties.getMensagem(MessagePropertiesEnum.ERROR_EXTRACT_CACHE_FROM_URL, url),
          e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  public void clearCache() {
    try {
      redisConnection.getConnection().flushAll();
    } catch (Exception e) {
      log.error(MessageProperties.getMensagem(MessagePropertiesEnum.ERROR_FLUSH_CACHE),
          e.getMessage(), e);
    }
  }
}
