import { useQuery, useMutation, useSuspenseQuery, useQueryClient } from '@tanstack/react-query';
import type { QueryKey, UseQueryOptions, UseMutationOptions, UseSuspenseQueryOptions } from '@tanstack/react-query';
import type { ErrorResponse } from '@/shared/types/api';

type UseApiQueryOptions<T> = Omit<UseQueryOptions<T, ErrorResponse>, 'queryKey' | 'queryFn'>;
type UseApiMutationOptions<TData, TVariables> = Omit<UseMutationOptions<TData, ErrorResponse, TVariables>, 'mutationFn'>;
type UseApiSuspenseQueryOptions<T> = Omit<UseSuspenseQueryOptions<T, ErrorResponse>, 'queryKey' | 'queryFn'>;

/**
 * useQuery 래퍼
 * - queryKey, queryFn 필수 전달
 * - 나머지 옵션은 선택적으로 오버라이드
 * - 성공: T (ApiResponse.data), 에러: ErrorResponse
 */
export const useApiQuery = <T>(queryKey: QueryKey, queryFn: () => Promise<T>, options?: UseApiQueryOptions<T>) => {
  return useQuery<T, ErrorResponse>({
    queryKey,
    queryFn,
    ...options,
  });
};

/**
 * useSuspenseQuery 래퍼
 * - data는 항상 T (undefined 불가능)
 * - 로딩 → 가장 가까운 Suspense boundary
 * - 에러 → 가장 가까운 ErrorBoundary
 */
export const useApiSuspenseQuery = <T>(queryKey: QueryKey, queryFn: () => Promise<T>, options?: UseApiSuspenseQueryOptions<T>) => {
  return useSuspenseQuery<T, ErrorResponse>({ queryKey, queryFn, ...options });
};

/**
 * useMutation 래퍼
 * - mutationFn 필수 전달
 * - invalidateKeys 전달 시 성공 후 자동 invalidate
 * - 나머지 옵션은 선택적으로 오버라이드
 * - 성공: TData (ApiResponse.data), 에러: ErrorResponse
 */
export const useApiMutation = <TData, TVariables = void>(
  mutationFn: (variables: TVariables) => Promise<TData>,
  options?: UseApiMutationOptions<TData, TVariables>,
  invalidateKeys?: QueryKey[],
) => {
  const queryClient = useQueryClient();

  return useMutation<TData, ErrorResponse, TVariables>({
    ...options,
    mutationFn,
    onSuccess: (...args) => {
      invalidateKeys?.forEach((key) => {
        queryClient.invalidateQueries({ queryKey: key });
      });
      options?.onSuccess?.(...args);
    },
  });
};
