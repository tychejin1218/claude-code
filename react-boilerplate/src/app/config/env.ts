import { z } from 'zod';

// 환경변수 런타임 검증 스키마
// 앱 시작 시 import.meta.env를 검증하여 누락/오류 시 렌더 전 fail-fast
const envSchema = z.object({
  VITE_APP_ENV: z.enum(['local', 'dev', 'stg', 'prd']),
  VITE_API_BASE_URL: z.string().url(),
});

export const env = envSchema.parse(import.meta.env);
