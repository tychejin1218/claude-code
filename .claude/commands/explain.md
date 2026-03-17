---
description: 지정된 파일의 역할, 코드 흐름, 핵심 로직을 설명합니다.
argument-hint: "<파일명>"
allowed-tools: ["Read", "Glob", "Grep"]
---

# 코드 설명

지정된 파일을 처음 보는 개발자도 이해할 수 있게 설명해주세요.

## 사전 준비

대상 파일과 연관 파일을 함께 읽어 전체 맥락을 파악하세요.

- **Spring Boot**: Entity, DTO, Repository, Service, Controller 연관 파일
- **React**: 타입 정의, API 함수, 훅, 관련 컴포넌트 연관 파일

## 설명 항목

### 1. 파일 역할

파일이 어느 계층/레이어에 위치하는지 설명하세요.

- **Spring Boot**: Controller → Service → Repository → DB 중 어디인지
- **React**: app / features / shared 중 어디인지, 컴포넌트·훅·API 함수·유틸 중 어떤 종류인지

### 2. 주요 패턴 및 어노테이션 의미

파일에 등장하는 패턴을 맥락에 맞게 설명:

**Spring Boot**
- `@Transactional(readOnly = true)` / `@Transactional` — Reader/Writer DB 라우팅
- `@Entity`, `@Table`, `@Column` — JPA 테이블 매핑
- `@RequiredArgsConstructor` — 생성자 주입 자동 생성
- `@Alias` — MyBatis 타입 별칭

**React**
- `useQuery` / `useMutation` — 서버 상태 관리 및 캐시 무효화
- `queryKey` 팩토리 — 일관된 캐시 키 관리
- MSW 핸들러 — 개발/테스트 환경 API Mocking
- `SuspenseBoundary` / `ErrorBoundary` — 비동기 로딩·에러 처리

### 3. 메서드/함수별 역할

각 public 메서드 또는 export된 함수·컴포넌트에 대해:
- 어떤 입력을 받아서 무엇을 반환/렌더링하는지
- 내부에서 어떤 계층·라이브러리를 호출하는지
- 예외 또는 에러 처리 방식

### 4. 데이터 흐름

요청부터 응답/화면 표시까지의 흐름을 단계별로 서술:

```
Spring Boot 예)
HTTP 요청 → Controller → Service → QueryDSL Repository → DB → DTO 변환 → BaseResponse 반환

React 예)
사용자 액션 → 컴포넌트 이벤트 핸들러 → useMutation → API 함수 → Axios 인터셉터 → API 서버
                                                      → onSuccess → invalidateQueries → 화면 갱신
```

### 5. 핵심 비즈니스 로직

코드만 봐서는 의도를 파악하기 어려운 부분을 중점 설명

## 출력 형식

- 전문 용어는 간단히 풀어서 설명
- 코드 스니펫을 인용하며 설명
- 필요 시 흐름도(텍스트 형식)로 시각화

---

대상: $ARGUMENTS
