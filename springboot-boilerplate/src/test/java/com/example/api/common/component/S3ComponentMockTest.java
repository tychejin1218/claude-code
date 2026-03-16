package com.example.api.common.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * S3 컴포넌트 Mock 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("S3 컴포넌트 Mock 테스트")
class S3ComponentMockTest {

  @InjectMocks
  private S3Component s3Component;

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(s3Component, "bucketName", "test-bucket");
    ReflectionTestUtils.setField(s3Component, "presignedUrlExpiryMinutes", 5L);
  }

  @Test
  @Order(1)
  @DisplayName("파일 업로드 - 성공")
  void uploadFile_success() {
    // given
    String key = "exam-pdfs/1/exam-1.pdf";
    byte[] content = "pdf content".getBytes();

    // when
    s3Component.uploadFile(key, content, "application/pdf");

    // then
    verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  @Order(2)
  @DisplayName("파일 업로드 - S3 오류 시 예외 전파")
  void uploadFile_failure() {
    // given
    given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willThrow(new RuntimeException("S3 연결 실패"));

    // when & then
    assertThatThrownBy(() -> s3Component.uploadFile("key", new byte[0], "application/pdf"))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @Order(3)
  @DisplayName("파일 삭제 - 성공")
  void deleteFile_success() {
    // given
    String key = "exam-pdfs/1/exam-1.pdf";

    // when
    s3Component.deleteFile(key);

    // then
    verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
  }

  @Test
  @Order(4)
  @DisplayName("파일 삭제 - S3 오류 시 예외 전파")
  void deleteFile_failure() {
    // given
    given(s3Client.deleteObject(any(DeleteObjectRequest.class)))
        .willThrow(new RuntimeException("S3 연결 실패"));

    // when & then
    assertThatThrownBy(() -> s3Component.deleteFile("key"))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @Order(5)
  @DisplayName("Presigned URL 발급 - 성공")
  void getPresignedDownloadUrl_success() throws Exception {
    // given
    String key = "exam-pdfs/1/exam-1.pdf";
    String expectedUrl = "https://test-bucket.s3.ap-northeast-2.amazonaws.com/" + key;
    PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
    given(presignedRequest.url()).willReturn(URI.create(expectedUrl).toURL());
    given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .willReturn(presignedRequest);

    // when
    String url = s3Component.getPresignedDownloadUrl(key);

    // then
    assertThat(url).isEqualTo(expectedUrl);
    verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
  }

  @Test
  @Order(6)
  @DisplayName("Presigned URL 발급 - S3 오류 시 예외 전파")
  void getPresignedDownloadUrl_failure() {
    // given
    given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .willThrow(new RuntimeException("S3 연결 실패"));

    // when & then
    assertThatThrownBy(() -> s3Component.getPresignedDownloadUrl("key"))
        .isInstanceOf(RuntimeException.class);
  }
}
