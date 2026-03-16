package com.example.api.sample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.api.common.exception.ApiException;
import com.example.api.domain.entity.Member;
import com.example.api.domain.repository.MemberRepository;
import com.example.api.sample.dto.SampleDto;
import com.example.api.sample.mapper.SampleMapper;
import com.example.api.sample.repository.SampleRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

/**
 * 샘플 서비스 Mock 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("샘플 서비스 Mock 테스트")
class SampleServiceMockTest {

  @InjectMocks
  private SampleService sampleService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private SampleRepository sampleRepository;

  @Mock
  private SampleMapper sampleMapper;

  @Test
  @Order(1)
  @DisplayName("QueryDSL 회원 목록 조회 - 성공")
  void getMemberList_success() {
    // given
    SampleDto.MemberListRequest request = SampleDto.MemberListRequest.builder()
        .name("admin")
        .email("")
        .build();
    List<SampleDto.MemberResponse> mockResponses = List.of(
        SampleDto.MemberResponse.builder()
            .id(1L)
            .name("admin")
            .email("admin@test.com")
            .build()
    );
    given(sampleRepository.selectMemberList(request)).willReturn(mockResponses);

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMemberList(request);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("admin");
    verify(sampleRepository).selectMemberList(request);
  }

  @Test
  @Order(2)
  @DisplayName("QueryDSL 회원 목록 조회 - 빈 결과")
  void getMemberList_emptyResult() {
    // given
    SampleDto.MemberListRequest request = SampleDto.MemberListRequest.builder()
        .name("존재하지않는이름xyz")
        .email("")
        .build();
    given(sampleRepository.selectMemberList(request)).willReturn(Collections.emptyList());

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMemberList(request);

    // then
    assertThat(result).isEmpty();
    verify(sampleRepository).selectMemberList(request);
  }

  @Test
  @Order(3)
  @DisplayName("회원 단건 조회 - 성공")
  void getMember_success() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .id(1L)
        .build();
    SampleDto.MemberResponse mockResponse = SampleDto.MemberResponse.builder()
        .id(1L)
        .name("admin")
        .email("admin@test.com")
        .build();
    given(sampleRepository.selectMember(request)).willReturn(mockResponse);

    // when
    SampleDto.MemberResponse response = sampleService.getMember(request);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo("admin");
    assertThat(response.getEmail()).isEqualTo("admin@test.com");
    verify(sampleRepository).selectMember(request);
  }

  @Test
  @Order(4)
  @DisplayName("회원 단건 조회 - 존재하지 않는 회원")
  void getMember_notFound() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .id(999999L)
        .build();
    given(sampleRepository.selectMember(request)).willReturn(null);

    // when & then
    assertThatThrownBy(() -> sampleService.getMember(request))
        .isInstanceOf(ApiException.class);
    verify(sampleRepository).selectMember(request);
  }

  @Test
  @Order(5)
  @DisplayName("JPA Repository 회원 목록 조회 - 성공")
  void getMembersWithRepository_success() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .name("admin")
        .email("")
        .build();
    List<Member> members = List.of(Member.builder()
        .name("admin")
        .email("admin@test.com")
        .build());
    given(memberRepository.findAllByNameContainingAndEmailContaining(
        eq("admin"), eq(""), any(Sort.class))).willReturn(members);

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMembersWithRepository(request);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("admin");
    verify(memberRepository).findAllByNameContainingAndEmailContaining(
        eq("admin"), eq(""), any(Sort.class));
  }

  @Test
  @Order(6)
  @DisplayName("JPA Repository 회원 목록 조회 - 빈 결과")
  void getMembersWithRepository_emptyResult() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .name("존재하지않는이름xyz")
        .email("")
        .build();
    given(memberRepository.findAllByNameContainingAndEmailContaining(
        eq("존재하지않는이름xyz"), eq(""), any(Sort.class))).willReturn(Collections.emptyList());

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMembersWithRepository(request);

    // then
    assertThat(result).isEmpty();
    verify(memberRepository).findAllByNameContainingAndEmailContaining(
        eq("존재하지않는이름xyz"), eq(""), any(Sort.class));
  }

  @Test
  @Order(7)
  @DisplayName("MyBatis Mapper 회원 목록 조회 - 성공")
  void getMembersWithMapper_success() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .name("admin")
        .email("")
        .build();
    List<SampleDto.MemberResponse> mockResponses = List.of(
        SampleDto.MemberResponse.builder()
            .id(1L)
            .name("admin")
            .email("admin@test.com")
            .build()
    );
    given(sampleMapper.selectMembers(request)).willReturn(mockResponses);

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMembersWithMapper(request);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("admin");
    verify(sampleMapper).selectMembers(request);
  }

  @Test
  @Order(8)
  @DisplayName("MyBatis Mapper 회원 목록 조회 - 빈 결과")
  void getMembersWithMapper_emptyResult() {
    // given
    SampleDto.MemberRequest request = SampleDto.MemberRequest.builder()
        .name("존재하지않는이름xyz")
        .email("")
        .build();
    given(sampleMapper.selectMembers(request)).willReturn(Collections.emptyList());

    // when
    List<SampleDto.MemberResponse> result = sampleService.getMembersWithMapper(request);

    // then
    assertThat(result).isEmpty();
    verify(sampleMapper).selectMembers(request);
  }
}
