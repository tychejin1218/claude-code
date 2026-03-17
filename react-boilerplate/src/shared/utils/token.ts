/** JWT payload에서 sub(email) 추출 */
export const parseEmailFromToken = (token: string): string => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return (payload as { sub?: string }).sub ?? '';
  } catch {
    return '';
  }
};
