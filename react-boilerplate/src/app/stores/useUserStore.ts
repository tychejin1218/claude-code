import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import type { User } from '@/features/auth/types/user';

interface UserState {
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  setUser: (user: User) => void;
  setAccessToken: (token: string) => void;
  setRefreshToken: (token: string) => void;
  clearUser: () => void;
}

export const useUserStore = create<UserState>()(
  devtools(
    persist(
      (set) => ({
        user: null,
        accessToken: null,
        refreshToken: null,
        setUser: (user) => set({ user }, false, 'setUser'),
        setAccessToken: (accessToken) => set({ accessToken }, false, 'setAccessToken'),
        setRefreshToken: (refreshToken) => set({ refreshToken }, false, 'setRefreshToken'),
        clearUser: () => set({ user: null, accessToken: null, refreshToken: null }, false, 'clearUser'),
      }),
      {
        name: 'user-store',
        partialize: (state) => ({
          user: state.user,
          accessToken: state.accessToken,
          refreshToken: state.refreshToken,
        }),
      },
    ),
    { name: 'UserStore' },
  ),
);
