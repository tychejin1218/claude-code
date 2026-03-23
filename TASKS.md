# 보일러플레이트 완성 태스크

> 하나씩 완성하고 체크하세요. 각 태스크는 BE/FE 모두 포함합니다.

---

## Phase 1 — 기반 다지기

### 1. Soft Delete
- [x] BE: `BaseAudit`에 `deletedAt` 추가
- [x] BE: `Member`, `Todo` 엔티티에 `@SQLRestriction` 적용
- [x] BE: Delete API를 물리 삭제 → 논리 삭제로 변경
- [x] FE: 삭제 후 UI 동작 확인 (변경 없어야 함)

### 2. 페이지네이션 + 필터링
- [x] BE: Todo 목록 API에 `page`, `size`, `sort`, `status` 파라미터 추가 (QueryDSL)
- [x] BE: `PageResponse<T>` 공통 응답 래퍼 추가
- [x] FE: Todo 목록에 페이지 UI (이전/다음 버튼)
- [x] FE: 완료/미완료 필터 탭 추가

### 3. 테스트 작성
- [x] BE: `TodoService` 단위 테스트
- [x] BE: `AuthService` 단위 테스트
- [x] BE: `TodoController` MockMvc 통합 테스트
- [x] FE: `useTodos` 훅 Vitest 테스트 (MSW 활용)
- [x] FE: Playwright E2E — 로그인 → Todo 생성 → 완료 → 삭제

---

## Phase 2 — 인증 강화

### 4. RBAC (역할 기반 접근 제어)
- [x] BE: `Member`에 `role` 필드 추가 (`ROLE_USER`, `ROLE_ADMIN`)
- [x] BE: 관리자 전용 API 엔드포인트 추가 (`/api/admin/**`)
- [x] BE: `@PreAuthorize("hasRole('ADMIN')")` 적용
- [x] FE: 역할별 라우트 보호 (`AdminRoute` 컴포넌트)
- [x] FE: 관리자 페이지 기본 틀 추가

### 5. 소셜 로그인 (Google OAuth2)
> Google Cloud Console 연동 필요 — 생략

### 6. Rate Limiting
- [x] BE: Redis 기반 요청 카운터 구현 (또는 Bucket4j)
- [x] BE: 인증 API (`/api/auth/**`)에 분당 N회 제한 적용
- [x] FE: 429 응답 처리 + 재시도 대기 UI

---

## Phase 3 — 기능 확장

### 7. 파일 업로드
> S3 대신 MinIO (Docker) 사용 — AWS SDK 코드 동일, endpoint만 로컬로 변경
- [x] Infra: Docker Compose에 MinIO 컨테이너 추가
- [x] BE: MinIO(S3 호환) Presigned URL 발급 API 추가 (`/api/files/presigned-url`)
- [x] BE: 업로드 완료 후 URL 저장 API (`PATCH /api/todos/{id}/image`)
- ~~[ ] FE: 드래그앤드롭 파일 업로드 컴포넌트~~ (생략)
- [x] FE: Todo에 이미지 첨부 기능

### 8. 실시간 알림 (SSE)
- [ ] BE: SSE 엔드포인트 추가 (`/api/notifications/subscribe`)
- [ ] BE: Todo 완료 시 이벤트 발행 (`ApplicationEventPublisher`)
- [ ] FE: SSE 구독 훅 (`useNotifications`)
- [ ] FE: Toast 알림 UI 연동

---

## Phase 4 — 운영 준비

### 9. 모니터링
- [ ] BE: Spring Actuator 엔드포인트 활성화
- [ ] BE: Micrometer 커스텀 메트릭 추가 (Todo 생성 수 등)
- [ ] BE: Docker Compose에 Prometheus + Grafana 추가
- [ ] FE: `/health` 체크 연동 (필요 시)

### 10. API 문서 개선
- [ ] BE: Swagger `@Operation`, `@ApiResponse` 어노테이션 전체 추가
- [ ] BE: 요청/응답 DTO에 `@Schema` 설명 추가
- [ ] FE: Swagger UI URL을 개발 환경 README에 명시

---

## 진행 상황

| # | 태스크 | 상태 |
|---|---|---|
| 1 | Soft Delete | 완료 |
| 2 | 페이지네이션 + 필터링 | 완료 |
| 3 | 테스트 작성 | 완료 |
| 4 | RBAC | 완료 |
| 5 | 소셜 로그인 | 생략 |
| 6 | Rate Limiting | 완료 |
| 7 | 파일 업로드 | 완료 |
| 8 | 실시간 알림 (SSE) | 대기 |
| 9 | 모니터링 | 대기 |
| 10 | API 문서 개선 | 대기 |
