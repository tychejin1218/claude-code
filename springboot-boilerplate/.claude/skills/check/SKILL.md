---
name: check
description: Checkstyle + PMD 정적 분석과 Google Style 포맷 정렬을 실행합니다. 코드 생성·수정·리팩토링 완료 후 검증할 때 사용합니다.
argument-hint: "[파일명]"
---

# 정적 분석 & 포맷 검증

지정된 파일에 대해 코드 포맷 정렬과 정적 분석을 실행합니다.

## 실행 순서

1. JetBrains MCP `reformat_file`로 Google Java Style 자동 정렬 (`projectPath: /path/to/your/project` 항상 포함)
2. `./gradlew checkstyleMain pmdMain` 실행
3. 오류 발생 시 내용을 분석하여 수정 후 재실행
4. 통과 시 결과 보고

---

대상: $ARGUMENTS
