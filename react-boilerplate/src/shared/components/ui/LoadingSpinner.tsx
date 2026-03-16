// Suspense fallback용 로딩 스피너
const LoadingSpinner = () => (
  <div className="flex h-screen items-center justify-center">
    <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-200 border-t-gray-600" />
  </div>
);

export default LoadingSpinner;
