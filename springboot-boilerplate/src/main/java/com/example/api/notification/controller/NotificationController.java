package com.example.api.notification.controller;

import com.example.api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 실시간 알림 컨트롤러
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * SSE 구독
   *
   * <p>클라이언트는 이 엔드포인트에 연결하여 실시간 알림을 수신합니다.
   *
   * @param authentication 인증 정보
   * @return SseEmitter
   */
  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(Authentication authentication) {
    return notificationService.subscribe(authentication.getName());
  }
}
