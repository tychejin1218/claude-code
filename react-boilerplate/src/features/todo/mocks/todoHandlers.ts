import { http, HttpResponse } from 'msw';
import { ok, err } from '@/mocks/response';
import type { Todo, PageResponse } from '@/features/todo/types/todo';

const BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:9091/api';

let todos: Todo[] = [
  { id: 2, title: '리액트 공부하기', completed: false },
  { id: 1, title: '스프링 부트 공부하기', completed: true },
];
let nextId = 3;

export const todoHandlers = [
  http.get(`${BASE}/todos`, ({ request }) => {
    const url = new URL(request.url);
    const page = Number(url.searchParams.get('page') ?? 0);
    const size = Number(url.searchParams.get('size') ?? 10);
    const status = url.searchParams.get('status') ?? 'all';

    let filtered = todos;
    if (status === 'completed') filtered = todos.filter((t) => t.completed);
    if (status === 'incomplete') filtered = todos.filter((t) => !t.completed);

    const totalElements = filtered.length;
    const totalPages = Math.max(1, Math.ceil(totalElements / size));
    const content = filtered.slice(page * size, (page + 1) * size);

    const pageResponse: PageResponse<Todo> = {
      content,
      page,
      size,
      totalElements,
      totalPages,
      last: (page + 1) * size >= totalElements,
    };

    return HttpResponse.json(ok(pageResponse));
  }),

  http.post(`${BASE}/todos`, async ({ request }) => {
    const { title } = (await request.json()) as { title: string };
    const todo: Todo = { id: nextId++, title, completed: false };
    todos = [todo, ...todos];
    return HttpResponse.json(ok(todo));
  }),

  http.patch(`${BASE}/todos/:id/complete`, ({ params }) => {
    const id = Number(params.id);
    const todo = todos.find((t) => t.id === id);
    if (!todo) {
      return HttpResponse.json(err('804', '존재하지 않는 항목입니다.', 'PATCH', `/todos/${id}`), {
        status: 404,
      });
    }
    todos = todos.map((t) => (t.id === id ? { ...t, completed: true } : t));
    return HttpResponse.json(ok({ ...todo, completed: true }));
  }),

  http.delete(`${BASE}/todos/:id`, ({ params }) => {
    const id = Number(params.id);
    if (!todos.find((t) => t.id === id)) {
      return HttpResponse.json(err('804', '존재하지 않는 항목입니다.', 'DELETE', `/todos/${id}`), {
        status: 404,
      });
    }
    todos = todos.filter((t) => t.id !== id);
    return HttpResponse.json(ok(null));
  }),
];
