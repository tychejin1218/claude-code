const AuthErrorPage = () => {
  const params = new URLSearchParams(window.location.search);
  const message = params.get('message') ?? '알 수 없는 오류가 발생했습니다.';

  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4">
      <p className="text-xl font-bold text-red-600">인증 실패</p>
      <p className="text-sm text-gray-600">{decodeURIComponent(message)}</p>
    </div>
  );
};

export default AuthErrorPage;
