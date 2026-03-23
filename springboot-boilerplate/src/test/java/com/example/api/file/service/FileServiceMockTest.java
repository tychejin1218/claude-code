package com.example.api.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.api.common.exception.ApiException;
import com.example.api.file.dto.FileDto;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * 파일 서비스 Mock 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("파일 서비스 Mock 테스트")
class FileServiceMockTest {

  @InjectMocks
  private FileService fileService;

  @Mock
  private S3Presigner s3Presigner;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(fileService, "bucketName", "test-bucket");
    ReflectionTestUtils.setField(fileService, "expiryMinutes", 5);
  }

  @Test
  @Order(1)
  @DisplayName("Presigned URL 발급 - 성공")
  void getPresignedUrl_success() throws Exception {
    // given
    String objectUrlStr = "http://localhost:9000/test-bucket/uuid/image.png";
    String presignedUrlStr = objectUrlStr + "?X-Amz-Algorithm=mock";
    PresignedPutObjectRequest presigned = mock(PresignedPutObjectRequest.class);
    given(presigned.url()).willReturn(URI.create(presignedUrlStr).toURL());
    given(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).willReturn(presigned);

    // when
    FileDto.PresignedUrlResponse result = fileService.getPresignedUrl("image.png", "image/png");

    // then
    assertThat(result.getPresignedUrl()).isEqualTo(presignedUrlStr);
    assertThat(result.getObjectUrl()).isEqualTo(objectUrlStr);
    verify(s3Presigner).presignPutObject(any(PutObjectPresignRequest.class));
  }

  @Test
  @Order(2)
  @DisplayName("Presigned URL 발급 - 허용되지 않는 contentType 예외")
  void getPresignedUrl_invalidContentType() {
    // when & then
    assertThatThrownBy(
        () -> fileService.getPresignedUrl("malware.exe", "application/x-msdownload"))
        .isInstanceOf(ApiException.class);
  }

  @Test
  @Order(3)
  @DisplayName("Presigned URL 발급 - 허용된 모든 이미지 타입 처리")
  void getPresignedUrl_allAllowedImageTypes() throws Exception {
    // given
    String[] allowedTypes = {"image/jpeg", "image/png", "image/gif", "image/webp"};
    PresignedPutObjectRequest presigned = mock(PresignedPutObjectRequest.class);
    given(presigned.url()).willReturn(
        URI.create("http://localhost:9000/test-bucket/uuid/file?sig=x").toURL());
    given(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).willReturn(presigned);

    // when & then
    for (String contentType : allowedTypes) {
      FileDto.PresignedUrlResponse result = fileService.getPresignedUrl("file", contentType);
      assertThat(result.getPresignedUrl()).isNotBlank();
    }
  }

  @Test
  @Order(4)
  @DisplayName("Presigned URL 발급 - S3 오류 시 예외 전파")
  void getPresignedUrl_s3Error() {
    // given
    given(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
        .willThrow(new RuntimeException("S3 연결 실패"));

    // when & then
    assertThatThrownBy(() -> fileService.getPresignedUrl("image.png", "image/png"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("S3 연결 실패");
  }
}
