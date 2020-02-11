package com.nabucco.petshop.config;

import java.io.IOException;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchConfig {

  private RestHighLevelClient client;

  ElasticSearchConfig(RestHighLevelClient client) {
    this.client = client;
  }

  public RestHighLevelClient getClient() {
    return client;
  }

  public void close() {
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
