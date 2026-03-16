package com.example.api.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.api.config.JasyptConfig;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Jasypt 암호화/복호화 유틸리티
 *
 * <p>사용법:
 * <ol>
 *   <li>{@code @TestPropertySource} 의 비밀번호를 사용할 Jasypt 비밀번호로 변경
 *   <li>암호화: {@code VALUE_TO_ENCRYPT} 에 평문을 입력하고 {@code encrypt()} 실행
 *   <li>DEBUG 로그에서 결과를 확인하여 yml 파일에 {@code ENC(...)} 형태로 적용
 * </ol>
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JasyptConfig.class)
@TestPropertySource(properties = "jasypt.encryptor.password=local-default")
@DisplayName("Jasypt 암호화/복호화 유틸리티")
class JasyptEncryptUtilTest {

  /** 암호화할 평문 */
  private static final String VALUE_TO_ENCRYPT = "암호화할-값을-여기에-입력";

  @Autowired
  private StringEncryptor stringEncryptor;

  @Test
  @DisplayName("[유틸] 암호화 - 결과를 ENC(...)에 감싸 yml에 적용")
  void encrypt() {
    String result = stringEncryptor.encrypt(VALUE_TO_ENCRYPT);
    log.debug("ENC({})", result);
  }

  @Test
  @DisplayName("[검증] 암호화 → 복호화 라운드트립 - 원문 일치 확인")
  void encryptDecrypt_roundTrip() {
    // given
    String plainText = "password1!";

    // when
    String encryptedText = stringEncryptor.encrypt(plainText);
    String decryptedText = stringEncryptor.decrypt(encryptedText);

    log.debug("Encrypted Text : [{}]", encryptedText);
    log.debug("Decrypted Text : [{}]", decryptedText);

    // then
    assertAll(
        () -> assertNotNull(encryptedText, "암호화 결과는 null이 아니어야 한다"),
        () -> assertNotNull(decryptedText, "복호화 결과는 null이 아니어야 한다"),
        () -> assertEquals(plainText, decryptedText, "복호화 결과가 원문과 일치해야 한다")
    );
  }
}
