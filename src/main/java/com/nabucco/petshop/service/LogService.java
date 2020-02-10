package com.nabucco.petshop.service;

import com.nabucco.petshop.domain.Log;
import com.nabucco.petshop.dto.DateTimeDTO;
import com.nabucco.petshop.dto.LogDTO;
import com.nabucco.petshop.dto.MetricDTO;
import com.nabucco.petshop.persistence.LogRepository;
import java.sql.Date;
import java.util.List;

public class LogService {

  private static final Integer NUMBER_RESULTS_3 = 3;
  private static final Integer NUMBER_RESULTS_1 = 1;

  private LogRepository repository;

  public LogService(LogRepository repository) {
    this.repository = repository;
  }

  public Log persist(LogDTO dto) {
    Log log = fromDTO(dto);
    return repository.save(log);
  }

  public List<MetricDTO> getMetricTop3AroundWorld(DateTimeDTO dateTimeDTO) {
    return repository
        .getMetricTopAccessAroundWorld(NUMBER_RESULTS_3, dateTimeDTO.getYearAsIntteger(),
            dateTimeDTO.getWeekAsIntteger(), dateTimeDTO.getDayAsIntteger(),
            dateTimeDTO.getMinuteAsIntteger());
  }

  public List<MetricDTO> getMetricTopPerRegion(DateTimeDTO dateTimeDTO, Integer regionCode) {
    return repository
        .getMetricTopAccessAroundWorld(NUMBER_RESULTS_3, dateTimeDTO.getYearAsIntteger(),
            dateTimeDTO.getWeekAsIntteger(), dateTimeDTO.getDayAsIntteger(),
            dateTimeDTO.getMinuteAsIntteger(), regionCode);
  }

  public MetricDTO getLessAccessUrl() {
    return repository.getMetricLessAccess(NUMBER_RESULTS_1).stream().findFirst().orElse(null);
  }

  private Log fromDTO(LogDTO logDTO) {
    Date date = new Date(Long.valueOf(logDTO.getTimestampUser()));
    return new Log(logDTO.getUrl(), date, logDTO.getUserUuid(), logDTO.getRegionCode());
  }
}
