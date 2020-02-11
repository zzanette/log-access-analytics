package com.nabucco.petshop.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

  private Flyway flyway;

  FlywayConfig(Flyway flyway) {
    this.flyway = flyway;
  }

  public void migrate() {
    this.flyway.migrate();
  }
}
