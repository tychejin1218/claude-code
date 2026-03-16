package com.example.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.example.api.common.exception.ApiException;
import com.example.api.domain.entity.Member;
import com.example.api.domain.repository.MemberRepository;
import com.example.api.user.dto.UserDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 회원 서비스 Mock 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("회원 서비스 Mock 테스트")
class UserServiceMockTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private MemberRepository memberRepository;

  @Test
  @Order(1)
  @DisplayName("내 정보 조회 - 성공")
  void getMe_success() {
    // given
    Member member = Member.builder()
        .id(1L)
        .name("홍길동")
        .email("test@example.com")
        .build();
    given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.of(member));

    // when
    UserDto.MeResponse response = userService.getMe("test@example.com");

    // then
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo("홍길동");
    assertThat(response.getEmail()).isEqualTo("test@example.com");
  }

  @Test
  @Order(2)
  @DisplayName("내 정보 조회 - 존재하지 않는 회원")
  void getMe_notFound() {
    // given
    given(memberRepository.findByEmail("notfound@example.com")).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userService.getMe("notfound@example.com"))
        .isInstanceOf(ApiException.class);
  }
}
