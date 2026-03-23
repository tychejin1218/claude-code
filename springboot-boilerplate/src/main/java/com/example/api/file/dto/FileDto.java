package com.example.api.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 DTO
 */
public class FileDto {

  /**
   * Presigned URL 응답
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Presigned URL 응답")
  public static class PresignedUrlResponse {

    @Schema(
        description = "파일 업로드용 Presigned URL (PUT 요청 사용)",
        example = "http://localhost:9000/boilerplate-bucket/uuid/image.png?X-Amz-Algorithm=..."
    )
    private String presignedUrl;

    @Schema(
        description = "업로드 완료 후 저장할 파일 URL",
        example = "http://localhost:9000/boilerplate-bucket/uuid/image.png"
    )
    private String objectUrl;

    /**
     * Presigned URL 응답 생성
     *
     * @param presignedUrl 업로드용 Presigned URL
     * @param objectUrl    파일 접근 URL
     * @return PresignedUrlResponse
     */
    public static PresignedUrlResponse of(String presignedUrl, String objectUrl) {
      return PresignedUrlResponse.builder()
          .presignedUrl(presignedUrl)
          .objectUrl(objectUrl)
          .build();
    }
  }
}
