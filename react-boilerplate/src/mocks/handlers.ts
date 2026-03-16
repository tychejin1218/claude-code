import { authHandlers } from '@/features/auth/mocks/authHandlers';
import { todoHandlers } from '@/features/todo/mocks/todoHandlers';

export const handlers = [...authHandlers, ...todoHandlers];
