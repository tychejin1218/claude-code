package com.example.api.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 애플리케이션 공통 상수 정의
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  // 기본 패키지 경로
  public static final String BASE_PACKAGE = "com.example.api";

  // 임시 사용자 ID (인증 연동 전 사용)
  public static final Long TEMP_USER_ID = 1L;

  // Hibernate에서 SQL 쿼리에 주석을 추가하기 위해 사용되는 주석 키
  public static final String HIBERNATE_SQL_COMMENT = "org.hibernate.comment";
}
