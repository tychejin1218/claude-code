# template-ds-backend-type7

백엔드 개발 표준이 적용된 **Claude Code 템플릿 프로젝트**입니다.
이 레포지토리의 `.claude/` 디렉토리와 `CLAUDE.md`를 신규 프로젝트에 복사하여 Claude Code 개발 환경을 빠르게 구성할 수 있습니다.

---

## 디렉토리 구조

```
CLAUDE.md                  ← 프로젝트 전체 규칙 요약 (Claude Code 진입점)
.claude/
├── docs/                  ← Claude Code 가이드 문서
│   ├── architecture.md
│   ├── naming-conventions.md
│   ├── api-response.md
│   ├── db-conventions.md
│   ├── configuration.md
│   ├── data-access.md
│   ├── testing.md
│   ├── openapi.md
│   ├── commenting.md
│   └── package.md
├── commands/              ← 슬래시 커맨드 정의 (/crud, /review, /perf 등)
├── skills/                ← 재사용 스킬 정의 (/check, /test, /cleanup)
└── agents/                ← 전문 서브에이전트 정의
```

---

## 슬래시 커맨드

| 커맨드 | 동작 |
|--------|------|
| `/crud <Entity 파일경로>` | DTO·QueryDSL Repository·Service·Controller·Test 자동 생성 |
| `/review <파일명>` | 코드 리뷰 (버그·성능·보안·컨벤션) |
| `/perf <파일명>` | 성능 이슈 감지 (N+1·readOnly 누락·페이징·쿼리 비효율) |
| `/refactor <파일명>` | 리팩토링 계획 수립 → 승인 → 실행 |
| `/api-doc <파일명>` | Swagger 어노테이션 자동 추가 |
| `/commit` | git diff 기반 커밋 메시지 생성 → 확인 후 커밋 |
| `/explain <파일명>` | 코드 흐름·로직·어노테이션 의미 설명 |

## 스킬

| 스킬 | 동작 |
|------|------|
| `/check [파일명]` | Google Style 포맷 정렬 + Checkstyle/PMD 정적 분석 |
| `/test <파일명>` | JUnit 5 테스트 코드 생성 |
| `/cleanup <파일명>` | 미사용 import 제거 + 임시 코드 정리 |

---

## 신규 프로젝트 적용 방법

```bash
# 이 레포의 Claude Code 설정을 신규 프로젝트에 복사
cp -r .claude/ /path/to/new-project/
cp CLAUDE.md /path/to/new-project/
cp .claudeignore /path/to/new-project/
```
