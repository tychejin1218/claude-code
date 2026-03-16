import { http, HttpResponse } from 'msw';
import { ok, err } from '@/mocks/response';

const BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:9091/api';

/** 이미 가입된 이메일 목록 (Mock 상태) */
const registeredEmails = new Set(['test@example.com']);

/** Mock 회원 정보 */
const mockUsers: Record<string, { name: string; password: string }> = {
  'test@example.com': { name: '테스트', password: 'password1' },
};

const makeFakeToken = (email: string) =>
  btoa(JSON.stringify({ alg: 'HS256' })) +
  '.' +
  btoa(JSON.stringify({ sub: email })) +
  '.signature';

export const authHandlers = [
  http.post(`${BASE}/auth/register`, async ({ request }) => {
    const { email, name, password } = (await request.json()) as {
      email: string;
      name: string;
      password: string;
    };

    if (registeredEmails.has(email)) {
      return HttpResponse.json(
        err('803', '이미 사용 중인 이메일입니다.', 'POST', '/auth/register'),
        { status: 409 },
      );
    }

    registeredEmails.add(email);
    mockUsers[email] = { name, password };

    return HttpResponse.json(
      ok({ accessToken: makeFakeToken(email), refreshToken: makeFakeToken(email) }),
    );
  }),

  http.post(`${BASE}/auth/login`, async ({ request }) => {
    const { email, password } = (await request.json()) as {
      email: string;
      password: string;
    };

    const user = mockUsers[email];
    if (!user || user.password !== password) {
      return HttpResponse.json(
        err('805', '이메일 또는 비밀번호가 올바르지 않습니다.', 'POST', '/auth/login'),
        { status: 401 },
      );
    }

    return HttpResponse.json(
      ok({ accessToken: makeFakeToken(email), refreshToken: makeFakeToken(email) }),
    );
  }),

  http.post(`${BASE}/auth/logout`, () => HttpResponse.json(ok(null))),
];
