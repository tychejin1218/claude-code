import { authHandlers } from '@/features/auth/mocks/authHandlers';
import { todoHandlers } from '@/features/todo/mocks/todoHandlers';
import { fileHandlers } from '@/mocks/fileHandlers';

export const handlers = [...authHandlers, ...todoHandlers, ...fileHandlers];
