import { createQueryKeys } from '@lukemorales/query-key-factory';
import type { TodoListParams } from '@/features/todo/types/todo';

export const todoKeys = createQueryKeys('todos', {
  all: null,
  list: (params: TodoListParams) => [params],
});
