package com.example.api.config.redis;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Redis Standalone 연결 설정
 *
 * <p>로컬 환경 전용. 단일 노드 Redis 연결
 */
@Profile({"local", "standalone"})
@Configuration
public class RedisStandaloneConfig {

  @Value("${redis.stand-alone.host}")
  private String redisHost;

  @Value("${redis.stand-alone.port}")
  private int redisPort;

  @Value("${redis.stand-alone.timeout}")
  private int redisTimeout;

  /**
   * Standalone 모드의 Lettuce 커넥션 팩토리 생성
   *
   * @return {@link LettuceConnectionFactory} 객체
   */
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {

    RedisStandaloneConfiguration standaloneConfig =
        new RedisStandaloneConfiguration(redisHost, redisPort);

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .commandTimeout(Duration.ofMillis(redisTimeout))
        .build();

    return new LettuceConnectionFactory(standaloneConfig, clientConfig);
  }
}
