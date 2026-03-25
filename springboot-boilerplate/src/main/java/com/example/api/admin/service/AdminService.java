package com.example.api.admin.service;

import com.example.api.admin.dto.AdminDto;
import com.example.api.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

  private final MemberRepository memberRepository;

  /**
   * 전체 회원 목록 조회
   *
   * @return 회원 목록
   */
  @Transactional(readOnly = true)
  public List<AdminDto.MemberResponse> getMemberList() {
    return memberRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
        .map(AdminDto.MemberResponse::from)
        .toList();
  }
}
