# Spring Boot 보일러플레이트 태스크

---

## Phase 1 — 기반 다지기

### 1. Soft Delete
- [x] `BaseAudit`에 `deletedAt` 추가
- [x] `Member`, `Todo` 엔티티에 `@SQLRestriction` 적용
- [x] Delete API를 물리 삭제 → 논리 삭제로 변경

### 2. 페이지네이션 + 필터링
- [x] Todo 목록 API에 `page`, `size`, `sort`, `status` 파라미터 추가 (QueryDSL)
- [x] `PageResponse<T>` 공통 응답 래퍼 추가

### 3. 테스트 작성
- [x] `TodoService` 단위 테스트
- [x] `AuthService` 단위 테스트
- [x] `TodoController` MockMvc 통합 테스트

---

## Phase 2 — 인증 강화

### 4. RBAC (역할 기반 접근 제어)
- [x] `Member`에 `role` 필드 추가 (`ROLE_USER`, `ROLE_ADMIN`)
- [x] 관리자 전용 API 엔드포인트 추가 (`/api/admin/**`)
- [x] `@PreAuthorize("hasRole('ADMIN')")` 적용

### 5. Rate Limiting
- [x] Redis 기반 요청 카운터 구현
- [x] 인증 API (`/api/auth/**`)에 분당 N회 제한 적용

---

## Phase 3 — 기능 확장

### 6. 파일 업로드
- [x] Presigned URL 발급 API (`GET /api/files/presigned-url`)
- [x] 업로드 완료 후 URL 저장 API (`PATCH /api/todos/{id}/image`)

### 7. 실시간 알림 (SSE + Redis Pub/Sub)
- [x] SSE 구독 엔드포인트 (`GET /api/notifications/subscribe`)
- [x] Todo 완료 시 `@TransactionalEventListener`로 Redis 채널에 발행

---

## Phase 4 — 운영 준비

### 8. Refresh Token Rotation
- [x] 토큰 갱신 시 새 Refresh Token 발급 + 기존 토큰 폐기
- [x] Redis에서 Refresh Token 단일 사용 검증 (재사용 감지)

### 9. 이메일 인증
- [x] 회원가입 시 인증 메일 발송 (JavaMailSender + Redis 토큰 저장)
- [x] 이메일 인증 확인 API (`GET /auth/verify-email?token=`)

### 10. 앱 컨테이너화
- [ ] `Dockerfile` 작성 (멀티 스테이지 빌드)
- [ ] `docker-compose.yml`에 앱 서비스 추가

### 11. 모니터링 (Actuator + Prometheus + Grafana)
- [ ] Spring Actuator 엔드포인트 활성화 (`/actuator/health`, `/actuator/metrics`)
- [ ] Micrometer 커스텀 메트릭 추가 (Todo 생성 수 등)
- [ ] Docker Compose에 Prometheus + Grafana 추가

### 12. 캐시 전략 (Redis `@Cacheable`)
- [ ] `@EnableCaching` 설정 및 `CacheManager` 빈 등록
- [ ] 자주 조회되는 데이터에 `@Cacheable` 적용
- [ ] 변경 시 `@CacheEvict`로 캐시 무효화

### 13. 비밀번호 재설정
- [ ] 재설정 링크 발송 API (`POST /auth/password/reset-request`)
- [ ] 새 비밀번호 설정 API (`POST /auth/password/reset`)
