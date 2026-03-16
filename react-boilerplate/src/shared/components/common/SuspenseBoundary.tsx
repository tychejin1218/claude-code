import { Suspense, type ReactNode } from 'react';
import { ErrorBoundary as ReactErrorBoundary } from 'react-error-boundary';
import type { FallbackProps } from 'react-error-boundary';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import LoadingSpinner from '@/shared/components/ui/LoadingSpinner';

const DefaultErrorFallback = ({ error, resetErrorBoundary }: FallbackProps) => (
  <div className="flex min-h-50 flex-col items-center justify-center gap-4">
    <p className="text-gray-500">{error instanceof Error ? error.message : '데이터를 불러오지 못했습니다'}</p>
    <button className="rounded bg-blue-600 px-4 py-2 text-sm text-white" onClick={resetErrorBoundary}>
      다시 시도
    </button>
  </div>
);

interface SuspenseBoundaryProps {
  children: ReactNode;
  pendingFallback?: ReactNode;
  errorFallback?: React.ComponentType<FallbackProps>;
}

const SuspenseBoundary = ({ children, pendingFallback, errorFallback: ErrorFallbackComponent = DefaultErrorFallback }: SuspenseBoundaryProps) => (
  <QueryErrorResetBoundary>
    {({ reset }) => (
      <ReactErrorBoundary onReset={reset} FallbackComponent={ErrorFallbackComponent}>
        <Suspense fallback={pendingFallback ?? <LoadingSpinner />}>{children}</Suspense>
      </ReactErrorBoundary>
    )}
  </QueryErrorResetBoundary>
);

export default SuspenseBoundary;
