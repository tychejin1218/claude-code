package com.example.api.auth.controller;

import com.example.api.auth.dto.AuthDto;
import com.example.api.auth.service.TempAuthService;
import com.example.api.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 임시 토큰 발급 컨트롤러 (local/dev/stg 전용)
 *
 * <p>SSO 없이 이메일만으로 JWT를 즉시 발급한다. Swagger 등 API 테스트 목적으로만 사용.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Profile({"local", "dev", "stg"})
public class TempAuthController implements TempAuthControllerDocs {

  private final TempAuthService tempAuthService;

  @Override
  @PostMapping("/token/temp")
  public BaseResponse<AuthDto.TokenResponse> issueTempToken(
      @RequestBody @Valid AuthDto.TempTokenRequest request) {
    return BaseResponse.ok(tempAuthService.issueToken(request));
  }
}
