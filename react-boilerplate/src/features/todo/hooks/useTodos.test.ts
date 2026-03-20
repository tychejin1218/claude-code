import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { createElement } from 'react';
import { useTodos, useCreateTodo, useCompleteTodo, useDeleteTodo } from './useTodos';
import type { TodoListParams } from '@/features/todo/types/todo';

const defaultParams: TodoListParams = { page: 0, size: 10, status: 'all', sort: 'id' };

const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  return ({ children }: { children: React.ReactNode }) =>
    createElement(QueryClientProvider, { client: queryClient }, children);
};

describe('useTodos', () => {
  it('할 일 목록을 페이지 응답으로 반환한다', async () => {
    const { result } = renderHook(() => useTodos(defaultParams), {
      wrapper: createWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data?.content).toHaveLength(2);
    expect(result.current.data?.page).toBe(0);
    expect(result.current.data?.totalElements).toBe(2);
  });

  it('completed 필터로 완료된 항목만 반환한다', async () => {
    const params: TodoListParams = { page: 0, size: 10, status: 'completed', sort: 'id' };
    const { result } = renderHook(() => useTodos(params), {
      wrapper: createWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data?.content.every((t) => t.completed)).toBe(true);
  });

  it('incomplete 필터로 미완료 항목만 반환한다', async () => {
    const params: TodoListParams = { page: 0, size: 10, status: 'incomplete', sort: 'id' };
    const { result } = renderHook(() => useTodos(params), {
      wrapper: createWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));

    expect(result.current.data?.content.every((t) => !t.completed)).toBe(true);
  });
});

describe('useCreateTodo', () => {
  it('할 일 추가 성공 시 쿼리를 무효화한다', async () => {
    const queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } },
    });
    const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');

    const wrapper = ({ children }: { children: React.ReactNode }) =>
      createElement(QueryClientProvider, { client: queryClient }, children);

    const { result } = renderHook(() => useCreateTodo(defaultParams), { wrapper });

    result.current.mutate({ title: '새 할 일' });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(invalidateSpy).toHaveBeenCalled();
  });
});

describe('useCompleteTodo', () => {
  it('완료 처리 성공 시 쿼리를 무효화한다', async () => {
    const queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } },
    });
    const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');

    const wrapper = ({ children }: { children: React.ReactNode }) =>
      createElement(QueryClientProvider, { client: queryClient }, children);

    const { result } = renderHook(() => useCompleteTodo(defaultParams), { wrapper });

    result.current.mutate(2);

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(invalidateSpy).toHaveBeenCalled();
  });
});

describe('useDeleteTodo', () => {
  it('삭제 성공 시 쿼리를 무효화한다', async () => {
    const queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } },
    });
    const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries');

    const wrapper = ({ children }: { children: React.ReactNode }) =>
      createElement(QueryClientProvider, { client: queryClient }, children);

    const { result } = renderHook(() => useDeleteTodo(defaultParams), { wrapper });

    result.current.mutate(1);

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(invalidateSpy).toHaveBeenCalled();
  });
});
