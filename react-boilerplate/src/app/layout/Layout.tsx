import { Outlet, useNavigate } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';

const Layout = () => {
  const user = useUserStore((state) => state.user);
  const clearUser = useUserStore((state) => state.clearUser);
  const navigate = useNavigate();

  const onClickLogout = () => {
    clearUser();
    navigate('/');
  };

  return (
    <div className="min-h-screen">
      <header className="border-b border-gray-200 bg-white">
        <nav className="flex items-center justify-between px-6 py-3">
          <h1 className="text-xl font-bold">React Boilerplate</h1>
          {user && (
            <div className="flex items-center gap-4">
              <span className="text-sm text-gray-600">
                {user.name} ({user.role})
              </span>
              <button className="rounded bg-gray-200 px-3 py-1 text-sm text-gray-700 hover:bg-gray-300" onClick={onClickLogout}>
                로그아웃
              </button>
            </div>
          )}
        </nav>
      </header>
      <main className="p-4">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;
