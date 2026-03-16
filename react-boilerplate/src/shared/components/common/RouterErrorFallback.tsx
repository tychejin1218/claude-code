import { useRouteError } from 'react-router-dom';
import { ErrorFallback } from './ErrorBoundary';

const RouterErrorFallback = () => {
  const error = useRouteError();
  return <ErrorFallback error={error} resetErrorBoundary={() => window.location.replace('/')} />;
};

export default RouterErrorFallback;
