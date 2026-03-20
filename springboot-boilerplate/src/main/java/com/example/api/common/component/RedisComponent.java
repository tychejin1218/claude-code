package com.example.api.common.component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Redis 캐시 공통 컴포넌트
 *
 * <p>문자열, 객체, 정수 타입별 저장/조회와 캐시 패턴(Cache-Aside) 지원
 * 모든 Redis 연산은 내부적으로 예외를 처리하여 장애 전파 방지
 *
 * @see com.example.api.config.redis.RedisTemplateConfig
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisComponent {

  private final StringRedisTemplate stringRedisTemplate;
  private final RedisTemplate<String, Object> objectRedisTemplate;
  private final RedisTemplate<String, Integer> integerRedisTemplate;
  private final ObjectMapper objectMapper;

  /**
   * 문자열 값 Redis 저장
   *
   * @param key      저장할 Redis 키
   * @param value    저장할 문자열 값
   * @param duration 유효 기간
   * @param unit     유효 기간 단위
   */
  public void setStringValue(String key, String value, long duration, TimeUnit unit) {
    try {
      stringRedisTemplate.opsForValue().set(key, value, duration, unit);
    } catch (Exception e) {
      log.error("[setStringValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
    }
  }

  /**
   * Redis 문자열 값 조회
   *
   * @param key 조회할 Redis 키
   * @return 저장된 문자열 값, 존재하지 않거나 오류 발생 시 null
   */
  public String getStringValue(String key) {
    try {
      String value = stringRedisTemplate.opsForValue().get(key);
      return StringUtils.isNotBlank(value) ? value : null;
    } catch (Exception e) {
      log.error("[getStringValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return null;
    }
  }

  /**
   * 객체 Redis 저장
   *
   * <p>내부적으로 JSON 직렬화
   *
   * @param key      저장할 Redis 키
   * @param value    저장할 객체
   * @param duration 유효 기간
   * @param unit     유효 기간 단위
   */
  public void setObjectValue(String key, Object value, long duration, TimeUnit unit) {
    try {
      objectRedisTemplate.opsForValue().set(key, value, duration, unit);
    } catch (Exception e) {
      log.error("[setObjectValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
    }
  }

  /**
   * Redis에서 JSON 객체 조회
   *
   * @param key           조회할 Redis 키
   * @param typeReference 변환할 객체 타입 정보
   * @param <T>           반환 객체 타입
   * @return 저장된 객체, 존재하지 않거나 오류 발생 시 null
   */
  public <T> T getObjectValue(String key, TypeReference<T> typeReference) {
    try {
      Object result = objectRedisTemplate.opsForValue().get(key);
      return result != null ? objectMapper.convertValue(result, typeReference) : null;
    } catch (Exception e) {
      log.error("[getObjectValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return null;
    }
  }

  /**
   * 정수 값 Redis 저장
   *
   * @param key      저장할 Redis 키
   * @param value    저장할 정수 값
   * @param duration 유효 기간
   * @param unit     유효 기간 단위
   */
  public void setIntegerValue(String key, Integer value, long duration, TimeUnit unit) {
    try {
      integerRedisTemplate.opsForValue().set(key, value, duration, unit);
    } catch (Exception e) {
      log.error("[setIntegerValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
    }
  }

  /**
   * Redis 정수 값 조회
   *
   * @param key 조회할 Redis 키
   * @return 저장된 정수 값, 존재하지 않거나 오류 발생 시 null
   */
  public Integer getIntegerValue(String key) {
    try {
      return integerRedisTemplate.opsForValue().get(key);
    } catch (Exception e) {
      log.error("[getIntegerValue] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return null;
    }
  }

  /**
   * Redis 키 삭제
   *
   * @param key 삭제할 Redis 키
   * @return 삭제 성공 여부
   */
  public boolean deleteKey(String key) {
    try {
      return stringRedisTemplate.delete(key);
    } catch (Exception e) {
      log.error("[deleteKey] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return false;
    }
  }

  /**
   * Redis 캐시 조회 또는 공급자를 통한 조회 후 캐시 저장
   *
   * @param key           Redis 키
   * @param typeReference 반환할 데이터 타입 정보
   * @param dataSupplier  데이터 공급자. 캐시에 값이 없을 경우 실행
   * @param ttl           캐시 만료 기간
   * @param timeUnit      만료 기간 단위
   * @param <T>           데이터 타입
   * @return 캐시된 데이터 또는 공급자를 통해 조회된 데이터. null이면 캐시에 저장하지 않음
   */
  public <T> T getCacheOrDefault(
      String key,
      TypeReference<T> typeReference,
      Supplier<T> dataSupplier,
      long ttl,
      TimeUnit timeUnit) {

    try {
      T cachedValue = getObjectValue(key, typeReference);
      if (cachedValue != null) {
        return cachedValue;
      }
    } catch (Exception e) {
      log.warn("[getCacheOrDefault] Redis 접근 실패, 공급자로 대체 - key: {}, message: {}", key,
          e.getMessage());
    }

    T value = dataSupplier.get();

    if (value != null) {
      setObjectValue(key, value, ttl, timeUnit);
    } else {
      log.warn("[getCacheOrDefault] 값 조회 실패, 캐시에 저장하지 않음 - key: {}", key);
    }

    return value;
  }

  /**
   * Redis 정수 카운터 증가 (키가 없으면 생성 후 TTL 설정)
   *
   * @param key      Redis 키
   * @param duration TTL
   * @param unit     TTL 단위
   * @return 증가 후 카운트 값 (오류 시 null)
   */
  public Long increment(String key, long duration, TimeUnit unit) {
    try {
      Long count = stringRedisTemplate.opsForValue().increment(key);
      if (Long.valueOf(1L).equals(count)) {
        stringRedisTemplate.expire(key, duration, unit);
      }
      return count;
    } catch (Exception e) {
      log.error("[increment] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return null;
    }
  }

  /**
   * Redis 키 존재 여부 확인
   *
   * @param key 확인할 Redis 키
   * @return 키 존재 여부
   */
  public boolean existsKey(String key) {
    try {
      return stringRedisTemplate.hasKey(key);
    } catch (Exception e) {
      log.error("[existsKey] 실패 - key: {}, message: {}", key, e.getMessage(), e);
      return false;
    }
  }

  /**
   * Redis 키 만료 시간 갱신
   *
   * @param key     Redis 키
   * @param timeout 갱신할 만료 시간
   * @param unit    시간 단위
   */
  public void renewExpire(String key, long timeout, TimeUnit unit) {
    try {
      if (!stringRedisTemplate.expire(key, timeout, unit)) {
        log.warn("[renewExpire] 만료 시간 갱신 실패 - key: {}", key);
      }
    } catch (Exception e) {
      log.error("[renewExpire] 실패 - key: {}, message: {}", key, e.getMessage(), e);
    }
  }
}