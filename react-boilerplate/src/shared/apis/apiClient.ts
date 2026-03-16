import api from './instance';
import type { ApiResponse } from '@/shared/types/api';
import type { AxiosRequestConfig } from 'axios';

const apiClient = {
  get: async <T>(url: string, params?: unknown, config?: AxiosRequestConfig): Promise<T> => {
    const { data } = await api.get<ApiResponse<T>>(url, { ...config, params });
    return data.data;
  },

  post: async <T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> => {
    const { data } = await api.post<ApiResponse<T>>(url, body, config);
    return data.data;
  },

  put: async <T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> => {
    const { data } = await api.put<ApiResponse<T>>(url, body, config);
    return data.data;
  },

  patch: async <T>(url: string, body?: unknown, config?: AxiosRequestConfig): Promise<T> => {
    const { data } = await api.patch<ApiResponse<T>>(url, body, config);
    return data.data;
  },

  delete: async <T = null>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    const { data } = await api.delete<ApiResponse<T>>(url, config);
    return data.data;
  },
};

export default apiClient;
