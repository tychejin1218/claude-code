import { http, HttpResponse } from 'msw';
import { ok } from '@/mocks/response';
import { env } from '@/app/config/env';

const BASE = env.VITE_API_BASE_URL;

export const fileHandlers = [
  http.get(`${BASE}/files/presigned-url`, () => {
    const mockMinioUrl = 'http://localhost:9000/boilerplate-bucket/mock-uuid/image.png';
    return HttpResponse.json(
      ok({
        presignedUrl: `${mockMinioUrl}?X-Amz-Algorithm=mock`,
        objectUrl: 'https://placehold.co/400x300/e2e8f0/94a3b8?text=Image',
      }),
    );
  }),

  // MinIO 업로드 요청 인터셉트 (MSW 모드에서 실제 MinIO 없이 동작)
  http.put('http://localhost:9000/*', () => new HttpResponse(null, { status: 200 })),
];
