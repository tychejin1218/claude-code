package com.example.api.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 DTO
 */
public class NotificationDto {

  /**
   * 할 일 완료 알림 페이로드
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TodoCompleted {

    private Long todoId;
    private String todoTitle;
    private String message;

    /**
     * 할 일 완료 알림 생성
     *
     * @param todoId    완료된 할 일 ID
     * @param todoTitle 완료된 할 일 제목
     * @return TodoCompleted
     */
    public static TodoCompleted of(Long todoId, String todoTitle) {
      return TodoCompleted.builder()
          .todoId(todoId)
          .todoTitle(todoTitle)
          .message(String.format("\"%s\" 완료!", todoTitle))
          .build();
    }
  }
}
