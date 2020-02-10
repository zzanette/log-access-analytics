package com.nabucco.petshop.route;

import com.nabucco.petshop.config.Configuration;
import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;

public abstract class Routes {

  public static void define(Configuration config, RestExpress server) {
    server.uri("/", config.getHomeController())
        .action("hello", HttpMethod.GET);

    server.uri("/health", config.getHomeController())
        .action("health", HttpMethod.GET);

    server.uri("/log-analytics/ingest", config.getLogAnalyticsController())
        .action("ingest", HttpMethod.POST);

    server.uri("/log-analytics/metrics/top-3-urls", config.getLogAnalyticsController())
        .action("metricTopURLs", HttpMethod.GET);

    server.uri("/log-analytics/metrics/top-3-urls/region/{regionId}", config.getLogAnalyticsController())
        .action("metricTopURLsPerRegion", HttpMethod.GET);

    server.uri("/log-analytics/metrics/less-access-url", config.getLogAnalyticsController())
        .action("metricsLessAccessURL", HttpMethod.GET);
  }
}
