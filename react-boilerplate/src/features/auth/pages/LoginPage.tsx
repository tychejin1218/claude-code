import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';

/** JWT payload에서 sub(email) 추출 */
const parseEmailFromToken = (token: string): string | null => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return (payload as { sub?: string }).sub ?? null;
  } catch {
    return null;
  }
};

const LoginPage = () => {
  const setUser = useUserStore((state) => state.setUser);
  const setAccessToken = useUserStore((state) => state.setAccessToken);
  const navigate = useNavigate();

  useEffect(() => {
    // redirect 처리: ?accessToken=xxx&refreshToken=yyy
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const refreshToken = params.get('refreshToken');

    if (accessToken && refreshToken) {
      const email = parseEmailFromToken(accessToken) ?? '';

      // TODO: /members/me API로 실제 사용자 정보(name, role 등) 조회 후 교체
      setUser({ userId: email, name: email, email, role: 'TEACHER' });
      setAccessToken(accessToken);

      // URL에서 토큰 제거 후 홈으로 이동
      window.history.replaceState({}, '', '/');
      void navigate('/home');
    }
  }, [navigate, setUser, setAccessToken]);

  const handleLogin = () => {
    setUser({ userId: '1', name: '테스트 사용자', email: 'test@test.com', role: 'TEACHER' });
    void navigate('/home');
  };

  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4">
      <h1 className="text-2xl font-bold">로그인</h1>
      <button className="rounded bg-blue-600 px-4 py-2 text-white" onClick={handleLogin}>
        테스트 로그인
      </button>
    </div>
  );
};

export default LoginPage;
