import { useEffect, useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';
import { getVerifyEmail, postResendVerification } from '@/features/auth/apis/authApi';
import type { MemberRole } from '@/features/auth/types/user';
import type { ErrorResponse } from '@/shared/types/api';
import { parseEmailFromToken, parseRoleFromToken } from '@/shared/utils/token';

const EmailVerifyPage = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const email = searchParams.get('email') ?? '';

  const [resendLoading, setResendLoading] = useState(false);
  const [resendMessage, setResendMessage] = useState('');
  const [error, setError] = useState('');

  const setUser = useUserStore((state) => state.setUser);
  const setAccessToken = useUserStore((state) => state.setAccessToken);
  const setRefreshToken = useUserStore((state) => state.setRefreshToken);
  const navigate = useNavigate();

  // 토큰이 있으면 자동으로 인증 처리
  useEffect(() => {
    if (!token) return;

    getVerifyEmail(token)
      .then((res) => {
        const { accessToken, refreshToken } = res.data;
        const parsedEmail = parseEmailFromToken(accessToken) ?? '';
        const role = parseRoleFromToken(accessToken) as MemberRole;

        setUser({ userId: parsedEmail, name: parsedEmail, email: parsedEmail, role });
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        void navigate('/todos');
      })
      .catch((err: ErrorResponse) => {
        setError(err?.message ?? '인증 링크가 유효하지 않거나 만료되었습니다.');
      });
  }, [token, navigate, setUser, setAccessToken, setRefreshToken]);

  const handleResend = async () => {
    if (!email) return;
    setResendLoading(true);
    setResendMessage('');
    setError('');

    try {
      await postResendVerification(email);
      setResendMessage('인증 메일을 재발송했습니다. 받은편지함을 확인해주세요.');
    } catch (err) {
      const apiErr = err as ErrorResponse;
      setError(apiErr?.message ?? '재발송에 실패했습니다.');
    } finally {
      setResendLoading(false);
    }
  };

  // 토큰 파라미터가 있으면 인증 처리 중 화면
  if (token) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50">
        <div className="w-full max-w-sm rounded-lg bg-white p-8 shadow text-center">
          {error ? (
            <>
              <p className="mb-4 text-red-500">{error}</p>
              <Link to="/" className="text-sm text-blue-600 hover:underline">
                로그인 페이지로 돌아가기
              </Link>
            </>
          ) : (
            <p className="text-gray-600">이메일 인증 중...</p>
          )}
        </div>
      </div>
    );
  }

  // 토큰 없이 진입 시 (회원가입 직후 안내 화면)
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50">
      <div className="w-full max-w-sm rounded-lg bg-white p-8 shadow text-center">
        <h1 className="mb-2 text-2xl font-bold text-gray-800">이메일 인증</h1>
        <p className="mb-1 text-sm text-gray-600">
          <span className="font-medium">{email}</span>으로 인증 메일을 발송했습니다.
        </p>
        <p className="mb-6 text-sm text-gray-500">받은편지함에서 링크를 클릭해 인증을 완료해주세요.</p>

        {resendMessage && <p className="mb-4 text-sm text-green-600">{resendMessage}</p>}
        {error && <p className="mb-4 text-sm text-red-500">{error}</p>}

        <button
          onClick={() => void handleResend()}
          disabled={resendLoading || !email}
          className="w-full rounded bg-blue-600 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
        >
          {resendLoading ? '발송 중...' : '인증 메일 재발송'}
        </button>

        <p className="mt-4 text-sm text-gray-500">
          <Link to="/" className="text-blue-600 hover:underline">
            로그인 페이지로 돌아가기
          </Link>
        </p>
      </div>
    </div>
  );
};

export default EmailVerifyPage;
