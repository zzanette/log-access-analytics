package com.nabucco.petshop.persistence;

import com.nabucco.petshop.domain.AbastractEntity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class BaseRespository<T extends AbastractEntity> {

  private EntityManager entityManager;

  BaseRespository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public T save(T object) {
    getEntityManager().getTransaction().begin();
    getEntityManager().persist(object);
    getEntityManager().getTransaction().commit();

    return object;
  }

  public Query createQuery(String query) {
    return entityManager.createQuery(query);
  }
}
