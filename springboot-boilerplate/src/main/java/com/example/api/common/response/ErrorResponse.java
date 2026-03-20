package com.example.api.common.response;

import com.example.api.common.type.ApiStatus;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

/**
 * API 오류 응답
 */
@Getter
@Builder
public class ErrorResponse {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  private String statusCode;
  private String message;
  private String method;
  private String path;
  private String timestamp;

  /**
   * {@link ApiStatus} 기반 오류 응답 생성
   *
   * @param apiStatus API 응답 상태
   * @param method    HTTP 메서드
   * @param path      요청 경로
   * @return 오류 응답
   */
  public static ErrorResponse of(ApiStatus apiStatus, String method, String path) {
    return ErrorResponse.builder()
        .statusCode(apiStatus.getCode())
        .message(apiStatus.getMessage())
        .method(method)
        .path(path)
        .timestamp(LocalDateTime.now(ZoneOffset.UTC).format(TIMESTAMP_FORMATTER))
        .build();
  }

  /**
   * 사용자 정의 코드·메시지 기반 오류 응답 생성
   *
   * @param code    응답 코드
   * @param message 응답 메시지
   * @param method  HTTP 메서드
   * @param path    요청 경로
   * @return 오류 응답
   */
  public static ErrorResponse of(String code, String message, String method, String path) {
    return ErrorResponse.builder()
        .statusCode(code)
        .message(message)
        .method(method)
        .path(path)
        .timestamp(LocalDateTime.now(ZoneOffset.UTC).format(TIMESTAMP_FORMATTER))
        .build();
  }
}
