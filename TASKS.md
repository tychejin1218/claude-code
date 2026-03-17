# 보일러플레이트 완성 태스크

> 하나씩 완성하고 체크하세요. 각 태스크는 BE/FE 모두 포함합니다.

---

## Phase 1 — 기반 다지기

### 1. Soft Delete
- [x] BE: `BaseAudit`에 `deletedAt` 추가
- [x] BE: `Member`, `Todo` 엔티티에 `@SQLRestriction` 적용
- [x] BE: Delete API를 물리 삭제 → 논리 삭제로 변경
- [ ] FE: 삭제 후 UI 동작 확인 (변경 없어야 함)
  - 삭제 API 호출 후 목록에서 해당 항목이 사라지는지 확인
  - 새로고침 후에도 삭제된 항목이 목록에 다시 나타나지 않는지 확인 (DB에 실제로 반영됐는지 검증)
  - 기존에 물리 삭제와 동일한 UX가 유지되는지 확인 (사용자 입장에서 차이 없어야 함)
- [ ] FE: API 응답의 UTC 날짜를 한국 시간(KST)으로 변환해서 표시
  - API는 UTC ISO 8601 형식으로 내려줌 (예: `2024-03-17T01:00:00Z`)
  - `dayjs.utc(...).tz('Asia/Seoul')` 또는 `Intl.DateTimeFormat`으로 UTC+9 변환
  - `dayjs` 기준: `dayjs-plugin-utc` + `dayjs-plugin-timezone` 두 플러그인 필요
  - 변환은 서버가 아닌 클라이언트에서 처리 (타임존이 다른 사용자 대응 가능)

### 2. 페이지네이션 + 필터링
- [ ] BE: Todo 목록 API에 `page`, `size`, `sort`, `status` 파라미터 추가 (QueryDSL)
- [ ] BE: `PageResponse<T>` 공통 응답 래퍼 추가
- [ ] FE: Todo 목록에 페이지 UI 또는 무한 스크롤 (`useInfiniteQuery`)
- [ ] FE: 완료/미완료 필터 탭 추가

### 3. 테스트 작성
- [x] BE: `TodoService` 단위 테스트
- [ ] BE: `AuthService` 단위 테스트
- [x] BE: `TodoController` MockMvc 통합 테스트
- [ ] FE: `useTodos` 훅 Vitest 테스트 (MSW 활용)
- [ ] FE: Playwright E2E — 로그인 → Todo 생성 → 완료 → 삭제

---

## Phase 2 — 인증 강화

### 4. RBAC (역할 기반 접근 제어)
- [ ] BE: `Member`에 `role` 필드 추가 (`ROLE_USER`, `ROLE_ADMIN`)
- [ ] BE: 관리자 전용 API 엔드포인트 추가 (`/api/admin/**`)
- [ ] BE: `@PreAuthorize("hasRole('ADMIN')")` 적용
- [ ] FE: 역할별 라우트 보호 (`AdminRoute` 컴포넌트)
- [ ] FE: 관리자 페이지 기본 틀 추가

### 5. 소셜 로그인 (Google OAuth2)
- [ ] BE: Spring Security OAuth2 설정 (`application.yml`)
- [ ] BE: OAuth2 성공 핸들러 → JWT 발급 연동
- [ ] BE: 소셜 가입 시 Member 자동 생성
- [ ] FE: 로그인 페이지에 Google 로그인 버튼 추가
- [ ] FE: OAuth2 콜백 처리 라우트 추가

### 6. Rate Limiting
- [ ] BE: Redis 기반 요청 카운터 구현 (또는 Bucket4j)
- [ ] BE: 인증 API (`/api/auth/**`)에 분당 N회 제한 적용
- [ ] FE: 429 응답 처리 + 재시도 대기 UI

---

## Phase 3 — 기능 확장

### 7. 파일 업로드
- [ ] BE: S3 Presigned URL 발급 API 추가 (`/api/files/presigned-url`)
- [ ] BE: 업로드 완료 후 URL 저장 API
- [ ] FE: 드래그앤드롭 파일 업로드 컴포넌트
- [ ] FE: Todo에 이미지 첨부 기능

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
| 1 | Soft Delete | 진행 중 (FE 확인 대기) |
| 2 | 페이지네이션 + 필터링 | 대기 |
| 3 | 테스트 작성 | 진행 중 |
| 4 | RBAC | 대기 |
| 5 | 소셜 로그인 | 대기 |
| 6 | Rate Limiting | 대기 |
| 7 | 파일 업로드 | 대기 |
| 8 | 실시간 알림 (SSE) | 대기 |
| 9 | 모니터링 | 대기 |
| 10 | API 문서 개선 | 대기 |
