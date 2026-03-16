package com.example.api.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt 암호화 설정
 *
 * <p>PBEWITHHMACSHA512ANDAES_256 알고리즘으로 설정값 암호화.
 * 암호화 비밀번호는 {@code JASYPT_ENCRYPTOR_PASSWORD} 환경변수로 주입
 *
 * <p>암호화 값 생성: {@code ./gradlew jasyptEncrypt --args="<비밀번호> <평문>"}
 */
@Configuration
public class JasyptConfig {

  /**
   * Jasypt StringEncryptor 빈 등록
   *
   * @param password 암호화 비밀번호 (JASYPT_ENCRYPTOR_PASSWORD 환경변수)
   * @return StringEncryptor
   */
  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor(
      @Value("${jasypt.encryptor.password}") String password) {
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(password);
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);
    return encryptor;
  }
}
