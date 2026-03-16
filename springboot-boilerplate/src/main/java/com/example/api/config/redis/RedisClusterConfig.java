package com.example.api.config.redis;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Redis Cluster 연결 설정
 *
 * <p>dev, stg, prd 환경 전용. 토폴로지 자동 갱신 및 Replica 우선 읽기 적용
 */
@Profile({"dev", "stg", "prd"})
@Configuration
public class RedisClusterConfig {

  @Value("${redis.cluster.nodes}")
  private List<String> nodes;

  /**
   * 클러스터 모드의 Lettuce 커넥션 팩토리 생성
   *
   * <p>토폴로지 주기적 갱신(60초) 및 Replica 우선 읽기 전략 적용
   *
   * @return {@link LettuceConnectionFactory} 객체
   */
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {

    RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(nodes);

    ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
        .dynamicRefreshSources(true)
        // 클러스터 토폴로지를 60초마다 주기적으로 갱신하도록 설정
        .enablePeriodicRefresh(Duration.ofSeconds(60))
        // 연결 오류, 시간 초과 등 즉각적으로 클러스터 토플로지를 갱신하도록 설정
        .enableAllAdaptiveRefreshTriggers()
        // 갱신 타임아웃을 30초로 설정
        .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
        .build();

    ClusterClientOptions clientOptions = ClusterClientOptions.builder()
        .autoReconnect(true)
        .topologyRefreshOptions(topologyRefreshOptions)
        .build();

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .clientOptions(clientOptions)
        // 읽기 작업을 우선적으로 수행하도록 설정
        .readFrom(ReadFrom.REPLICA_PREFERRED)
        .build();

    return new LettuceConnectionFactory(clusterConfig, clientConfig);
  }
}
