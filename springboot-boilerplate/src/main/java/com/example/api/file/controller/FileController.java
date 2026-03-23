package com.example.api.file.controller;

import com.example.api.common.response.BaseResponse;
import com.example.api.file.dto.FileDto;
import com.example.api.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 파일 컨트롤러
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController implements FileControllerDocs {

  private final FileService fileService;

  @Override
  @GetMapping("/presigned-url")
  public BaseResponse<FileDto.PresignedUrlResponse> getPresignedUrl(
      @RequestParam String fileName,
      @RequestParam String contentType) {
    return BaseResponse.ok(fileService.getPresignedUrl(fileName, contentType));
  }
}
