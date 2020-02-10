package com.nabucco.petshop.persistence;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nabucco.petshop.dto.MetricDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LogRepositoryTest {

  private EntityManager entityManager;
  private LogRepository repository;

  @Before
  public void setUp() {
    entityManager = Mockito.mock(EntityManager.class);
    repository = new LogRepository(entityManager);
  }

  @Test
  public void shouldReturnEmptyList() {
    Query query = Mockito.mock(Query.class);
    when(entityManager.createQuery(anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.emptyList());
    List<MetricDTO> listArouldWorld = repository.getMetricTopAccessAroundWorld(3, 2002, 20, 4, 2);
    List<MetricDTO> listRegion = repository.getMetricTopAccessAroundWorld(3, 2002, 20, 4, 2, 1);
    Assert.assertTrue(listArouldWorld.isEmpty());
    Assert.assertTrue(listRegion.isEmpty());
  }

  @Test
  public void shouldReturnTwoElements() {
    Query query = Mockito.mock(Query.class);
    when(query.getResultList()).thenReturn(getResults());
    when(entityManager.createQuery(anyString())).thenReturn(query);
    List<MetricDTO> listArouldWorld = repository.getMetricTopAccessAroundWorld(2, 2002, 20, 4, 2);
    List<MetricDTO> listRegion = repository.getMetricTopAccessAroundWorld(2, 2002, 20, 4, 2, 1);
    Assert.assertTrue(listArouldWorld.size() == 2);
    Assert.assertTrue(listRegion.size() == 2);
  }


  @Test
  public void shouldReturnTheLessAccess() {
    Query query = Mockito.mock(Query.class);
    when(query.getResultList()).thenReturn(getResults());
    when(entityManager.createQuery(anyString())).thenReturn(query);
    List<MetricDTO> listArouldWorld = repository.getMetricLessAccess(1);
    Assert.assertTrue(listArouldWorld.size() == 1);
  }

  private List<Object[]> getResults() {
    Object[] result1 = {1L, "/pets/123"};
    Object[] result2 = {2L, "/pets/123"};
    Object[] result3 = {3L, "/pets/123"};

    return Arrays.asList(result1, result2, result3);
  }
}