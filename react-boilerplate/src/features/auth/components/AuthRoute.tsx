import { Navigate, Outlet } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';

const AuthRoute = () => {
  const user = useUserStore((state) => state.user);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default AuthRoute;
