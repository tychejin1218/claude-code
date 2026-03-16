import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4">
      <h1 className="text-6xl font-bold">404</h1>
      <p className="text-gray-500">페이지를 찾을 수 없습니다.</p>
      <button className="rounded bg-blue-600 px-4 py-2 text-white" onClick={() => navigate('/')}>
        홈으로 이동
      </button>
    </div>
  );
};

export default NotFoundPage;
