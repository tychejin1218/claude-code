package com.example.api.notification.event;

/**
 * 할 일 완료 이벤트
 *
 * <p>트랜잭션 커밋 후 알림 발행을 위해 {@link org.springframework.context.ApplicationEventPublisher}로
 * 게시됩니다.
 *
 * @param email     완료 처리한 회원 이메일
 * @param todoId    완료된 할 일 ID
 * @param todoTitle 완료된 할 일 제목
 */
public record TodoCompleteEvent(String email, Long todoId, String todoTitle) {

}
