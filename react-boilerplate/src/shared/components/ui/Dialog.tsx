import { useCallback, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { useDialogStore } from '@/app/stores/useDialogStore';
import type { DialogItem } from '@/app/stores/useDialogStore';

/**
 * 개별 다이얼로그 렌더러
 * - ESC 키로 닫기, backdrop 클릭으로 닫기 지원
 */
const DialogElement = ({ dialog }: { dialog: DialogItem }) => {
  const close = useDialogStore((s) => s.close);

  const handleClose = useCallback(
    (result: boolean) => {
      close(dialog.id, result);
    },
    [close, dialog.id],
  );

  // ESC 키 닫기
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') handleClose(false);
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [dialog.id, handleClose]);

  // 스크롤 방지
  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = '';
    };
  }, []);

  return createPortal(
    // 프로젝트별 디자인 마크업에 맞게 작성
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* backdrop */}
      <div className="fixed inset-0 bg-black/40" onClick={() => handleClose(false)} />

      {/* dialog */}
      <div role="dialog" className="relative z-10 min-w-80 rounded-lg bg-white p-6 shadow-xl">
        {dialog.title && <h2 className="mb-2 text-lg font-semibold text-black">{dialog.title}</h2>}
        <div className="text-sm text-gray-700">{dialog.content}</div>

        {/* custom 타입은 content에서 직접 닫기 처리 */}
        {dialog.type !== 'custom' && (
          <div className="mt-6 flex justify-end gap-2">
            {dialog.type === 'confirm' && (
              <button className="rounded px-4 py-2 text-sm text-gray-600 hover:bg-gray-100" onClick={() => handleClose(false)}>
                {dialog.cancelLabel ?? '취소'}
              </button>
            )}
            <button className="rounded bg-blue-600 px-4 py-2 text-sm text-white hover:bg-blue-700" onClick={() => handleClose(true)}>
              {dialog.confirmLabel ?? '확인'}
            </button>
          </div>
        )}
      </div>
    </div>,
    document.body,
  );
};

/**
 * 다이얼로그 렌더러 — App에 한 번만 배치
 * 여러 다이얼로그가 동시에 열릴 수 있도록 스택 구조
 */
const DialogRenderer = () => {
  const dialogs = useDialogStore((s) => s.dialogs);
  return dialogs.map((dialog) => <DialogElement key={dialog.id} dialog={dialog} />);
};

export default DialogRenderer;
