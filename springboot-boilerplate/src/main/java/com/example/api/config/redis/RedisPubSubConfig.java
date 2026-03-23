package com.example.api.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Redis Pub/Sub 설정
 *
 * <p>SSE 알림을 위한 Redis Pub/Sub 리스너 컨테이너를 구성합니다.
 * 서버 인스턴스가 여러 개인 경우에도 Redis 채널을 통해 모든 인스턴스에 이벤트가 전달됩니다.
 */
@Configuration
public class RedisPubSubConfig {

  /**
   * Redis 메시지 리스너 컨테이너
   *
   * @param connectionFactory Lettuce 커넥션 팩토리
   * @return RedisMessageListenerContainer
   */
  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer(
      LettuceConnectionFactory connectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }
}
