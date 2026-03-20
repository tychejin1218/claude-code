export interface Todo {
  id: number;
  title: string;
  completed: boolean;
}

export interface CreateTodoRequest {
  title: string;
}

export type TodoFilter = 'all' | 'completed' | 'incomplete';

export type TodoSort = 'id' | 'title';

export interface TodoListParams {
  page: number;
  size: number;
  status: TodoFilter;
  sort: TodoSort;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
