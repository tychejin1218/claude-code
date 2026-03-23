package com.example.api.notification.listener;

import com.example.api.notification.dto.NotificationDto;
import com.example.api.notification.event.TodoCompleteEvent;
import com.example.api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 알림 이벤트 리스너
 *
 * <p>트랜잭션 커밋 이후에 알림을 발행합니다.
 * DB 커밋 전에 알림을 보내면 롤백 시 잘못된 알림이 발행될 수 있으므로 {@link TransactionPhase#AFTER_COMMIT}을 사용합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;

  /**
   * 할 일 완료 이벤트 처리
   *
   * @param event 할 일 완료 이벤트
   */
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onTodoComplete(TodoCompleteEvent event) {
    notificationService.publish(
        event.email(),
        NotificationDto.TodoCompleted.of(event.todoId(), event.todoTitle())
    );
  }
}
