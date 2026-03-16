import api from '@/shared/apis/instance';
import type { ApiResponse } from '@/shared/types/api';

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  name: string;
  password: string;
}

export const postLogin = (data: LoginRequest): Promise<ApiResponse<TokenResponse>> =>
  api.post<ApiResponse<TokenResponse>>('/auth/login', data).then((res) => res.data);

export const postRegister = (data: RegisterRequest): Promise<ApiResponse<TokenResponse>> =>
  api.post<ApiResponse<TokenResponse>>('/auth/register', data).then((res) => res.data);

export const postLogout = (refreshToken: string): Promise<ApiResponse<null>> =>
  api.post<ApiResponse<null>>('/auth/logout', { refreshToken }).then((res) => res.data);
