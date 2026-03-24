package com.example.api.auth.controller;

import com.example.api.auth.dto.AuthDto;
import com.example.api.auth.service.AuthService;
import com.example.api.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 컨트롤러
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @Override
  @PostMapping("/register")
  public BaseResponse<Void> register(
      @RequestBody @Valid AuthDto.RegisterRequest request) {
    authService.register(request);
    return BaseResponse.ok();
  }

  @Override
  @GetMapping("/verify-email")
  public BaseResponse<AuthDto.TokenResponse> verifyEmail(@RequestParam String token) {
    return BaseResponse.ok(authService.verifyEmail(token));
  }

  @Override
  @PostMapping("/resend-verification")
  public BaseResponse<Void> resendVerification(
      @RequestBody @Valid AuthDto.ResendVerificationRequest request) {
    authService.resendVerification(request);
    return BaseResponse.ok();
  }

  @Override
  @PostMapping("/login")
  public BaseResponse<AuthDto.TokenResponse> login(
      @RequestBody @Valid AuthDto.LoginRequest request) {
    return BaseResponse.ok(authService.login(request));
  }

  @Override
  @PostMapping("/logout")
  public BaseResponse<Void> logout(
      @RequestBody @Valid AuthDto.RefreshRequest request) {
    authService.logout(request);
    return BaseResponse.ok();
  }

  @Override
  @PostMapping("/token/refresh")
  public BaseResponse<AuthDto.TokenResponse> refresh(
      @RequestBody @Valid AuthDto.RefreshRequest request) {
    return BaseResponse.ok(authService.refresh(request));
  }
}
