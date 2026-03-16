package com.example.api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 요청/응답 로깅 필터
 */
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    long startTime = System.currentTimeMillis();
    filterChain.doFilter(request, response);
    long elapsed = System.currentTimeMillis() - startTime;

    log.info("[REQUEST] {} {} → {} ({}ms)",
        request.getMethod(),
        request.getRequestURI(),
        response.getStatus(),
        elapsed);
  }
}
