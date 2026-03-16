package com.example.api.sample.dto;

import com.example.api.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * 샘플 모듈 DTO 모음
 */
public class SampleDto {

  /**
   * 회원 단건 조회 요청 DTO
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Alias("MemberRequest")
  public static class MemberRequest {

    private Long id;
    private String name;
    private String email;

    /**
     * ID 기반 단건 조회 요청 생성
     *
     * @param id 회원 ID
     * @return MemberRequest
     */
    public static MemberRequest of(long id) {
      return MemberRequest.builder().id(id).build();
    }

    /**
     * 이름·이메일 기반 목록 조회 요청 생성
     *
     * @param name  이름
     * @param email 이메일
     * @return MemberRequest
     */
    public static MemberRequest of(String name, String email) {
      return MemberRequest.builder().name(name).email(email).build();
    }
  }

  /**
   * 회원 목록 조회 요청 DTO
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Alias("MemberListRequest")
  public static class MemberListRequest {

    private String name;
    private String email;

    /**
     * 이름·이메일 기반 목록 조회 요청 생성
     *
     * @param name  이름
     * @param email 이메일
     * @return MemberListRequest
     */
    public static MemberListRequest of(String name, String email) {
      return MemberListRequest.builder().name(name).email(email).build();
    }
  }

  /**
   * 회원 조회 응답 DTO
   */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Alias("MemberResponse")
  @Schema(description = "회원 조회 응답")
  public static class MemberResponse {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    /**
     * Member 엔티티로부터 응답 DTO 생성
     *
     * @param member 회원 엔티티
     * @return 회원 응답 DTO
     */
    public static MemberResponse from(Member member) {
      return MemberResponse.builder()
          .id(member.getId())
          .name(member.getName())
          .email(member.getEmail())
          .build();
    }
  }
}
