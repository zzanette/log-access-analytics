package com.nabucco.petshop.config;

import com.nabucco.petshop.controller.HomeController;
import com.nabucco.petshop.controller.LogAnalyticsController;
import com.nabucco.petshop.persistence.LogRepository;
import com.nabucco.petshop.service.CacheMetricService;
import com.nabucco.petshop.service.ElasticSearchService;
import com.nabucco.petshop.service.LogService;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.flywaydb.core.Flyway;
import org.restexpress.RestExpress;
import org.restexpress.util.Environment;
import redis.clients.jedis.Jedis;

public class Configuration extends Environment {

  private static final String DEFAULT_EXECUTOR_THREAD_POOL_SIZE = "20";

  private static final String PORT_PROPERTY = "port";
  private static final String BASE_URL_PROPERTY = "base.url";
  private static final String EXECUTOR_THREAD_POOL_SIZE = "executor.threadPool.size";
  private static final String REDIS_CONNECTION_URL = "redis.url";
  private static final String REDIS_CONNECTION_PORT = "redis.port";
  private static final String REDIS_CONNECTION_TIMEOUT = "redis.timeout";
  private static final String JDBC_URI = "jdbc.uri";
  private static final String JDBC_USER = "jdbc.user";
  private static final String JDBC_PASSWORD = "jdbc.password";
  private static final String JDBC_DBNAME = "jdbc.dbname";
  private static final String FLYWAY_LOCATION = "flyway.location";
  private static final String ELASTICSEARCH_HOST = "elasticsearch.host";
  private static final String ELASTICSEARCH_SCHEMA = "elasticsearch.schema";
  private static final String ELASTICSEARCH_PORT = "elasticsearch.port";


  private int port;
  private String baseUrl;
  private int executorThreadPoolSize;

  private EntityManager entityManager;
  private RedisConfig redisConfig;
  private ElasticSearchConfig elasticSearchConfig;
  private FlywayConfig flywayConfig;
  private LogRepository logRepository;

  private LogService logService;
  private CacheMetricService cacheMetricService;
  private ElasticSearchService elasticSearchService;

  private HomeController homeController;
  private LogAnalyticsController logAnalyticsController;

  @Override
  protected void fillValues(Properties p) {
    this.port = Integer
        .parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
    this.baseUrl = p.getProperty(BASE_URL_PROPERTY, "http://localhost:" + String.valueOf(port));
    this.executorThreadPoolSize = Integer
        .parseInt(p.getProperty(EXECUTOR_THREAD_POOL_SIZE, DEFAULT_EXECUTOR_THREAD_POOL_SIZE));
    this.entityManager = configureEntityManager(p);
    this.redisConfig = configureRedis(p);
    this.flywayConfig = configureFlyway(p);
    this.elasticSearchConfig = configureElasticSearch(p);
    initialize();
  }

  public int getPort() {
    return port;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public int getExecutorThreadPoolSize() {
    return executorThreadPoolSize;
  }

  public LogAnalyticsController getLogAnalyticsController() {
    return logAnalyticsController;
  }

  public HomeController getHomeController() {
    return homeController;
  }

  public RedisConfig getRedisConfig() {
    return redisConfig;
  }

  public ElasticSearchConfig getElasticSearchConfig() {
    return elasticSearchConfig;
  }

  private void initialize() {
    //Initialize repositories
    this.logRepository = new LogRepository(entityManager);

    //Initialize Services
    this.logService = new LogService(logRepository);
    this.cacheMetricService = new CacheMetricService(redisConfig);
    this.elasticSearchService = new ElasticSearchService(elasticSearchConfig);

    //Initialize Cntrollers
    this.homeController = new HomeController();
    this.logAnalyticsController = new LogAnalyticsController(logService, cacheMetricService,
        elasticSearchService);

    //Run migration
    this.flywayConfig.migrate();
  }

  private EntityManager configureEntityManager(Properties p) {
    Map<String, Object> configOverrides = new HashMap<>();
    configOverrides.put("javax.persistence.jdbc.url", p.getProperty(JDBC_URI));
    configOverrides.put("javax.persistence.jdbc.user", p.getProperty(JDBC_USER));
    configOverrides.put("javax.persistence.jdbc.password", p.getProperty(JDBC_PASSWORD));

    return Persistence.createEntityManagerFactory(p.getProperty(JDBC_DBNAME), configOverrides)
        .createEntityManager();
  }

  private RedisConfig configureRedis(Properties p) {
    Jedis jedis = new Jedis(p.getProperty(REDIS_CONNECTION_URL),
        Integer.valueOf(p.getProperty(REDIS_CONNECTION_PORT)),
        Integer.valueOf(p.getProperty(REDIS_CONNECTION_TIMEOUT)));

    return new RedisConfig(jedis);
  }

  private FlywayConfig configureFlyway(Properties p) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(p.getProperty(JDBC_URI), p.getProperty(JDBC_USER),
        p.getProperty(JDBC_PASSWORD));
    flyway.setBaselineOnMigrate(true);
    flyway.setOutOfOrder(true);
    flyway.setLocations(p.getProperty(FLYWAY_LOCATION));
    return new FlywayConfig(flyway);
  }

  private ElasticSearchConfig configureElasticSearch(Properties p) {
    RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
            new HttpHost(p.getProperty(ELASTICSEARCH_HOST),
                Integer.valueOf(p.getProperty(ELASTICSEARCH_PORT)),
                p.getProperty(ELASTICSEARCH_SCHEMA))));

    return new ElasticSearchConfig(client);
  }
}
