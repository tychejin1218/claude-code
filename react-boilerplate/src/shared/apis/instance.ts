import axios from 'axios';
import type { ErrorResponse } from '@/shared/types/api';
import { ERROR_MESSAGES } from '@/shared/constants/messages';
// 예외: 인증 인터셉터(cross-cutting concern)로 app 레이어 참조
import { useUserStore } from '@/app/stores/useUserStore';
import { env } from '@/app/config/env';

const api = axios.create({
  baseURL: env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/** 요청 인터셉터: Authorization 헤더 주입 */
api.interceptors.request.use(
  (config) => {
    const { accessToken } = useUserStore.getState();
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

/** 응답 인터셉터: 공통 에러 처리 */
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (axios.isAxiosError(error) && error.response) {
      const { status, data } = error.response;
      const errorResponse = data as ErrorResponse;

      if (status === 401) {
        const originalRequest = error.config;

        // 토큰 갱신 요청 자체에서 401이 오면 무한 루프 방지 — 바로 로그아웃
        if (originalRequest?.url?.includes('/auth/token/refresh')) {
          useUserStore.getState().clearUser();
          window.location.href = '/';
          return Promise.reject(errorResponse);
        }

        const { refreshToken } = useUserStore.getState();

        if (refreshToken) {
          try {
            const response = await axios.post(
              `${env.VITE_API_BASE_URL}/auth/token/refresh`,
              { refreshToken },
              { headers: { 'Content-Type': 'application/json' } },
            );

            const { accessToken: newAccessToken, refreshToken: newRefreshToken } = response.data.data;
            useUserStore.getState().setAccessToken(newAccessToken);
            useUserStore.getState().setRefreshToken(newRefreshToken);

            // 원래 요청 재시도
            if (originalRequest) {
              originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
              return api(originalRequest);
            }
          } catch {
            useUserStore.getState().clearUser();
            window.location.href = '/';
            return Promise.reject(errorResponse);
          }
        } else {
          console.error(`[401] ${ERROR_MESSAGES.UNAUTHORIZED}`, errorResponse);
          useUserStore.getState().clearUser();
          window.location.href = '/';
        }

        return Promise.reject(errorResponse);
      }

      switch (status) {
        case 403:
          console.error(`[403] ${ERROR_MESSAGES.FORBIDDEN}`, errorResponse);
          break;
        case 429:
          console.error(`[429] ${ERROR_MESSAGES.RATE_LIMIT}`, errorResponse);
          break;
        case 500:
          console.error(`[500] ${ERROR_MESSAGES.SERVER_ERROR}`, errorResponse);
          break;
      }

      return Promise.reject(errorResponse);
    }

    return Promise.reject(error);
  },
);

export default api;
