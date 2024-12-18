package com.servPet.sitterFav.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class FavoRedisConfig {

	    @Bean
	    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(connectionFactory);

	        // 使用 StringRedisSerializer 來處理 Key 的序列化
	        redisTemplate.setKeySerializer(new StringRedisSerializer());
	        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

	        // 使用 GenericJackson2JsonRedisSerializer 來處理 Value 的序列化
	        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

	        return redisTemplate;
	    }
}
