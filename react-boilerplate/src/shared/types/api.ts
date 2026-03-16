/** API 성공 응답 */
export interface ApiResponse<T> {
  statusCode: string;
  message: string;
  data: T;
}

/** API 실패 응답 */
export interface ErrorResponse {
  statusCode: string;
  message: string;
  method: string;
  path: string;
  timestamp: string;
}
