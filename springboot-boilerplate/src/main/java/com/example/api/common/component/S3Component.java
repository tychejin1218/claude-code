package com.example.api.common.component;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * AWS S3 공통 컴포넌트
 *
 * <p>파일 업로드 / 삭제 / Presigned URL 발급
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Component {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  @Value("${aws.s3.presigned-url-expiry-minutes}")
  private long presignedUrlExpiryMinutes;

  /**
   * 파일 S3 업로드
   *
   * @param key         S3 객체 키 (경로 포함 파일명)
   * @param content     파일 바이트 배열
   * @param contentType MIME 타입 (예: application/pdf)
   */
  public void uploadFile(String key, byte[] content, String contentType) {
    try {
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(contentType)
          .build();
      s3Client.putObject(request, RequestBody.fromBytes(content));
      log.info("[S3] 업로드 완료 - key: {}", key);
    } catch (Exception e) {
      log.error("[S3] 업로드 실패 - key: {}, message: {}", key, e.getMessage(), e);
      throw e;
    }
  }

  /**
   * S3 파일 삭제
   *
   * @param key S3 객체 키
   */
  public void deleteFile(String key) {
    try {
      DeleteObjectRequest request = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();
      s3Client.deleteObject(request);
      log.info("[S3] 삭제 완료 - key: {}", key);
    } catch (Exception e) {
      log.error("[S3] 삭제 실패 - key: {}, message: {}", key, e.getMessage(), e);
      throw e;
    }
  }

  /**
   * S3 다운로드 Presigned URL 발급
   *
   * @param key S3 객체 키
   * @return 유효 시간이 설정된 Presigned URL 문자열
   */
  public String getPresignedDownloadUrl(String key) {
    try {
      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(presignedUrlExpiryMinutes))
          .getObjectRequest(r -> r.bucket(bucketName).key(key))
          .build();
      String url = s3Presigner.presignGetObject(presignRequest).url().toString();
      log.info("[S3] Presigned URL 발급 완료 - key: {}", key);
      return url;
    } catch (Exception e) {
      log.error("[S3] Presigned URL 발급 실패 - key: {}, message: {}", key, e.getMessage(), e);
      throw e;
    }
  }
}
