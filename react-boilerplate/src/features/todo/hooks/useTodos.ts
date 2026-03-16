import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { todoKeys } from '@/shared/apis/queryKeys';
import { deleteTodo, getTodos, patchTodoComplete, postTodo } from '@/features/todo/apis/todoApi';
import type { CreateTodoRequest } from '@/features/todo/types/todo';

export const useTodos = () =>
  useQuery({
    queryKey: todoKeys.all.queryKey,
    queryFn: getTodos,
    select: (data) => data.data,
  });

export const useCreateTodo = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateTodoRequest) => postTodo(data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: todoKeys.all.queryKey }),
  });
};

export const useCompleteTodo = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => patchTodoComplete(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: todoKeys.all.queryKey }),
  });
};

export const useDeleteTodo = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => deleteTodo(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: todoKeys.all.queryKey }),
  });
};
