import { useCallback } from 'react';
import { useToastStore } from '@/app/stores/useToastStore';

const DEFAULT_DURATION = 3000;

/**
 * Toast 편의 훅
 * - success / error / info / warning: non-blocking 알림
 * - 기본 duration: 3000ms
 */
export const useToast = () => {
  const add = useToastStore((s) => s.add);

  const success = useCallback((message: string, duration?: number) => add({ type: 'success', message, duration: duration ?? DEFAULT_DURATION }), [add]);
  const error = useCallback((message: string, duration?: number) => add({ type: 'error', message, duration: duration ?? DEFAULT_DURATION }), [add]);
  const info = useCallback((message: string, duration?: number) => add({ type: 'info', message, duration: duration ?? DEFAULT_DURATION }), [add]);
  const warning = useCallback((message: string, duration?: number) => add({ type: 'warning', message, duration: duration ?? DEFAULT_DURATION }), [add]);

  return { success, error, info, warning };
};
