package com.nabucco.petshop.config;

import redis.clients.jedis.Jedis;

public class RedisConnection {

  private Jedis jedis;

  protected RedisConnection(Jedis jedis) {
    this.jedis = jedis;
  }

  public Jedis getConnection() {
    return jedis;
  }

  public void close() {
    if (jedis != null) {
      this.jedis.close();
    }
  }
}
