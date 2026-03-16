/** 공통 에러 메시지 */
export const ERROR_MESSAGES = {
  UNAUTHORIZED: '인증이 만료되었습니다. 다시 로그인해주세요.',
  FORBIDDEN: '접근 권한이 없습니다.',
  SERVER_ERROR: '서버 오류가 발생했습니다.',
  NETWORK_ERROR: '네트워크 연결을 확인해주세요.',
} as const;
