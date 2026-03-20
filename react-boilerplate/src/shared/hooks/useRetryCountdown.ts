import { useState, useEffect } from 'react';

/** 재시도 대기 카운트다운 훅 */
const useRetryCountdown = () => {
  const [retrySeconds, setRetrySeconds] = useState(0);

  useEffect(() => {
    if (retrySeconds <= 0) return;
    const timer = setTimeout(() => setRetrySeconds((s) => s - 1), 1000);
    return () => clearTimeout(timer);
  }, [retrySeconds]);

  return { retrySeconds, startCountdown: (seconds: number) => setRetrySeconds(seconds) };
};

export default useRetryCountdown;
