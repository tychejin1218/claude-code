package com.example.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 DTO
 */
public class AuthDto {

  /**
   * 토큰 응답
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "토큰 응답")
  public static class TokenResponse {

    @Schema(description = "액세스 토큰 (Bearer)")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    /**
     * 토큰 응답 생성
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @return TokenResponse
     */
    public static TokenResponse of(String accessToken, String refreshToken) {
      return TokenResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
    }
  }

  /**
   * 로그인 요청
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "로그인 요청")
  public static class LoginRequest {

    @Schema(description = "이메일", example = "user@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
  }

  /**
   * 토큰 재발급 요청
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "토큰 재발급 요청")
  public static class RefreshRequest {

    @Schema(description = "리프레시 토큰")
    @NotBlank(message = "리프레시 토큰을 입력해주세요.")
    private String refreshToken;
  }

  /**
   * 임시 토큰 발급 요청 (local/dev/stg 전용)
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "임시 토큰 발급 요청 (local/dev/stg 전용)")
  public static class TempTokenRequest {

    @Schema(description = "이메일 (JWT subject)", example = "test@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    /**
     * 정적 팩토리 메서드
     *
     * @param email 이메일
     * @return TempTokenRequest
     */
    public static TempTokenRequest of(String email) {
      return TempTokenRequest.builder()
          .email(email)
          .build();
    }
  }
}
