package com.example.api.sample.mapper;

import com.example.api.sample.dto.SampleDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 샘플 MyBatis 매퍼
 */
@Mapper
public interface SampleMapper {

  /**
   * 회원 목록 조회
   *
   * @param memberRequest 조회 조건
   * @return 회원 목록
   */
  List<SampleDto.MemberResponse> selectMembers(SampleDto.MemberRequest memberRequest);
}
