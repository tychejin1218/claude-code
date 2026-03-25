import { http, HttpResponse } from 'msw';
import { ok, err } from '@/mocks/response';
import { env } from '@/app/config/env';

const BASE = env.VITE_API_BASE_URL;

/** 이미 가입된 이메일 목록 (Mock 상태) */
const registeredEmails = new Set(['test@example.com', 'admin@example.com']);

/** Mock 회원 정보 */
const mockUsers: Record<string, { name: string; password: string; role: string }> = {
  'test@example.com': { name: '테스트', password: 'password1', role: 'ROLE_USER' },
  'admin@example.com': { name: '관리자', password: 'admin1234', role: 'ROLE_ADMIN' },
};

const makeFakeToken = (email: string, role: string = 'ROLE_USER') =>
  btoa(JSON.stringify({ alg: 'HS256' })) + '.' + btoa(JSON.stringify({ sub: email, role })) + '.signature';

export const authHandlers = [
  http.post(`${BASE}/auth/register`, async ({ request }) => {
    const { email, name, password } = (await request.json()) as {
      email: string;
      name: string;
      password: string;
    };

    if (registeredEmails.has(email)) {
      return HttpResponse.json(err('803', '이미 사용 중인 이메일입니다.', 'POST', '/auth/register'), { status: 409 });
    }

    registeredEmails.add(email);
    mockUsers[email] = { name, password, role: 'ROLE_USER' };

    return HttpResponse.json(ok(null));
  }),

  http.get(`${BASE}/auth/verify-email`, ({ request }) => {
    const url = new URL(request.url);
    const token = url.searchParams.get('token') ?? '';

    // mock-token-{email} 형태로 이메일 추출
    const email = token.startsWith('mock-token-') ? token.replace('mock-token-', '') : null;

    if (!email || !registeredEmails.has(email)) {
      return HttpResponse.json(err('801', '유효하지 않거나 만료된 인증 토큰입니다.', 'GET', '/auth/verify-email'), { status: 400 });
    }

    return HttpResponse.json(ok({ accessToken: makeFakeToken(email, 'ROLE_USER'), refreshToken: makeFakeToken(email) }));
  }),

  http.post(`${BASE}/auth/resend-verification`, async ({ request }) => {
    const { email } = (await request.json()) as { email: string };

    if (!registeredEmails.has(email)) {
      return HttpResponse.json(err('804', '존재하지 않는 이메일입니다.', 'POST', '/auth/resend-verification'), { status: 404 });
    }

    return HttpResponse.json(ok(null));
  }),

  http.post(`${BASE}/auth/login`, async ({ request }) => {
    const { email, password } = (await request.json()) as {
      email: string;
      password: string;
    };

    const user = mockUsers[email];
    if (!user || user.password !== password) {
      return HttpResponse.json(err('805', '이메일 또는 비밀번호가 올바르지 않습니다.', 'POST', '/auth/login'), { status: 401 });
    }

    return HttpResponse.json(ok({ accessToken: makeFakeToken(email, user.role), refreshToken: makeFakeToken(email) }));
  }),

  http.post(`${BASE}/auth/logout`, () => HttpResponse.json(ok(null))),

  http.post(`${BASE}/auth/password/reset-request`, async ({ request }) => {
    const { email } = (await request.json()) as { email: string };

    if (!registeredEmails.has(email)) {
      return HttpResponse.json(err('806', '존재하지 않는 이메일입니다.', 'POST', '/auth/password/reset-request'), { status: 404 });
    }

    return HttpResponse.json(ok(null));
  }),

  http.post(`${BASE}/auth/password/reset`, async ({ request }) => {
    const { token, newPassword } = (await request.json()) as { token: string; newPassword: string };

    if (!token || token === 'invalid-token') {
      return HttpResponse.json(err('807', '유효하지 않거나 만료된 토큰입니다.', 'POST', '/auth/password/reset'), { status: 400 });
    }

    if (newPassword.length < 8) {
      return HttpResponse.json(err('808', '비밀번호는 8자 이상이어야 합니다.', 'POST', '/auth/password/reset'), { status: 400 });
    }

    return HttpResponse.json(ok(null));
  }),
];
