package com.example.api.auth.service;

import com.example.api.auth.dto.AuthDto;
import com.example.api.common.component.RedisComponent;
import com.example.api.common.exception.ApiException;
import com.example.api.common.type.ApiStatus;
import com.example.api.common.type.RedisKeys;
import com.example.api.config.jwt.JwtTokenProvider;
import com.example.api.domain.entity.Member;
import com.example.api.domain.repository.MemberRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final RedisComponent redisComponent;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 회원가입 - 이메일 중복 확인 후 저장 및 JWT 발급
   *
   * @param request 회원가입 요청 (email, name, password)
   * @return 토큰 응답
   */
  @Transactional
  public AuthDto.TokenResponse register(AuthDto.RegisterRequest request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new ApiException(HttpStatus.CONFLICT, ApiStatus.DUPLICATED_REQUEST);
    }
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    memberRepository.save(Member.of(request.getEmail(), request.getName(), encodedPassword));
    return issueTokenPair(request.getEmail());
  }

  /**
   * 로그인 - 이메일/비밀번호 검증 후 JWT 발급
   *
   * @param request 로그인 요청 (email, password)
   * @return 토큰 응답
   */
  @Transactional(readOnly = true)
  public AuthDto.TokenResponse login(AuthDto.LoginRequest request) {
    var member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED);
    }

    return issueTokenPair(member.getEmail());
  }

  /**
   * 로그아웃 - Redis에서 Refresh 토큰 삭제
   *
   * @param request 리프레시 토큰 요청
   */
  public void logout(AuthDto.RefreshRequest request) {
    String refreshToken = request.getRefreshToken();

    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED);
    }

    String subject = jwtTokenProvider.getSubject(refreshToken);
    redisComponent.deleteKey(RedisKeys.REFRESH_TOKEN.getKey() + subject);
  }

  /**
   * 토큰 재발급 - Refresh 토큰 검증 후 Access/Refresh 재발급
   *
   * @param request 토큰 재발급 요청
   * @return 새 토큰 응답
   */
  public AuthDto.TokenResponse refresh(AuthDto.RefreshRequest request) {
    String refreshToken = request.getRefreshToken();

    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED);
    }

    String subject = jwtTokenProvider.getSubject(refreshToken);
    String storedToken = redisComponent.getStringValue(RedisKeys.REFRESH_TOKEN.getKey() + subject);

    if (StringUtils.isBlank(storedToken) || !storedToken.equals(refreshToken)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED);
    }

    return issueTokenPair(subject);
  }

  private AuthDto.TokenResponse issueTokenPair(String subject) {
    String accessToken = jwtTokenProvider.createAccessToken(subject);
    String refreshToken = jwtTokenProvider.createRefreshToken(subject);

    redisComponent.setStringValue(
        RedisKeys.REFRESH_TOKEN.getKey() + subject,
        refreshToken,
        RedisKeys.REFRESH_TOKEN.getTtl(),
        TimeUnit.SECONDS);

    return AuthDto.TokenResponse.of(accessToken, refreshToken);
  }
}
