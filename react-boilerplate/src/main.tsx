import { env } from '@/app/config/env';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.tsx';
import QueryProvider from './app/providers/QueryProvider.tsx';
import ErrorBoundary from './shared/components/common/ErrorBoundary';

const enableMocking = async () => {
  if (env.VITE_ENABLE_MSW) {
    const { worker } = await import('./mocks/browser.ts');
    return worker.start({ onUnhandledRequest: 'bypass' });
  }
};

enableMocking().then(() => {
  createRoot(document.getElementById('root')!).render(
    <StrictMode>
      <ErrorBoundary>
        <QueryProvider>
          <App />
        </QueryProvider>
      </ErrorBoundary>
    </StrictMode>,
  );
});
