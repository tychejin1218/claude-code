package com.example.api.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Redis 캐시 키 및 TTL 정의
 */
@Getter
@RequiredArgsConstructor
public enum RedisKeys {

  SAMPLE("APP:SAMPLE:", 3600),
  REFRESH_TOKEN("APP:REFRESH_TOKEN:", 604_800),
  RATE_LIMIT("APP:RATE_LIMIT:", 60);

  private final String key;
  private final long ttl;
}
