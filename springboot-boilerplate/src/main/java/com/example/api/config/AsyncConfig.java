package com.example.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 처리 설정
 *
 * <p>이메일 발송 등 I/O 블로킹 작업을 별도 스레드에서 실행하기 위해 {@code @Async}를 활성화합니다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

}
