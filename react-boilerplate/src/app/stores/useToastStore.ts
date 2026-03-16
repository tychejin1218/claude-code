import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

export interface ToastItem {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
  duration?: number;
}

interface ToastState {
  toasts: ToastItem[];
  add: (toast: Omit<ToastItem, 'id'>) => string;
  remove: (id: string) => void;
}

export const useToastStore = create<ToastState>()(
  devtools(
    (set, get) => ({
      toasts: [],
      add: (toast) => {
        const id = crypto.randomUUID();
        set({ toasts: [...get().toasts, { ...toast, id }] }, false, 'toast/add');
        return id;
      },
      remove: (id) => set({ toasts: get().toasts.filter((t) => t.id !== id) }, false, 'toast/remove'),
    }),
    { name: 'ToastStore' },
  ),
);
