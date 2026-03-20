package com.example.api.admin.controller;

import com.example.api.admin.dto.AdminDto;
import com.example.api.admin.service.AdminService;
import com.example.api.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 컨트롤러
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController implements AdminControllerDocs {

  private final AdminService adminService;

  @Override
  @GetMapping("/members")
  @PreAuthorize("hasRole('ADMIN')")
  public BaseResponse<List<AdminDto.MemberResponse>> getMemberList() {
    return BaseResponse.ok(adminService.getMemberList());
  }
}
