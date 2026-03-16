package com.example.api.user.controller;

import com.example.api.common.response.BaseResponse;
import com.example.api.user.dto.UserDto;
import com.example.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 컨트롤러
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserService userService;

  @Override
  @GetMapping("/me")
  public BaseResponse<UserDto.MeResponse> getMe(@AuthenticationPrincipal String email) {
    return BaseResponse.ok(userService.getMe(email));
  }
}
