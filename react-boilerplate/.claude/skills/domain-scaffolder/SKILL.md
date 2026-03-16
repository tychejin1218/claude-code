---
name: domain-scaffolder
description: 새 도메인 scaffolding (types, api, queryKeys, mocks)
argument-hint: '[domain-name]'
disable-model-invocation: true
allowed-tools: Read, Write, Bash, Glob, Grep, Edit, AskUserQuestion
---

# /domain-scaffolder — 도메인 Scaffolding 스킬

사용자가 `/domain-scaffolder <도메인명>` 형태로 실행하면, 프로젝트 컨벤션에 맞는 도메인 파일 4개를 자동 생성한다.

---

## 실행 전 확인

- 인자로 전달된 도메인명이 비어 있으면 `"사용법: /domain-scaffolder <domain-name>"` 안내 후 종료한다.
- 도메인명은 **소문자 kebab-case 또는 camelCase**만 허용한다. (예: `user`, `question`, `testItem`)
- `src/features/{domain}/` 디렉토리가 이미 존재하면 `"이미 존재하는 도메인입니다: {domain}"` 안내 후 종료한다.

---

## 단계 1: 엔티티 정보 수집

AskUserQuestion으로 아래 정보를 수집한다.

1. **주요 엔티티명** (PascalCase, 예: `User`, `Question`)
   - 도메인명에서 자동 추론하여 기본값 제안 (예: `user` → `User`)
2. **주요 필드** (예: `id: number, name: string, email: string`)
3. **필요한 API 엔드포인트** (기본 제안: 목록 조회, 상세 조회, 생성)

---

## 단계 2: 파일 생성

아래 파일들을 생성한다. **기존 auth 도메인의 패턴을 참고한다.**

### 2-1. `src/features/{domain}/types/{domain}.ts`

```ts
import { z } from 'zod';

// Zod 스키마
export const {entity}Schema = z.object({
  // 사용자가 입력한 필드
});
export type {Entity} = z.infer<typeof {entity}Schema>;

// Request 타입 (생성 API용)
export const create{Entity}RequestSchema = z.object({
  // id 제외한 필드
});
export type Create{Entity}Request = z.infer<typeof create{Entity}RequestSchema>;
```

### 2-2. `src/features/{domain}/apis/{domain}Api.ts`

```ts
import apiClient from '@/shared/apis/apiClient';
import type { {Entity}, Create{Entity}Request } from '@/features/{domain}/types/{domain}';

export const get{Entity}List = () => apiClient.get<{Entity}[]>('/{domain}');

export const get{Entity}Detail = (id: string) => apiClient.get<{Entity}>(`/{domain}/${id}`);

export const post{Entity} = (body: Create{Entity}Request) => apiClient.post<{Entity}>('/{domain}', body);
```

- 엔드포인트 구성은 사용자 입력에 따라 조정한다.
- URL 경로는 `/{domain}/...` 형태를 기본으로 한다.

### 2-3. `src/shared/apis/queryKeys.ts` (기존 파일에 추가)

```ts
import { createQueryKeys } from '@lukemorales/query-key-factory';

export const {domain}Keys = createQueryKeys('{domain}', {
  all: null,
});
```

- 이미 존재하는 파일이므로 **새로 생성하지 않고**, 기존 파일 하단에 쿼리 키를 추가한다.

### 2-4. `src/mocks/handlers/{domain}.ts`

```ts
import { http, HttpResponse } from 'msw';
import { ok } from '@/mocks/response';
import type { {Entity}, Create{Entity}Request } from '@/features/{domain}/types/{domain}';

let nextId = 1;
const items: {Entity}[] = [
  // 샘플 데이터 2건 (필드 기반 자동 생성)
];

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const {domain}Handlers = [
  // 목록 조회
  http.get(`${BASE_URL}/{domain}`, () => {
    return HttpResponse.json(ok(items));
  }),

  // 생성
  http.post(`${BASE_URL}/{domain}`, async ({ request }) => {
    const body = (await request.json()) as Create{Entity}Request;
    const newItem: {Entity} = {
      id: nextId++,
      ...body,
    };
    items.push(newItem);
    return HttpResponse.json(ok(newItem));
  }),
];
```

---

## 단계 3: 출력

생성 완료 후 아래 형식으로 요약한다.

```
도메인 생성 완료: {domain}

생성된 파일:
- src/features/{domain}/types/{domain}.ts
- src/features/{domain}/apis/{domain}Api.ts
- src/mocks/handlers/{domain}.ts

수정된 파일:
- src/shared/apis/queryKeys.ts ({domain}Keys 추가)

TODO:
- src/mocks/handlers.ts에 {domain}Handlers 등록
```

---

## 코드 스타일 규칙

- **import 경로**: `@/` alias 사용
- **type import**: `import type { ... }` 구문 사용
- **Prettier**: printWidth 160, single quote, trailing comma `all`, semicolon 사용
- **한국어 주석** 사용
- **Zod 스키마** 기반 타입 정의 (스키마 먼저, `z.infer`로 타입 추론)
