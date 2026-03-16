import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import type { ReactNode } from 'react';

export interface DialogItem {
  id: string;
  type: 'alert' | 'confirm' | 'custom';
  title?: string;
  content: ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
  resolve?: (value: boolean) => void;
}

interface DialogState {
  dialogs: DialogItem[];
  open: (dialog: DialogItem) => void;
  close: (id: string, result?: boolean) => void;
}

export const useDialogStore = create<DialogState>()(
  devtools(
    (set, get) => ({
      dialogs: [],
      open: (dialog) => set({ dialogs: [...get().dialogs, dialog] }, false, 'dialog/open'),
      close: (id, result = false) => {
        const dialog = get().dialogs.find((d) => d.id === id);
        dialog?.resolve?.(result);
        set({ dialogs: get().dialogs.filter((d) => d.id !== id) }, false, 'dialog/close');
      },
    }),
    { name: 'DialogStore' },
  ),
);
