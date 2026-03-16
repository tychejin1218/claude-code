package com.example.api.common.response;

import com.example.api.common.type.ApiStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * API 공통 응답 래퍼
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@Builder
public class BaseResponse<T> {

  private String statusCode;
  private String message;
  private T data;

  /**
   * 데이터를 포함한 성공 응답 생성
   *
   * @param data 응답 데이터
   * @param <T>  응답 데이터 타입
   * @return 성공 응답
   */
  public static <T> BaseResponse<T> ok(T data) {
    return BaseResponse.<T>builder()
        .statusCode(ApiStatus.OK.getCode())
        .message(ApiStatus.OK.getMessage())
        .data(data)
        .build();
  }

  /**
   * 데이터 없는 성공 응답 생성
   *
   * @param <T> 응답 데이터 타입
   * @return 성공 응답
   */
  public static <T> BaseResponse<T> ok() {
    return BaseResponse.<T>builder()
        .statusCode(ApiStatus.OK.getCode())
        .message(ApiStatus.OK.getMessage())
        .build();
  }

  /**
   * 응답 메시지 변경
   *
   * @param message 변경할 메시지
   * @return 현재 응답 객체
   */
  public BaseResponse<T> message(String message) {
    this.message = message;
    return this;
  }
}
