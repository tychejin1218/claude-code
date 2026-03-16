import { lazy, type ComponentType } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import Layout from '@/app/layout/Layout';
import AuthRoute from '@/features/auth/components/AuthRoute';
import RouterErrorFallback from '@/shared/components/common/RouterErrorFallback';
import SuspenseBoundary from '@/shared/components/common/SuspenseBoundary';

// 페이지 컴포넌트 lazy loading
const LoginPage = lazy(() => import('@/features/auth/pages/LoginPage'));
const AuthErrorPage = lazy(() => import('@/features/auth/pages/AuthErrorPage'));
const HomePage = lazy(() => import('@/app/pages/HomePage'));
const NotFoundPage = lazy(() => import('@/app/pages/NotFoundPage'));

// SuspenseBoundary 래핑 헬퍼 (lazy loading + 데이터 페칭 로딩/에러 처리)
const withSuspense = (Component: ComponentType) => (
  <SuspenseBoundary>
    <Component />
  </SuspenseBoundary>
);

const router = createBrowserRouter([
  // 공개 라우트
  {
    path: '/',
    index: true,
    element: withSuspense(LoginPage),
  },
  // 인증 실패 페이지 (/error?message= 리다이렉트)
  {
    path: '/error',
    element: withSuspense(AuthErrorPage),
  },
  // 인증 필요
  {
    element: <AuthRoute />,
    errorElement: <RouterErrorFallback />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: '/home',
            element: withSuspense(HomePage),
          },
        ],
      },
    ],
  },
  {
    path: '*',
    element: withSuspense(NotFoundPage),
  },
]);

export default router;
