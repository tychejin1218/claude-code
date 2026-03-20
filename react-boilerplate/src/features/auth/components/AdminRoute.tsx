import { Navigate, Outlet } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';

const AdminRoute = () => {
  const user = useUserStore((state) => state.user);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (user.role !== 'ROLE_ADMIN') {
    return <Navigate to="/home" replace />;
  }

  return <Outlet />;
};

export default AdminRoute;
