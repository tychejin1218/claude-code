import { Navigate, Outlet } from 'react-router-dom';
import { useUserStore } from '@/app/stores/useUserStore';
import type { MemberRole } from '@/features/auth/types/user';

interface Props {
  requiredRole?: MemberRole;
  roleRedirectTo?: string;
}

const RouteGuard = ({ requiredRole, roleRedirectTo = '/home' }: Props) => {
  const user = useUserStore((state) => state.user);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to={roleRedirectTo} replace />;
  }

  return <Outlet />;
};

export default RouteGuard;
