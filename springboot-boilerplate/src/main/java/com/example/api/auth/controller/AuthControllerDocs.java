package com.example.api.auth.controller;

import com.example.api.auth.dto.AuthDto;
import com.example.api.common.response.BaseResponse;
import com.example.api.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

/**
 * 인증 Controller API 문서
 */
@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃, 토큰 재발급 API")
public interface AuthControllerDocs {

  /**
   * 회원가입
   *
   * @param request 회원가입 요청 (email, name, password)
   * @return 토큰 응답
   */
  @Operation(summary = "회원가입", description = "이메일/이름/비밀번호로 회원가입하고 accessToken/refreshToken을 발급합니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "회원가입 성공",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AuthDto.TokenResponse.class),
              examples = @ExampleObject(value = """
                  {
                    "statusCode": "200",
                    "message": "성공",
                    "data": {
                      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
                    }
                  }
                  """)
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "이미 사용 중인 이메일",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  BaseResponse<AuthDto.TokenResponse> register(AuthDto.RegisterRequest request);

  /**
   * 로그인
   *
   * @param request 로그인 요청 (email, password)
   * @return 토큰 응답
   */
  @Operation(summary = "로그인", description = "이메일/비밀번호로 accessToken/refreshToken을 발급합니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "로그인 성공",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AuthDto.TokenResponse.class),
              examples = @ExampleObject(value = """
                  {
                    "statusCode": "200",
                    "message": "성공",
                    "data": {
                      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
                    }
                  }
                  """)
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "인증 실패 (이메일/비밀번호 불일치)",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  BaseResponse<AuthDto.TokenResponse> login(AuthDto.LoginRequest request);

  /**
   * 로그아웃
   *
   * @param request 리프레시 토큰 요청
   * @return 성공 응답
   */
  @Operation(summary = "로그아웃", description = "리프레시 토큰을 무효화하여 로그아웃 처리합니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "로그아웃 성공",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = BaseResponse.class),
              examples = @ExampleObject(value = """
                  {
                    "statusCode": "200",
                    "message": "성공",
                    "data": null
                  }
                  """)
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "유효하지 않은 리프레시 토큰",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  BaseResponse<Void> logout(AuthDto.RefreshRequest request);

  /**
   * 토큰 재발급
   *
   * @param request 리프레시 토큰 요청
   * @return 새 토큰 응답
   */
  @Operation(summary = "토큰 재발급", description = "유효한 리프레시 토큰으로 새 accessToken/refreshToken을 재발급합니다.")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "토큰 재발급 성공",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AuthDto.TokenResponse.class),
              examples = @ExampleObject(value = """
                  {
                    "statusCode": "200",
                    "message": "성공",
                    "data": {
                      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
                    }
                  }
                  """)
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "유효하지 않은 리프레시 토큰",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  BaseResponse<AuthDto.TokenResponse> refresh(AuthDto.RefreshRequest request);
}
