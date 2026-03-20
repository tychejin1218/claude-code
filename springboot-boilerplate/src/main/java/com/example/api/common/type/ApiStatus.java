package com.example.api.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API 응답 상태 코드 및 메시지 정의
 */
@Getter
@RequiredArgsConstructor
public enum ApiStatus {

  OK("200", "성공"),
  CUSTOM_EXCEPTION("800", "오류가 발생했습니다. 확인 후 다시 시도해주세요."),
  INVALID_REQUEST("801", "유효하지 않은 요청입니다."),
  FORBIDDEN_REQUEST("802", "허용되지 않은 요청입니다."),
  DUPLICATED_REQUEST("803", "중복된 요청입니다."),
  NOT_FOUND("804", "존재하지 않는 정보입니다."),
  UNAUTHORIZED("805", "유효하지 않은 권한입니다."),
  MEMBER_UPDATE_FAILED("806", "회원 정보 수정에 실패했습니다."),
  MEMBER_DELETE_FAILED("807", "회원 정보 삭제에 실패했습니다."),
  RATE_LIMIT_EXCEEDED("808", "요청 횟수가 초과됐습니다. 잠시 후 다시 시도해주세요."),
  INTERNAL_SERVER_ERROR("900", "내부 오류가 발생했습니다."),
  METHOD_ARGUMENT_NOT_VALID("901", "파라미터가 유효하지 않습니다."),
  MISSING_SERVLET_REQUEST_PARAMETER("902", "필수 파라미터가 누락되었습니다."),
  CONSTRAINT_VIOLATION("903", "파라미터 유효성 검사에 실패했습니다."),
  METHOD_ARGUMENT_TYPE_MISMATCH("904", "파라미터 타입이 올바르지 않습니다."),
  NO_HANDLER_FOUND("905", "요청한 URL을 찾을 수 없습니다."),
  HTTP_REQUEST_METHOD_NOT_SUPPORTED("906", "지원하지 않는 메서드입니다."),
  HTTP_MEDIA_TYPE_NOT_SUPPORTED("907", "지원되지 않는 미디어 타입입니다."),
  HTTP_MESSAGE_NOT_READABLE_EXCEPTION("908", "읽을 수 있는 요청 정보가 없습니다.");

  private final String code;
  private final String message;
}
