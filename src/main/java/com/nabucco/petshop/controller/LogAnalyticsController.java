package com.nabucco.petshop.controller;

import com.nabucco.petshop.domain.Log;
import com.nabucco.petshop.dto.DateTimeDTO;
import com.nabucco.petshop.dto.LogDTO;
import com.nabucco.petshop.dto.MetricDTO;
import com.nabucco.petshop.route.Constants.Url;
import com.nabucco.petshop.service.CacheMetricService;
import com.nabucco.petshop.service.ElasticSearchService;
import com.nabucco.petshop.service.LogService;
import com.strategicgains.syntaxe.ValidationEngine;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.restexpress.Request;
import org.restexpress.Response;

public class LogAnalyticsController {

  private LogService service;
  private CacheMetricService cacheMetricService;
  private ElasticSearchService elasticSearchService;

  public LogAnalyticsController(LogService service, CacheMetricService cacheMetricService,
      ElasticSearchService elasticSearchService) {
    this.service = service;
    this.cacheMetricService = cacheMetricService;
    this.elasticSearchService = elasticSearchService;
  }

  public Log ingest(Request request, Response response) {
    LogDTO logDTO = request.getBodyAs(LogDTO.class, "Log not provided");
    ValidationEngine.validateAndThrow(logDTO);
    response.setResponseCreated();
    Log log = service.persist(logDTO);
    CompletableFuture.runAsync(() -> {
      cacheMetricService.clearCache();
      elasticSearchService.createElasticLog(log);
    });

    return log;
  }

  public List<MetricDTO> metricTopURLs(Request request, Response response) {
    response.setResponseStatus(HttpResponseStatus.OK);

    List<MetricDTO> cachedMetrcic = cacheMetricService.getCacheMetricByURL(request.getUrl());
    if (!cachedMetrcic.isEmpty()) {
      return cachedMetrcic;
    }
    List<MetricDTO> metrics = service.getMetricTop3AroundWorld(getDateTimeDTOFrom(request));
    CompletableFuture.runAsync(() -> cacheMetricService.setChacheMetricByURL(request.getUrl(), metrics));
    return metrics;
  }

  public List<MetricDTO> metricTopURLsPerRegion(Request request, Response response) {
    Integer regionId = Integer
        .valueOf(request.getHeader(Url.REGION_ID_PARAMETER, "Region id required."));
    response.setResponseStatus(HttpResponseStatus.OK);

    List<MetricDTO> cachedMetrcic = cacheMetricService.getCacheMetricByURL(request.getUrl());
    if (!cachedMetrcic.isEmpty()) {
      return cachedMetrcic;
    }
    List<MetricDTO> metrics = service.getMetricTopPerRegion(getDateTimeDTOFrom(request), regionId);
    CompletableFuture
        .runAsync(() -> cacheMetricService.setChacheMetricByURL(request.getUrl(), metrics));
    return metrics;
  }

  public MetricDTO metricsLessAccessURL(Request request, Response response) {
    response.setResponseStatus(HttpResponseStatus.OK);

    List<MetricDTO> cachedMetrcic = cacheMetricService.getCacheMetricByURL(request.getUrl());
    if (!cachedMetrcic.isEmpty()) {
      return cachedMetrcic.stream().findFirst().get();
    }
    List<MetricDTO> metrics = Collections.singletonList(service.getLessAccessUrl());
    CompletableFuture
        .runAsync(() -> cacheMetricService.setChacheMetricByURL(request.getUrl(), metrics));
    return metrics.stream().findFirst().orElseGet(() -> {
      response.setResponseStatus(HttpResponseStatus.NO_CONTENT);
      return null;
    });
  }

  private DateTimeDTO getDateTimeDTOFrom(Request request) {
    String year = request.getHeader(Url.YEAR_PARAMETER);
    String week = request.getHeader(Url.WEEK_PARAMETER);
    String day = request.getHeader(Url.DAY_PARAMETER);
    String minute = request.getHeader(Url.MINUTE_PARAMETER);

    return new DateTimeDTO(year, week, day, minute);
  }
}
