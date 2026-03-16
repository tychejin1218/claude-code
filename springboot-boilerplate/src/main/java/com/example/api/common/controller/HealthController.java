package com.example.api.common.controller;

import com.example.api.common.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 헬스 체크 컨트롤러
 */
@RestController
public class HealthController {

  /**
   * 루트 경로 헬스 체크
   *
   * @return 성공 응답
   */
  @GetMapping({"/", "/health"})
  public BaseResponse<Void> health() {
    return BaseResponse.ok();
  }
}
