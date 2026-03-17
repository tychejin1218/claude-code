# React Boilerplate

React + Vite + TypeScript 기반의 프론트엔드 보일러플레이트입니다. 인증(로그인/회원가입)과 할 일 관리 기능을 포함한 실전 프로젝트 구조를 제공합니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| UI | React 19, Tailwind CSS 4 |
| 빌드 | Vite 7, TypeScript 5 |
| 서버 상태 | React Query v5 (`@tanstack/react-query`) |
| 클라이언트 상태 | Zustand 5 |
| HTTP 클라이언트 | Axios |
| 유효성 검사 | Zod |
| API Mocking | MSW 2 |
| 단위 테스트 | Vitest, Testing Library |
| E2E 테스트 | Playwright |
| 코드 품질 | ESLint, Prettier |

---

## 프로젝트 구조

```
src/
├── app/                        # 앱 전역 설정
│   ├── config/env.ts           # 환경변수 런타임 검증 (Zod)
│   ├── layout/Layout.tsx       # 공통 레이아웃
│   ├── pages/                  # 앱 레벨 페이지 (Home, NotFound)
│   ├── providers/              # React Query 등 전역 Provider
│   ├── router/router.tsx       # 라우터 정의 (React Router v7)
│   └── stores/                 # Zustand 전역 상태 (user, toast, dialog)
│
├── features/                   # 기능별 모듈 (Feature-Sliced 구조)
│   ├── auth/                   # 인증 기능
│   │   ├── apis/authApi.ts     # 로그인/회원가입/로그아웃 API
│   │   ├── components/         # AuthRoute (인증 가드)
│   │   ├── mocks/              # MSW 핸들러
│   │   ├── pages/              # LoginPage, RegisterPage, AuthErrorPage
│   │   └── types/user.ts       # User 타입 정의
│   └── todo/                   # 할 일 관리 기능
│       ├── apis/todoApi.ts     # 할 일 CRUD API
│       ├── components/         # TodoItem 컴포넌트
│       ├── hooks/useTodos.ts   # React Query 훅
│       ├── mocks/              # MSW 핸들러
│       ├── pages/TodoPage.tsx  # 할 일 목록 페이지
│       └── types/todo.ts       # Todo 타입 정의
│
├── shared/                     # 공통 모듈
│   ├── apis/                   # Axios 인스턴스, API 클라이언트, Query Keys
│   ├── components/             # 공통 UI 컴포넌트 (ErrorBoundary, Toast, Dialog 등)
│   ├── constants/              # 공통 상수
│   ├── hooks/                  # 공통 훅
│   ├── types/                  # 공통 타입 (api.ts, env.d.ts)
│   └── utils/token.ts          # JWT 유틸리티
│
├── mocks/                      # MSW 브라우저/서버 설정
├── test/setup.ts               # Vitest 전역 설정
├── App.tsx
└── main.tsx
```

---

## 로컬 개발 환경 설정

### 1. 의존성 설치

```bash
npm install
```

### 2. 환경변수 파일 설정

`env/` 디렉터리에 환경별 `.env` 파일이 위치합니다. 로컬 개발을 위해 `env/.env.local` 파일을 수정하세요.

```bash
# env/.env.local
VITE_APP_ENV=local
VITE_API_BASE_URL=http://localhost:8080/api
VITE_ENABLE_MSW=true   # MSW 사용 시 true, 실제 백엔드 연동 시 false
```

### 3. 개발 서버 실행

```bash
# .env.local 기준 (기본)
npm run dev

# 환경별 실행
npm run dev:dev   # .env.dev 사용
npm run dev:stg   # .env.stg 사용
npm run dev:prd   # .env.prd 사용
```

---

## 환경변수 설명

| 변수명 | 필수 | 설명 |
|--------|------|------|
| `VITE_APP_ENV` | 필수 | 앱 실행 환경 (`local` \| `dev` \| `stg` \| `prd`) |
| `VITE_API_BASE_URL` | 필수 | API 서버 Base URL (예: `http://localhost:8080/api`) |
| `VITE_ENABLE_MSW` | 선택 | MSW 활성화 여부 (`true` \| `false`, 기본값: `false`) |

> 환경변수는 앱 시작 시 Zod 스키마로 런타임 검증됩니다. 누락되거나 잘못된 값이 있으면 렌더 전에 오류가 발생합니다.

---

## MSW (Mock Service Worker)

MSW를 사용하면 실제 백엔드 없이 브라우저에서 API 요청을 가로채 Mock 응답을 반환합니다.

### Mock 개발 모드 (백엔드 없이 실행)

```bash
# env/.env.local에서 MSW 활성화
VITE_ENABLE_MSW=true

npm run dev
```

Mock 사용자 계정: `test@example.com` / `password1`

### 실제 백엔드 연동 모드

```bash
# env/.env.local에서 MSW 비활성화
VITE_ENABLE_MSW=false
VITE_API_BASE_URL=http://localhost:8080/api  # 실제 백엔드 주소

npm run dev
```

MSW 핸들러는 `src/features/*/mocks/` 디렉터리에 기능별로 관리됩니다.

---

## 주요 기능

### 인증
- **로그인** (`/`): 이메일 + 비밀번호로 로그인, JWT 토큰 발급
- **회원가입** (`/register`): 이름 + 이메일 + 비밀번호로 계정 생성
- **인증 가드**: `AuthRoute` 컴포넌트로 비인증 접근 차단 및 리다이렉트

### 할 일 관리
- **목록 조회** (`/todos`): 전체 할 일 목록 표시
- **할 일 추가**: 텍스트 입력 후 추가
- **완료 처리**: 개별 할 일 완료 상태 토글
- **삭제**: 개별 할 일 삭제

---

## 테스트 실행

### 단위 테스트 (Vitest)

```bash
# 감시 모드 (개발 중)
npm run test

# 단일 실행
npm run test:run

# 커버리지 리포트
npm run test:coverage
```

### E2E 테스트 (Playwright)

```bash
# Headless 실행
npm run e2e

# UI 모드
npm run e2e:ui

# 브라우저 표시 모드
npm run e2e:headed

# 리포트 확인
npm run e2e:report
```

---

## 코드 품질

### ESLint

```bash
# 검사
npm run lint

# 자동 수정
npm run lint:fix
```

### Prettier

```bash
# 포매팅 적용
npm run format

# 포매팅 검사만
npm run format:check
```

---

## 빌드

```bash
npm run build:dev   # Development 빌드
npm run build:stg   # Staging 빌드
npm run build:prd   # Production 빌드 (console, debugger 자동 제거)
```
