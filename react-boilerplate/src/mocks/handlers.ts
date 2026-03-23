import { authHandlers } from '@/features/auth/mocks/authHandlers';
import { todoHandlers } from '@/features/todo/mocks/todoHandlers';
import { fileHandlers } from '@/mocks/fileHandlers';
import { notificationHandlers } from '@/features/notification/mocks/notificationHandlers';

export const handlers = [...authHandlers, ...todoHandlers, ...fileHandlers, ...notificationHandlers];
