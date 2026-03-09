---
name: cleanup
description: 지정된 파일에서 미사용 import, 임시 코드(System.out.println, 주석 처리된 코드)를 제거합니다. 코드 정리가 필요할 때 사용합니다.
argument-hint: "[파일명]"
disable-model-invocation: true
---

# 코드 정리

지정된 파일을 아래 기준으로 정리해주세요.

## 사전 준비

대상 파일을 읽고 현재 상태를 파악한 후 진행하세요.

## 정리 항목

### 1. Import 정리
- 사용하지 않는 import 제거
- 중복 import 제거
- 와일드카드 import(`*`) 제거 후 개별 import로 교체

### 2. 임시 코드 제거
- `System.out.println` 제거 (로깅은 `@Slf4j` + `log.debug/info/error` 사용)
- 주석 처리된 코드 블록 제거
- `// TODO`, `// FIXME` 확인 후 처리 가능한 것은 즉시 처리, 아닌 것은 그대로 유지

### 3. 후처리

정리 완료 후 `/check $ARGUMENTS` 스킬을 실행하세요 (포맷 정렬 + 정적 분석).

## 주의

- 로직 변경 없이 **형식만** 정리할 것
- 의미 있는 주석(비즈니스 규칙 설명 등)은 유지할 것
- 확신이 없는 코드는 임의로 삭제하지 말 것

---

대상: $ARGUMENTS
