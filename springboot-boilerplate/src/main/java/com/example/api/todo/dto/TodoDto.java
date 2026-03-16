package com.example.api.todo.dto;

import com.example.api.domain.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 할 일 DTO
 */
public class TodoDto {

  /**
   * 할 일 생성 요청
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "할 일 생성 요청")
  public static class CreateRequest {

    @Schema(description = "할 일 내용", example = "스프링 부트 공부하기")
    @NotBlank(message = "할 일 내용을 입력해주세요.")
    @Size(max = 200, message = "할 일 내용은 200자 이내로 입력해주세요.")
    private String title;
  }

  /**
   * 할 일 응답
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "할 일 응답")
  public static class TodoResponse {

    @Schema(description = "할 일 ID", example = "1")
    private Long id;

    @Schema(description = "할 일 내용", example = "스프링 부트 공부하기")
    private String title;

    @Schema(description = "완료 여부", example = "false")
    private boolean completed;

    /**
     * Entity → Response DTO 변환
     *
     * @param todo 할 일 엔티티
     * @return TodoResponse
     */
    public static TodoResponse from(Todo todo) {
      return TodoResponse.builder()
          .id(todo.getId())
          .title(todo.getTitle())
          .completed(todo.isCompleted())
          .build();
    }
  }
}
