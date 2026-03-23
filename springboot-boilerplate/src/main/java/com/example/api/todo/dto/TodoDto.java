package com.example.api.todo.dto;

import com.example.api.domain.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 할 일 DTO
 */
public class TodoDto {

  /**
   * 할 일 목록 조회 요청
   */
  @Getter
  @Setter
  @NoArgsConstructor
  @Schema(description = "할 일 목록 조회 요청")
  public static class TodoListRequest {

    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int size = 10;

    @Schema(description = "완료 상태 필터 (all/completed/incomplete)", example = "all")
    private String status = "all";

    @Schema(description = "정렬 기준 (id/title)", example = "id")
    private String sort = "id";
  }

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
   * 이미지 URL 업데이트 요청
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "이미지 URL 업데이트 요청")
  public static class UpdateImageRequest {

    @Schema(description = "업로드된 이미지 URL", example = "http://localhost:9000/boilerplate-bucket/uuid/image.png")
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;
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

    @Schema(description = "이미지 URL", example = "http://localhost:9000/boilerplate-bucket/uuid/image.png")
    private String imageUrl;

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
          .imageUrl(todo.getImageUrl())
          .build();
    }
  }
}
