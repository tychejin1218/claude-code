package com.example.api.config.web;

import com.example.api.common.component.RedisComponent;
import com.example.api.common.response.ErrorResponse;
import com.example.api.common.type.ApiStatus;
import com.example.api.common.type.RedisKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

/**
 * Rate Limit 인터셉터
 *
 * <p>IP + 경로 기준으로 분당 요청 횟수를 제한한다.
 * 초과 시 429 Too Many Requests 응답을 반환한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

  private static final int MAX_REQUESTS_PER_MINUTE = 10;

  private final RedisComponent redisComponent;
  private final ObjectMapper objectMapper;

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws IOException {

    String ip = resolveClientIp(request);
    String key = RedisKeys.RATE_LIMIT.getKey() + ip + ":" + request.getRequestURI();

    Long count = redisComponent.increment(key, RedisKeys.RATE_LIMIT.getTtl(), TimeUnit.SECONDS);

    if (count != null && count > MAX_REQUESTS_PER_MINUTE) {
      log.warn("[RateLimit] 요청 초과 - ip: {}, uri: {}, count: {}", ip,
          request.getRequestURI(), count);
      writeRateLimitResponse(request, response);
      return false;
    }

    return true;
  }

  private void writeRateLimitResponse(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    ErrorResponse errorResponse = ErrorResponse.of(
        ApiStatus.RATE_LIMIT_EXCEEDED,
        request.getMethod(),
        request.getRequestURI());
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private String resolveClientIp(HttpServletRequest request) {
    String forwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNotBlank(forwardedFor)) {
      return forwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
