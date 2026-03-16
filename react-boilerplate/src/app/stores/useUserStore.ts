import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import type { User } from '@/features/auth/types/user';

interface UserState {
  user: User | null;
  accessToken: string | null;
  setUser: (user: User) => void;
  setAccessToken: (token: string) => void;
  clearUser: () => void;
}

export const useUserStore = create<UserState>()(
  devtools(
    (set) => ({
      user: null,
      accessToken: null,
      setUser: (user) => set({ user }, false, 'setUser'),
      setAccessToken: (accessToken) => set({ accessToken }, false, 'setAccessToken'),
      clearUser: () => set({ user: null, accessToken: null }, false, 'clearUser'),
    }),
    { name: 'UserStore' },
  ),
);
