package com.example.api.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 생성 / 파싱 / 검증
 */
@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long accessTokenValidityMs;
  private final long refreshTokenValidityMs;

  /**
   * JWT 토큰 제공자 생성자
   *
   * @param secret                  JWT 서명 키
   * @param accessTokenValidityMs   액세스 토큰 유효 기간 (밀리초)
   * @param refreshTokenValidityMs  리프레시 토큰 유효 기간 (밀리초)
   */
  public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity-ms}") long accessTokenValidityMs,
      @Value("${jwt.refresh-token-validity-ms}") long refreshTokenValidityMs) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidityMs = accessTokenValidityMs;
    this.refreshTokenValidityMs = refreshTokenValidityMs;
  }

  /**
   * 액세스 토큰 생성
   *
   * @param subject 토큰 주체 (사용자 ID)
   * @return 액세스 토큰
   */
  public String createAccessToken(String subject) {
    return createToken(subject, accessTokenValidityMs);
  }

  /**
   * 리프레시 토큰 생성
   *
   * @param subject 토큰 주체 (사용자 ID)
   * @return 리프레시 토큰
   */
  public String createRefreshToken(String subject) {
    return createToken(subject, refreshTokenValidityMs);
  }

  /**
   * 토큰에서 Subject(사용자 ID) 추출
   *
   * @param token JWT 토큰
   * @return Subject
   */
  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  /**
   * 토큰 유효성 검증
   *
   * @param token JWT 토큰
   * @return 유효 여부
   */
  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("[JWT] 만료된 토큰: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.warn("[JWT] 지원하지 않는 토큰: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.warn("[JWT] 잘못된 형식의 토큰: {}", e.getMessage());
    } catch (SignatureException e) {
      log.warn("[JWT] 서명 검증 실패: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.warn("[JWT] 빈 토큰: {}", e.getMessage());
    }
    return false;
  }

  private String createToken(String subject, long validityMs) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(validityMs)))
        .signWith(secretKey)
        .compact();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
