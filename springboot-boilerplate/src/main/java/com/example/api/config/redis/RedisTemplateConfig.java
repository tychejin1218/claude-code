package com.example.api.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;

/**
 * Redis Template 빈 설정
 *
 * <p>문자열, Object, Integer 타입별 {@link RedisTemplate} 제공
 *
 * @see RedisStandaloneConfig
 * @see RedisClusterConfig
 */
@Configuration
public class RedisTemplateConfig {

  /**
   * 문자열 전용 RedisTemplate 설정
   *
   * @param connectionFactory Lettuce 커넥션 팩토리
   * @return {@link StringRedisTemplate} 객체
   */
  @Bean
  public StringRedisTemplate stringRedisTemplate(
      LettuceConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }

  /**
   * Object 타입 RedisTemplate 설정
   *
   * <p>Jackson 3.x 기반 JSON 직렬화 사용
   *
   * @param connectionFactory Lettuce 커넥션 팩토리
   * @param objectMapper      Jackson ObjectMapper
   * @return {@link RedisTemplate} 객체
   */
  @Bean
  public RedisTemplate<String, Object> objectRedisTemplate(
      LettuceConnectionFactory connectionFactory,
      ObjectMapper objectMapper
  ) {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);

    // key
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setHashKeySerializer(RedisSerializer.string());

    // value serializer
    GenericJacksonJsonRedisSerializer serializer =
        new GenericJacksonJsonRedisSerializer(objectMapper);

    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashValueSerializer(serializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  /**
   * Integer 타입 RedisTemplate 설정
   *
   * @param connectionFactory Lettuce 커넥션 팩토리
   * @param objectMapper      Jackson ObjectMapper
   * @return {@link RedisTemplate} 객체
   */
  @Bean
  public RedisTemplate<String, Integer> integerRedisTemplate(
      LettuceConnectionFactory connectionFactory,
      ObjectMapper objectMapper
  ) {
    RedisTemplate<String, Integer> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(RedisSerializer.string());
    template.setValueSerializer(new GenericJacksonJsonRedisSerializer(objectMapper));
    template.afterPropertiesSet();
    return template;
  }
}
