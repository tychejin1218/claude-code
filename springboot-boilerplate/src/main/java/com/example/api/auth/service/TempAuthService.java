package com.example.api.auth.service;

import com.example.api.auth.dto.AuthDto;
import com.example.api.common.component.RedisComponent;
import com.example.api.common.type.RedisKeys;
import com.example.api.config.jwt.JwtTokenProvider;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 임시 토큰 발급 서비스 (local/dev/stg 전용)
 *
 * <p>SSO 없이 이메일만으로 JWT를 즉시 발급한다. Swagger 등 API 테스트 목적으로만 사용.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile({"local", "dev", "stg"})
public class TempAuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final RedisComponent redisComponent;

  /**
   * 이메일로 JWT 즉시 발급
   *
   * @param request 임시 토큰 발급 요청
   * @return 토큰 응답
   */
  public AuthDto.TokenResponse issueToken(AuthDto.TempTokenRequest request) {
    String subject = request.getEmail();

    String accessToken = jwtTokenProvider.createAccessToken(subject);
    String refreshToken = jwtTokenProvider.createRefreshToken(subject);

    String redisKey = RedisKeys.REFRESH_TOKEN.getKey() + subject;
    redisComponent.setStringValue(
        redisKey, refreshToken, RedisKeys.REFRESH_TOKEN.getTtl(), TimeUnit.SECONDS);

    log.info("[TempAuth] 임시 토큰 발급: subject={}", subject);

    return AuthDto.TokenResponse.of(accessToken, refreshToken);
  }
}
