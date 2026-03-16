package com.example.api.user.dto;

import com.example.api.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원 DTO
 */
public class UserDto {

  /**
   * 내 정보 응답
   */
  @Getter
  @Builder
  @Schema(description = "내 정보 응답")
  public static class MeResponse {

    @Schema(description = "회원 ID")
    private Long id;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일")
    private String email;

    /**
     * Entity → DTO 변환
     *
     * @param member 회원 엔티티
     * @return 내 정보 응답
     */
    public static MeResponse from(Member member) {
      return MeResponse.builder()
          .id(member.getId())
          .name(member.getName())
          .email(member.getEmail())
          .build();
    }
  }
}
