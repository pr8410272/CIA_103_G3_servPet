package com.servPet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

//    @Bean
//    public JedisPool jedisPool() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(20); // 最大連線數
//        poolConfig.setMaxIdle(10);  // 最大空閒連線數
//        poolConfig.setMinIdle(5);   // 最小空閒連線數
//        poolConfig.setTestOnBorrow(true); // 借用連線時測試有效性
//        poolConfig.setTestOnReturn(true); // 還回連線時測試有效性
//
//        // 連線到本地 Redis，預設端口 6379
//        return new JedisPool(poolConfig, "localhost", 6379);
//    }

	@Bean
	public JedisPool jedisPool() {
		// 設定連線池參數
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(20); // 最大連線數
		poolConfig.setMaxIdle(10); // 最大空閒連線數
		poolConfig.setMinIdle(5); // 最小空閒連線數
		poolConfig.setTestOnBorrow(true); // 借用連線時測試有效性
		poolConfig.setTestOnReturn(true); // 還回連線時測試有效性
		poolConfig.setTestWhileIdle(true); // 空閒時測試連線
		poolConfig.setMinEvictableIdleTimeMillis(60000); // 最小空閒時間
		poolConfig.setTimeBetweenEvictionRunsMillis(30000); // 空閒檢查時間間隔
		poolConfig.setNumTestsPerEvictionRun(-1); // 每次檢查的連線數量 (-1 表示檢查所有)

		// 建立 Redis 連線池，連接本地 Redis 預設端口 6379
		return new JedisPool(poolConfig, "localhost", 6379);
	}
}
