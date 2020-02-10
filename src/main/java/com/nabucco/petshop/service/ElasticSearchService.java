package com.nabucco.petshop.service;

import com.google.gson.GsonBuilder;
import com.nabucco.petshop.adapter.LocalDateTimeGsonAdapter;
import com.nabucco.petshop.config.ElasticSearchConfig;
import com.nabucco.petshop.domain.Log;
import com.nabucco.petshop.dto.ElasticLogDTO;
import java.io.IOException;
import java.time.LocalDateTime;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

public class ElasticSearchService {

  private static final String US_EAST = "us-east";
  private static final String INDEX = "log-access-analytics";

  private ElasticSearchConfig elasticSearchConfig;

  public ElasticSearchService(ElasticSearchConfig elasticSearchConfig) {
    this.elasticSearchConfig = elasticSearchConfig;
  }

  public void createElasticLog(Log log) {
    String id = US_EAST.concat("-").concat(log.getRegionCode().toString()).concat(":")
        .concat(log.getId().toString());
    IndexRequest request = new IndexRequest(INDEX.concat("-").concat(log.getId().toString()));
    ElasticLogDTO elasticLogDTO = new ElasticLogDTO(US_EAST.concat(log.getRegionCode().toString()),
        LocalDateTime.now(), getMessageLog(log));
    request.id(id);

    String jsonString = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter()).serializeNulls()
        .create().toJson(elasticLogDTO);
    try {
      elasticSearchConfig.getClient()
          .index(request.source(jsonString, XContentType.JSON), RequestOptions.DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getMessageLog(Log log) {
    return log.getUrl().concat(" ").concat(Long.valueOf(log.getDateTime().getTime()).toString())
        .concat(" ").concat(log.getUserUuid()).concat(" ").concat(log.getRegionCode().toString());
  }
}
