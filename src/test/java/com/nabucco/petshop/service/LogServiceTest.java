package com.nabucco.petshop.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.nabucco.petshop.domain.Log;
import com.nabucco.petshop.dto.DateTimeDTO;
import com.nabucco.petshop.dto.LogDTO;
import com.nabucco.petshop.dto.MetricDTO;
import com.nabucco.petshop.persistence.LogRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LogServiceTest {

  private LogRepository logRepository;
  private LogService service;

  @Before
  public void setUp() {
    logRepository = Mockito.mock(LogRepository.class);
    service = new LogService(logRepository);
  }

  @Test
  public void shouldPersist() {
    LogDTO logDTO = new LogDTO("/pets/rat/1", "1037825323957",
        "uuqiqwe123-qweiqeowiuos-asdkajlqwe1-asdljç12", 1);
    Date date = new Date(Long.valueOf(logDTO.getTimestampUser()));
    Log log = new Log("/pets/rat/1", date, "uuqiqwe123-qweiqeowiuos-asdkajlqwe1-asdljç12", 1);
    Mockito.when(logRepository.save(any())).thenReturn(log);

    Log retured = service.persist(logDTO);
    Assert.assertEquals(log.getDateTime(), retured.getDateTime());
    Assert.assertEquals(log.getUrl(), retured.getUrl());
    Assert.assertEquals(log.getRegionCode(), retured.getRegionCode());
  }

  @Test
  public void shouldReturnMetricTopWorld() {
    MetricDTO metricDTO = new MetricDTO("/pets/cat/1", 22L);
    List<MetricDTO> metricDTOS = Arrays.asList(metricDTO);
    Mockito.when(logRepository.getMetricTopAccessAroundWorld(any(), any(), any(), any(), any()))
        .thenReturn(metricDTOS);
    MetricDTO returnedMetric = service
        .getMetricTop3AroundWorld(new DateTimeDTO("2002", "5", "22", null)).stream().findFirst()
        .get();

    Assert.assertEquals(metricDTO.getUrl(), returnedMetric.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), returnedMetric.getAmountAccess());
  }

  @Test
  public void shouldReturnMetricPerRegion() {
    MetricDTO metricDTO = new MetricDTO("/pets/cat/1", 22L);
    List<MetricDTO> metricDTOS = Arrays.asList(metricDTO);
    Mockito
        .when(logRepository
            .getMetricTopAccessAroundWorld(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), anyInt())).thenReturn(metricDTOS);
    MetricDTO returnedMetric = service
        .getMetricTopPerRegion(new DateTimeDTO("2002", "5", "22", null), 1).stream().findFirst()
        .get();

    Assert.assertEquals(metricDTO.getUrl(), returnedMetric.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), returnedMetric.getAmountAccess());
  }

  @Test
  public void shouldReturnLessAccess() {
    MetricDTO metricDTO = new MetricDTO("/pets/cat/1", 22L);
    List<MetricDTO> metricDTOS = Arrays.asList(metricDTO);
    Mockito
        .when(logRepository.getMetricLessAccess(Mockito.any())).thenReturn(metricDTOS);
    MetricDTO returnedMetric = service.getLessAccessUrl();

    Assert.assertEquals(metricDTO.getUrl(), returnedMetric.getUrl());
    Assert.assertEquals(metricDTO.getAmountAccess(), returnedMetric.getAmountAccess());
  }
}