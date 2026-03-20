package com.example.api.admin.dto;

import com.example.api.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자 DTO
 */
public class AdminDto {

  /**
   * 회원 응답 (관리자)
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "회원 응답 (관리자)")
  public static class MemberResponse {

    @Schema(description = "회원 ID")
    private Long id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "역할 (ROLE_USER, ROLE_ADMIN)")
    private String role;

    /**
     * 엔티티 → 응답 DTO 변환
     *
     * @param member 회원 엔티티
     * @return MemberResponse
     */
    public static MemberResponse from(Member member) {
      return MemberResponse.builder()
          .id(member.getId())
          .email(member.getEmail())
          .name(member.getName())
          .role(member.getRole().name())
          .build();
    }
  }
}
