package com.example.api.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Jasypt 암호화/복호화 단위 테스트
 *
 * <p>Spring Context 없이 JasyptConfig와 동일한 설정으로 암호화/복호화 검증
 * (신규 ENC() 값 생성 시 {@code ./gradlew jasyptEncrypt --args="<비밀번호> <평문>"} 사용)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Jasypt 암호화/복호화 테스트")
class JasyptEncryptorTest {

  private static final String PASSWORD = "local-default";
  private static final String PLAIN_TEXT =
      "boilerplate-jwt-secret-key-must-be-at-least-256-bits-long-for-hs256";

  private PooledPBEStringEncryptor encryptor;

  @BeforeEach
  void setUp() {
    encryptor = createEncryptor(PASSWORD);
  }

  @Test
  @Order(1)
  @DisplayName("암호화 - 평문과 다른 값이 생성됨")
  void encrypt_producesValueDifferentFromPlaintext() {
    String encrypted = encryptor.encrypt(PLAIN_TEXT);

    assertThat(encrypted).isNotEqualTo(PLAIN_TEXT);
  }

  @Test
  @Order(2)
  @DisplayName("암호화 - 매번 다른 암호문 생성 (랜덤 Salt)")
  void encrypt_producesDifferentCiphertextEachTime() {
    String encrypted1 = encryptor.encrypt(PLAIN_TEXT);
    String encrypted2 = encryptor.encrypt(PLAIN_TEXT);

    assertThat(encrypted1).isNotEqualTo(encrypted2);
  }

  @Test
  @Order(3)
  @DisplayName("암호화 → 복호화 라운드트립 - 원문 복원")
  void encryptThenDecrypt_returnsOriginal() {
    String encrypted = encryptor.encrypt(PLAIN_TEXT);
    String decrypted = encryptor.decrypt(encrypted);

    assertThat(decrypted).isEqualTo(PLAIN_TEXT);
  }

  @Test
  @Order(4)
  @DisplayName("다른 비밀번호로 복호화 - 예외 발생")
  void decrypt_withWrongPassword_throwsException() {
    PooledPBEStringEncryptor wrongEncryptor = createEncryptor("wrong-password");
    String encrypted = encryptor.encrypt(PLAIN_TEXT);

    assertThatThrownBy(() -> wrongEncryptor.decrypt(encrypted))
        .isInstanceOf(EncryptionOperationNotPossibleException.class);
  }

  @Test
  @Order(5)
  @DisplayName("손상된 암호문 복호화 - 예외 발생")
  void decrypt_withCorruptedCiphertext_throwsException() {
    assertThatThrownBy(() -> encryptor.decrypt("corrupted-ciphertext-value"))
        .isInstanceOf(EncryptionOperationNotPossibleException.class);
  }

  private PooledPBEStringEncryptor createEncryptor(String password) {
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(password);
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    PooledPBEStringEncryptor enc = new PooledPBEStringEncryptor();
    enc.setConfig(config);
    return enc;
  }
}
