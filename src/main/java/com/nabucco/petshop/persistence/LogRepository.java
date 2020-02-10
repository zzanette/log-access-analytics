package com.nabucco.petshop.persistence;

import com.nabucco.petshop.domain.Log;
import com.nabucco.petshop.dto.MetricDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class LogRepository extends BaseRespository<Log> {

  private static final String ORDER_ASC = "ASC";
  private static final String ORDER_DESC = "DESC";

  public LogRepository(EntityManager entityManager) {
    super(entityManager);
  }

  public List<MetricDTO> getMetricTopAccessAroundWorld(Integer numberOfResults, Integer year,
      Integer week, Integer day, Integer minute, Integer regionCode) {
    StringBuilder hqlStrngBuilder = queryBuilderTopURLs(year, week, day, minute,
        regionCode, ORDER_DESC);
    Query query = getQueryTopURL(year, week, day, minute, regionCode, hqlStrngBuilder);
    return getMetricDTOFrom(query, numberOfResults);
  }

  public List<MetricDTO> getMetricTopAccessAroundWorld(Integer numberOfResults, Integer year,
      Integer week, Integer day, Integer minute) {
    return getMetricTopAccessAroundWorld(numberOfResults, year, week, day, minute, null);
  }

  public List<MetricDTO> getMetricLessAccess(Integer numberOfResults) {
    StringBuilder hqlStrngBuilder = queryBuilderTopURLs(null, null, null, null, null,
        ORDER_ASC);
    Query query = getQueryTopURL(null, null, null, null, null, hqlStrngBuilder);
    return getMetricDTOFrom(query, numberOfResults);
  }

  private List<MetricDTO> getMetricDTOFrom(Query query, Integer numberOfResult) {
    List<MetricDTO> metricDTOS = new ArrayList<>();
    List<?> list = query.getResultList();
    for (int i = 0; i < list.size() && i < numberOfResult; i++) {
      Object[] row = (Object[]) list.get(i);
      System.out.println("Objectttt" + row);
      metricDTOS.add(new MetricDTO((String) row[1], (Long) row[0]));
    }

    return metricDTOS;
  }

  private StringBuilder queryBuilderTopURLs(Integer year, Integer week, Integer day, Integer minute,
      Integer regionCode, String orderBy) {
    StringBuilder hqlStrngBuilder = new StringBuilder()
        .append(" SELECT COUNT(*) as countAccess, url ")
        .append(" FROM log ")
        .append(" WHERE 1=1 ");

    if (regionCode != null) {
      hqlStrngBuilder.append(" AND region_code = :region ");
    }
    if (year != null) {
      hqlStrngBuilder.append(" AND YEAR(date_time) = :year ");
    }
    if (week != null) {
      hqlStrngBuilder.append(" AND WEEK(date_time) = :week ");
    }
    if (day != null) {
      hqlStrngBuilder.append(" AND  DAY(date_time) = :day ");
    }
    if (minute != null) {
      hqlStrngBuilder.append(" AND MINUTE(date_time) = :minute ");
    }

    hqlStrngBuilder
        .append(" GROUP BY url ")
        .append(" ORDER BY 1 ")
        .append(orderBy);

    System.out.println(hqlStrngBuilder.toString());
    return hqlStrngBuilder;
  }

  private Query getQueryTopURL(Integer year, Integer week, Integer day, Integer minute,
      Integer regionCode, StringBuilder hqlStrngBuilder) {
    Query query = createQuery(hqlStrngBuilder.toString());
    if (regionCode != null) {
      query.setParameter("region", regionCode);
    }
    if (year != null) {
      query.setParameter("year", year);
    }
    if (week != null) {
      query.setParameter("week", week);
    }
    if (day != null) {
      query.setParameter("day", day);
    }
    if (minute != null) {
      query.setParameter("minute", minute);
    }
    return query;
  }
}
