import { lazy, type ComponentType } from 'react';
import { createBrowserRouter } from 'react-router-dom';

import Layout from '@/app/layout/Layout';
import RouteGuard from '@/features/auth/components/RouteGuard';
import RouterErrorFallback from '@/shared/components/common/RouterErrorFallback';
import SuspenseBoundary from '@/shared/components/common/SuspenseBoundary';

// 페이지 컴포넌트 lazy loading
const LoginPage = lazy(() => import('@/features/auth/pages/LoginPage'));
const RegisterPage = lazy(() => import('@/features/auth/pages/RegisterPage'));
const AuthErrorPage = lazy(() => import('@/features/auth/pages/AuthErrorPage'));
const TodoPage = lazy(() => import('@/features/todo/pages/TodoPage'));
const AdminPage = lazy(() => import('@/features/admin/pages/AdminPage'));
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
  {
    path: '/register',
    element: withSuspense(RegisterPage),
  },
  // 인증 실패 페이지 (/error?message= 리다이렉트)
  {
    path: '/error',
    element: withSuspense(AuthErrorPage),
  },
  // 인증 필요
  {
    element: <RouteGuard />,
    errorElement: <RouterErrorFallback />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: '/home',
            element: withSuspense(HomePage),
          },
          {
            path: '/todos',
            element: withSuspense(TodoPage),
          },
        ],
      },
    ],
  },
  // 관리자 전용
  {
    element: <RouteGuard requiredRole="ROLE_ADMIN" />,
    errorElement: <RouterErrorFallback />,
    children: [
      {
        element: <Layout />,
        children: [
          {
            path: '/admin',
            element: withSuspense(AdminPage),
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
