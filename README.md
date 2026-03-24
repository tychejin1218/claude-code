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

## 개발 방식 (Claude Code)

백엔드와 프론트엔드는 독립적인 터미널에서 각각 Claude Code를 실행합니다.

```bash
# 터미널 1 — 백엔드
cd springboot-boilerplate
claude

# 터미널 2 — 프론트엔드
cd react-boilerplate
claude
```

두 프로젝트는 REST API로 협조합니다. API 스펙 변경 시 백엔드에서 먼저 수정하고 프론트엔드에 반영하는 순서로 작업합니다.

