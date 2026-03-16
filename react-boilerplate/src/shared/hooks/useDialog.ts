import { useCallback } from 'react';
import { useDialogStore } from '@/app/stores/useDialogStore';
import type { ReactNode } from 'react';

interface DialogOptions {
  title?: string;
  confirmLabel?: string;
  cancelLabel?: string;
}

/**
 * Dialog 편의 훅
 * - alert: 확인 버튼만 있는 알림 (Promise<void>)
 * - confirm: 확인/취소 선택 (Promise<boolean>)
 * - openDialog: 커스텀 콘텐츠 다이얼로그 (id 반환)
 * - closeDialog: id로 다이얼로그 닫기
 */
export const useDialog = () => {
  const open = useDialogStore((s) => s.open);
  const close = useDialogStore((s) => s.close);

  const alert = useCallback(
    (content: ReactNode, options?: DialogOptions | string): Promise<void> => {
      const opts = typeof options === 'string' ? { title: options } : options;
      return new Promise((resolve) => {
        open({
          id: crypto.randomUUID(),
          type: 'alert',
          content,
          ...opts,
          resolve: () => resolve(),
        });
      });
    },
    [open],
  );

  const confirm = useCallback(
    (content: ReactNode, options?: DialogOptions | string): Promise<boolean> => {
      const opts = typeof options === 'string' ? { title: options } : options;
      return new Promise((resolve) => {
        open({
          id: crypto.randomUUID(),
          type: 'confirm',
          content,
          ...opts,
          resolve,
        });
      });
    },
    [open],
  );

  const openDialog = useCallback(
    (content: ReactNode, title?: string): string => {
      const id = crypto.randomUUID();
      open({ id, type: 'custom', title, content });
      return id;
    },
    [open],
  );

  const closeDialog = useCallback(
    (id: string) => {
      close(id);
    },
    [close],
  );

  return { alert, confirm, openDialog, closeDialog };
};
