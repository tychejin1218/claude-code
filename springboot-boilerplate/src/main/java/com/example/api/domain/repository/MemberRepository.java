package com.example.api.domain.repository;

import com.example.api.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 JPA 리포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

  /**
   * 이메일로 회원 단건 조회
   *
   * @param email 이메일
   * @return 회원 (없으면 empty)
   */
  Optional<Member> findByEmail(String email);

  /**
   * 이메일 존재 여부 확인
   *
   * @param email 이메일
   * @return 존재하면 true
   */
  boolean existsByEmail(String email);

  /**
   * 이름·이메일 부분 일치 회원 목록 조회
   *
   * @param name  이름 검색어
   * @param email 이메일 검색어
   * @param sort  정렬 조건
   * @return 조건에 일치하는 회원 목록
   */
  List<Member> findAllByNameContainingAndEmailContaining(
      String name, String email, Sort sort);
}
