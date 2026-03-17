-- =============================================================================
-- 초기 테이블 생성 스크립트
-- Docker 컨테이너 최초 실행 시 자동으로 실행됩니다.
-- 재실행 시 기존 테이블이 존재하면 건너뜁니다 (CREATE TABLE IF NOT EXISTS).
-- =============================================================================

USE boilerplate_db;

-- -----------------------------------------------------------------------------
-- member : 회원 정보
--   - email 은 로그인 ID 로 사용되므로 UNIQUE 제약
--   - password 는 BCrypt 인코딩된 값 저장 (평문 저장 금지)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS member (
  id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '회원 PK',
  email      VARCHAR(200) NOT NULL                COMMENT '이메일 (로그인 ID)',
  name       VARCHAR(100) NOT NULL                COMMENT '이름',
  password   VARCHAR(200) NOT NULL                COMMENT 'BCrypt 인코딩된 비밀번호',
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                   COMMENT '생성일시',
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (id),
  UNIQUE KEY uq_member_email (email)            -- 이메일 중복 가입 방지
) COMMENT = '회원';

-- -----------------------------------------------------------------------------
-- todo : 할 일 목록
--   - member_id 로 소유자를 식별하며, 회원 삭제 시 연쇄 삭제(CASCADE)
--   - completed : 0 = 미완료, 1 = 완료
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS todo (
  id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '할 일 PK',
  title      VARCHAR(200) NOT NULL                COMMENT '할 일 내용',
  completed  TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '완료 여부 (0: 미완료, 1: 완료)',
  member_id  BIGINT       NOT NULL                COMMENT '소유 회원 FK',
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                   COMMENT '생성일시',
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (id),
  KEY        ix_todo_member_id (member_id),       -- 회원별 목록 조회 인덱스
  CONSTRAINT fk_todo_member
    FOREIGN KEY (member_id) REFERENCES member (id)
    ON DELETE CASCADE                             -- 회원 삭제 시 할 일도 함께 삭제
) COMMENT = '할 일';
