---
name: feature-scaffolder
description: 새 feature 디렉토리 구조와 보일러플레이트 파일을 생성합니다. features/todo를 참고해서 동일한 패턴으로 생성합니다. 새 기능, 페이지, feature 추가 요청 시 자동으로 호출됩니다.
model: sonnet
---

# Feature 스캐폴딩 에이전트

새로운 feature 디렉토리 구조와 보일러플레이트 파일을 생성하는 전문 에이전트입니다.

## 사전 준비

작업 시작 전 반드시 아래를 읽으세요:
- `src/features/todo/` — 참조 구현체 (전체 디렉토리)
  - `types/todo.ts` — 타입 정의 패턴
  - `apis/todoApi.ts` — API 함수 패턴
  - `hooks/useTodos.ts` — TanStack Query 훅 패턴
  - `mocks/todoHandlers.ts` — MSW 핸들러 패턴
  - `pages/TodoPage.tsx` — 페이지 컴포넌트 패턴
  - `components/TodoItem.tsx` — UI 컴포넌트 패턴
- `src/shared/apis/queryKeys.ts` — QueryKey 팩토리 패턴
- `src/mocks/handlers.ts` — 핸들러 통합 파일

파일을 읽은 후 **ultrathink**로 생성할 feature의 구조와 타입을 설계하세요.

## 생성 파일 목록

`src/features/{feature}/` 하위에 아래 파일들을 생성하세요:

1. **타입 정의** — `types/{feature}.ts`
2. **API 함수** — `apis/{feature}Api.ts`
3. **TanStack Query 훅** — `hooks/use{Feature}s.ts`
4. **MSW 핸들러** — `mocks/{feature}Handlers.ts`
5. **페이지 컴포넌트** — `pages/{Feature}Page.tsx`
6. **UI 컴포넌트** (필요 시) — `components/{Feature}Item.tsx` 등

## 생성 후 처리

생성한 파일 외에 아래 기존 파일을 수정하세요:

7. **QueryKey 등록** — `src/shared/apis/queryKeys.ts`에 새 feature의 키 팩토리 추가
8. **MSW 핸들러 통합** — `src/mocks/handlers.ts`에 새 핸들러 import 및 추가
9. **라우터 등록** — `src/app/router/router.tsx`에 `lazy()` + `withSuspense()`로 페이지 등록

## 필수 준수 규칙

### 타입 정의 패턴

```ts
// types/{feature}.ts
export interface {Feature} {
  id: number;
  // ... 필드
}

export interface Create{Feature}Request {
  // ... 생성 요청 필드
}
```

### API 함수 패턴

```ts
// apis/{feature}Api.ts
import api from '@/shared/apis/instance';
import type { ApiResponse } from '@/shared/types/api';
import type { {Feature}, Create{Feature}Request } from '@/features/{feature}/types/{feature}';

export const get{Feature}s = (): Promise<ApiResponse<{Feature}[]>> =>
  api.get<ApiResponse<{Feature}[]>>('{endpoint}').then((res) => res.data);

export const post{Feature} = (data: Create{Feature}Request): Promise<ApiResponse<{Feature}>> =>
  api.post<ApiResponse<{Feature}>>('{endpoint}', data).then((res) => res.data);
```

### TanStack Query 훅 패턴

```ts
// hooks/use{Feature}s.ts
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { {feature}Keys } from '@/shared/apis/queryKeys';

export const use{Feature}s = () =>
  useQuery({
    queryKey: {feature}Keys.all.queryKey,
    queryFn: get{Feature}s,
    select: (data) => data.data,
  });

export const useCreate{Feature} = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: Create{Feature}Request) => post{Feature}(data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: {feature}Keys.all.queryKey }),
  });
};
```

### MSW 핸들러 패턴

```ts
// mocks/{feature}Handlers.ts
import { http, HttpResponse } from 'msw';
import { ok, err } from '@/mocks/response';

const BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:9091/api';

export const {feature}Handlers = [
  http.get(`${BASE}/{endpoint}`, () => HttpResponse.json(ok(items))),
  // ... 나머지 CRUD 핸들러
];
```

### 페이지 컴포넌트 패턴

```tsx
// pages/{Feature}Page.tsx
import { useState } from 'react';
import { use{Feature}s, useCreate{Feature} } from '@/features/{feature}/hooks/use{Feature}s';

const {Feature}Page = () => {
  const { data: items, isLoading } = use{Feature}s();
  // ... 로직

  return (
    <div className="mx-auto max-w-lg py-6">
      <h2 className="mb-4 text-xl font-bold text-gray-800">{Feature} 목록</h2>
      {/* ... UI */}
    </div>
  );
};

export default {Feature}Page;
```

## 출력 순서

1. 타입 정의 파일 생성
2. API 함수 파일 생성
3. TanStack Query 훅 파일 생성
4. MSW 핸들러 파일 생성
5. 페이지 컴포넌트 파일 생성
6. UI 컴포넌트 파일 생성 (필요 시)
7. `queryKeys.ts` 수정 (새 키 팩토리 추가)
8. `handlers.ts` 수정 (새 핸들러 통합)
9. `router.tsx` 수정 (새 페이지 라우트 등록)
10. `/check` 스킬 실행 (ESLint + Prettier 검사)
11. 생성/수정된 파일 목록 보고
