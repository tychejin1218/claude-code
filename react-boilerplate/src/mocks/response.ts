import type { ApiResponse, ErrorResponse } from '@/shared/types/api';

/** 성공 응답 헬퍼 */
export const ok = <T>(data: T): ApiResponse<T> => ({
  statusCode: '200',
  message: 'OK',
  data,
});

/** 에러 응답 헬퍼 */
export const err = (statusCode: string, message: string, method: string, path: string): ErrorResponse => ({
  statusCode,
  message,
  method,
  path,
  timestamp: new Date().toISOString(),
});
