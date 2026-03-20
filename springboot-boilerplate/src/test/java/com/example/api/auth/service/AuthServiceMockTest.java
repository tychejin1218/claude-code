package com.example.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.api.auth.dto.AuthDto;
import com.example.api.common.component.RedisComponent;
import com.example.api.common.exception.ApiException;
import com.example.api.config.jwt.JwtTokenProvider;
import com.example.api.domain.entity.Member;
import com.example.api.domain.repository.MemberRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 인증 서비스 Mock 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("인증 서비스 Mock 테스트")
class AuthServiceMockTest {

  @InjectMocks
  private AuthService authService;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private RedisComponent redisComponent;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @Order(1)
  @DisplayName("로그인 - 성공")
  void login_success() {
    // given
    AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
        .email("test@example.com")
        .password("password123")
        .build();
    Member member = Member.builder()
        .id(1L)
        .email("test@example.com")
        .name("테스트")
        .password("encodedPassword")
        .build();
    given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.of(member));
    given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);
    given(jwtTokenProvider.createAccessToken("test@example.com")).willReturn("accessToken");
    given(jwtTokenProvider.createRefreshToken("test@example.com")).willReturn("refreshToken");

    // when
    AuthDto.TokenResponse response = authService.login(request);

    // then
    assertThat(response.getAccessToken()).isEqualTo("accessToken");
    assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
  }

  @Test
  @Order(2)
  @DisplayName("로그인 - 존재하지 않는 이메일")
  void login_memberNotFound() {
    // given
    AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
        .email("notfound@example.com")
        .password("password123")
        .build();
    given(memberRepository.findByEmail("notfound@example.com")).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(3)
  @DisplayName("로그인 - 비밀번호 불일치")
  void login_wrongPassword() {
    // given
    AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
        .email("test@example.com")
        .password("wrongPassword")
        .build();
    Member member = Member.builder()
        .id(1L)
        .email("test@example.com")
        .name("테스트")
        .password("encodedPassword")
        .build();
    given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.of(member));
    given(passwordEncoder.matches("wrongPassword", "encodedPassword")).willReturn(false);

    // when & then
    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(4)
  @DisplayName("로그아웃 - 성공")
  void logout_success() {
    // given
    AuthDto.RefreshRequest request = AuthDto.RefreshRequest.builder()
        .refreshToken("validToken")
        .build();
    given(jwtTokenProvider.validateToken("validToken")).willReturn(true);
    given(jwtTokenProvider.getSubject("validToken")).willReturn("test@example.com");

    // when
    authService.logout(request);

    // then
    verify(redisComponent).deleteKey("APP:REFRESH_TOKEN:test@example.com");
  }

  @Test
  @Order(5)
  @DisplayName("로그아웃 - 유효하지 않은 토큰")
  void logout_invalidToken() {
    // given
    AuthDto.RefreshRequest request = AuthDto.RefreshRequest.builder()
        .refreshToken("invalidToken")
        .build();
    given(jwtTokenProvider.validateToken("invalidToken")).willReturn(false);

    // when & then
    assertThatThrownBy(() -> authService.logout(request))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(6)
  @DisplayName("토큰 재발급 - 성공")
  void refresh_success() {
    // given
    AuthDto.RefreshRequest request = AuthDto.RefreshRequest.builder()
        .refreshToken("validToken")
        .build();
    given(jwtTokenProvider.validateToken("validToken")).willReturn(true);
    given(jwtTokenProvider.getSubject("validToken")).willReturn("test@example.com");
    given(redisComponent.getStringValue("APP:REFRESH_TOKEN:test@example.com"))
        .willReturn("validToken");
    given(jwtTokenProvider.createAccessToken("test@example.com")).willReturn("newAccessToken");
    given(jwtTokenProvider.createRefreshToken("test@example.com")).willReturn("newRefreshToken");

    // when
    AuthDto.TokenResponse response = authService.refresh(request);

    // then
    assertThat(response.getAccessToken()).isEqualTo("newAccessToken");
    assertThat(response.getRefreshToken()).isEqualTo("newRefreshToken");
    verify(redisComponent).setStringValue(
        eq("APP:REFRESH_TOKEN:test@example.com"),
        eq("newRefreshToken"),
        anyLong(),
        any(TimeUnit.class));
  }

  @Test
  @Order(7)
  @DisplayName("토큰 재발급 - 유효하지 않은 토큰")
  void refresh_invalidToken() {
    // given
    AuthDto.RefreshRequest request = AuthDto.RefreshRequest.builder()
        .refreshToken("invalidToken")
        .build();
    given(jwtTokenProvider.validateToken("invalidToken")).willReturn(false);

    // when & then
    assertThatThrownBy(() -> authService.refresh(request))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(8)
  @DisplayName("토큰 재발급 - Redis 저장 토큰 불일치")
  void refresh_tokenMismatch() {
    // given
    AuthDto.RefreshRequest request = AuthDto.RefreshRequest.builder()
        .refreshToken("validToken")
        .build();
    given(jwtTokenProvider.validateToken("validToken")).willReturn(true);
    given(jwtTokenProvider.getSubject("validToken")).willReturn("test@example.com");
    given(redisComponent.getStringValue("APP:REFRESH_TOKEN:test@example.com"))
        .willReturn("otherToken");

    // when & then
    assertThatThrownBy(() -> authService.refresh(request))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(9)
  @DisplayName("회원가입 - 성공")
  void register_success() {
    // given
    AuthDto.RegisterRequest request = AuthDto.RegisterRequest.builder()
        .email("new@example.com")
        .name("신규회원")
        .password("password123")
        .build();
    given(memberRepository.existsByEmail("new@example.com")).willReturn(false);
    given(passwordEncoder.encode("password123")).willReturn("encodedPassword");
    given(jwtTokenProvider.createAccessToken("new@example.com")).willReturn("accessToken");
    given(jwtTokenProvider.createRefreshToken("new@example.com")).willReturn("refreshToken");

    // when
    AuthDto.TokenResponse response = authService.register(request);

    // then
    assertThat(response.getAccessToken()).isEqualTo("accessToken");
    assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
    verify(memberRepository).save(any(Member.class));
  }

  @Test
  @Order(10)
  @DisplayName("회원가입 - 이메일 중복")
  void register_duplicatedEmail() {
    // given
    AuthDto.RegisterRequest request = AuthDto.RegisterRequest.builder()
        .email("test@example.com")
        .name("테스트")
        .password("password123")
        .build();
    given(memberRepository.existsByEmail("test@example.com")).willReturn(true);

    // when & then
    assertThatThrownBy(() -> authService.register(request))
        .isInstanceOf(ApiException.class);
  }
}
