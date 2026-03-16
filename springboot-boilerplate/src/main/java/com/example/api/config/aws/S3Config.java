package com.example.api.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * AWS S3 설정
 */
@Configuration
public class S3Config {

  @Value("${aws.profile}")
  private String profile;

  @Value("${aws.s3.region}")
  private String region;

  /**
   * S3 클라이언트 빈 생성
   *
   * @return S3Client
   */
  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  /**
   * S3 Presigned URL 생성기 빈 생성
   *
   * @return S3Presigner
   */
  @Bean
  public S3Presigner s3Presigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }
}
