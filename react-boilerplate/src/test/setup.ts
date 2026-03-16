import '@testing-library/jest-dom/vitest';
import { server } from '@/mocks/server';

beforeAll(() => server.listen({ onUnhandledRequest: 'bypass' }));
afterAll(() => server.resetHandlers());
afterAll(() => server.close());
