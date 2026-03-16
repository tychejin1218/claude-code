package com.example.api.sample.controller;

import com.example.api.common.response.BaseResponse;
import com.example.api.sample.dto.SampleDto;
import com.example.api.sample.service.SampleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 샘플 컨트롤러
 */
@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController implements SampleControllerDocs {

  private final SampleService sampleService;

  @Override
  @GetMapping("/member")
  public BaseResponse<List<SampleDto.MemberResponse>> getMemberList(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email) {
    return BaseResponse.ok(sampleService.getMemberList(SampleDto.MemberListRequest.of(name, email)));
  }

  @Override
  @GetMapping("/member/{id}")
  public BaseResponse<SampleDto.MemberResponse> getMember(@PathVariable long id) {
    return BaseResponse.ok(sampleService.getMember(SampleDto.MemberRequest.of(id)));
  }

  @Override
  @GetMapping("/members/repository")
  public BaseResponse<List<SampleDto.MemberResponse>> getMembersWithRepository(
      @RequestParam(defaultValue = "") String name,
      @RequestParam(defaultValue = "") String email) {
    return BaseResponse.ok(
        sampleService.getMembersWithRepository(SampleDto.MemberRequest.of(name, email)));
  }

  @Override
  @GetMapping("/members/mapper")
  public BaseResponse<List<SampleDto.MemberResponse>> getMembersWithMapper(
      @RequestParam(defaultValue = "") String name,
      @RequestParam(defaultValue = "") String email) {
    return BaseResponse.ok(
        sampleService.getMembersWithMapper(SampleDto.MemberRequest.of(name, email)));
  }
}
