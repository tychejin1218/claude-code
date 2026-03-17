# Boilerplate

Spring Boot + React 풀스택 보일러플레이트 모노레포

---

## 프로젝트 구조

```
claude-code/
├── springboot-boilerplate/   # Spring Boot 백엔드 API 서버
└── react-boilerplate/        # React 프론트엔드 앱
```

## 프로젝트 소개

| 프로젝트 | 설명 | 문서 |
|---------|------|------|
| [springboot-boilerplate](./springboot-boilerplate) | Spring Boot 4 · Java 17 · JPA · Redis · JWT | [README](./springboot-boilerplate/README.md) |
| [react-boilerplate](./react-boilerplate) | React 19 · Vite 7 · TypeScript · React Query · Zustand | [README](./react-boilerplate/README.md) |

---

## 로컬 개발 환경

### 1. 인프라 실행 (MySQL + Redis)

```bash
cd springboot-boilerplate
docker-compose up -d
```

### 2. 백엔드 실행

```bash
cd springboot-boilerplate
./gradlew bootRun
# → http://localhost:8080/api
# → http://localhost:8080/api/swagger-ui/index.html
```

### 3. 프론트엔드 실행

```bash
cd react-boilerplate
cp env/.env.dev env/.env.local   # 최초 1회
# env/.env.local 에서 VITE_API_BASE_URL 수정
npm install
npm run dev
# → http://localhost:5173
```

---

## 기술 스택

### Backend
- Java 17 / Spring Boot 4 / Gradle
- JPA + QueryDSL + MyBatis
- MySQL 8.0 / Redis 7.0
- JWT (JJWT) / Jasypt 암호화
- Checkstyle / PMD / JaCoCo

### Frontend
- React 19 / Vite 7 / TypeScript 5
- TanStack Query v5 / Zustand 5
- Tailwind CSS 4 / Axios
- MSW 2 / Vitest / Playwright

---

## 포함된 예제 기능

- **인증**: 회원가입 · 로그인 · 로그아웃 · 토큰 재발급 (JWT Refresh)
- **할 일**: 목록 조회 · 추가 · 완료 처리 · 삭제

---

## Claude Code 설정

각 프로젝트에 `.claude/` 자동화 설정이 포함되어 있습니다.

| 종류 | 내용 |
|------|------|
| `CLAUDE.md` | 프로젝트 컨텍스트 · 코드 규칙 |
| `.claude/agents/` | 자동 트리거 에이전트 (debugger, code-reviewer 등) |
| `.claude/commands/` | 슬래시 커맨드 (`/crud`, `/review`, `/scaffold` 등) |
| `.claude/skills/` | 자동 실행 스킬 (`/check`, `/test` 등) |
| `.claude/docs/` | 아키텍처 · 컨벤션 참고 문서 |

신규 프로젝트에 적용하려면:

```bash
cp -r springboot-boilerplate/.claude/ /path/to/new-project/
cp springboot-boilerplate/CLAUDE.md /path/to/new-project/
```
