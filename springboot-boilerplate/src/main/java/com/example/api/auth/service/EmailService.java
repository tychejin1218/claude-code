package com.example.api.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 이메일 발송 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${mail.from}")
  private String from;

  @Value("${mail.verify-url}")
  private String verifyUrl;

  @Value("${mail.password-reset-url}")
  private String passwordResetUrl;

  /**
   * 이메일 인증 메일 발송 (비동기)
   *
   * @param email 수신자 이메일
   * @param token 인증 토큰
   */
  @Async
  public void sendVerificationEmail(String email, String token) {
    try {
      var message = mailSender.createMimeMessage();
      var helper = new MimeMessageHelper(message, false, "UTF-8");
      helper.setFrom(from);
      helper.setTo(email);
      helper.setSubject("[Boilerplate] 이메일 인증을 완료해주세요");
      helper.setText(buildVerificationEmailBody(token), true);
      mailSender.send(message);
      log.debug("인증 메일 발송 완료: {}", email);
    } catch (Exception e) {
      log.error("인증 메일 발송 실패 (email={}): {}", email, e.getMessage());
    }
  }

  /**
   * 비밀번호 재설정 메일 발송 (비동기)
   *
   * @param email 수신자 이메일
   * @param token 재설정 토큰
   */
  @Async
  public void sendPasswordResetEmail(String email, String token) {
    try {
      var message = mailSender.createMimeMessage();
      var helper = new MimeMessageHelper(message, false, "UTF-8");
      helper.setFrom(from);
      helper.setTo(email);
      helper.setSubject("[Boilerplate] 비밀번호를 재설정해주세요");
      helper.setText(buildPasswordResetEmailBody(token), true);
      mailSender.send(message);
      log.debug("비밀번호 재설정 메일 발송 완료: {}", email);
    } catch (Exception e) {
      log.error("비밀번호 재설정 메일 발송 실패 (email={}): {}", email, e.getMessage());
    }
  }

  private String buildPasswordResetEmailBody(String token) {
    String link = passwordResetUrl + "?token=" + token;
    return String.format("""
        <p>아래 링크를 클릭하여 비밀번호를 재설정해주세요.</p>
        <p><a href="%s">비밀번호 재설정하기</a></p>
        <p>링크는 30분 동안 유효합니다.</p>
        """, link);
  }

  private String buildVerificationEmailBody(String token) {
    String link = verifyUrl + "?token=" + token;
    return String.format("""
        <p>아래 링크를 클릭하여 이메일 인증을 완료해주세요.</p>
        <p><a href="%s">이메일 인증하기</a></p>
        <p>링크는 24시간 동안 유효합니다.</p>
        """, link);
  }
}
