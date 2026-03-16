package com.example.api.domain.repository;

import com.example.api.domain.entity.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 할 일 JPA 리포지토리
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

  /**
   * 회원 이메일로 할 일 목록 조회 (최신순)
   *
   * @param email 회원 이메일
   * @return 할 일 목록
   */
  List<Todo> findByMemberEmailOrderByIdDesc(String email);

  /**
   * ID와 회원 이메일로 할 일 단건 조회
   *
   * @param id    할 일 ID
   * @param email 회원 이메일
   * @return 할 일 (없으면 empty)
   */
  Optional<Todo> findByIdAndMemberEmail(Long id, String email);
}
