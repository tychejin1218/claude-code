package com.example.api.common.exception;

import com.example.api.common.type.ApiStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 비즈니스 예외
 *
 * <p>HTTP 상태 코드와 {@link ApiStatus} 응답 코드를 함께 전달
 */
@Getter
public class ApiException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final ApiStatus apiStatus;

  /**
   * 기본 HTTP 400 상태의 API 예외 생성
   *
   * @param apiStatus API 응답 상태
   */
  public ApiException(ApiStatus apiStatus) {
    super(apiStatus.getMessage());
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.apiStatus = apiStatus;
  }

  /**
   * HTTP 상태 코드를 지정하는 API 예외 생성
   *
   * @param httpStatus HTTP 상태 코드
   * @param apiStatus  API 응답 상태
   */
  public ApiException(HttpStatus httpStatus, ApiStatus apiStatus) {
    super(apiStatus.getMessage());
    this.httpStatus = httpStatus;
    this.apiStatus = apiStatus;
  }

  /**
   * HTTP 상태 코드와 사용자 정의 메시지를 지정하는 API 예외 생성
   *
   * @param httpStatus HTTP 상태 코드
   * @param apiStatus  API 응답 상태
   * @param message    사용자 정의 메시지
   */
  public ApiException(HttpStatus httpStatus, ApiStatus apiStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.apiStatus = apiStatus;
  }

  /**
   * 원인 예외를 포함하는 API 예외 생성
   *
   * @param httpStatus HTTP 상태 코드
   * @param apiStatus  API 응답 상태
   * @param cause      원인 예외
   */
  public ApiException(HttpStatus httpStatus, ApiStatus apiStatus, Throwable cause) {
    super(apiStatus.getMessage(), cause);
    this.httpStatus = httpStatus;
    this.apiStatus = apiStatus;
  }
}
