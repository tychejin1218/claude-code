package com.example.api.todo.service;

import com.example.api.common.exception.ApiException;
import com.example.api.common.type.ApiStatus;
import com.example.api.domain.entity.Member;
import com.example.api.domain.entity.Todo;
import com.example.api.domain.repository.MemberRepository;
import com.example.api.domain.repository.TodoRepository;
import com.example.api.todo.dto.TodoDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 할 일 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final MemberRepository memberRepository;

  /**
   * 내 할 일 목록 조회
   *
   * @param email 인증 회원 이메일
   * @return 할 일 목록
   */
  @Transactional(readOnly = true)
  public List<TodoDto.TodoResponse> getTodoList(String email) {
    return todoRepository.findByMemberEmailOrderByIdDesc(email).stream()
        .map(TodoDto.TodoResponse::from)
        .toList();
  }

  /**
   * 할 일 추가
   *
   * @param email   인증 회원 이메일
   * @param request 할 일 생성 요청
   * @return 생성된 할 일
   */
  @Transactional
  public TodoDto.TodoResponse insertTodo(String email, TodoDto.CreateRequest request) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, ApiStatus.UNAUTHORIZED));
    Todo todo = todoRepository.save(Todo.of(request.getTitle(), member));
    return TodoDto.TodoResponse.from(todo);
  }

  /**
   * 할 일 완료 처리
   *
   * @param email 인증 회원 이메일
   * @param id    할 일 ID
   * @return 업데이트된 할 일
   */
  @Transactional
  public TodoDto.TodoResponse updateTodoComplete(String email, Long id) {
    Todo todo = todoRepository.findByIdAndMemberEmail(id, email)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiStatus.NOT_FOUND));
    todo.complete();
    return TodoDto.TodoResponse.from(todo);
  }

  /**
   * 할 일 삭제
   *
   * @param email 인증 회원 이메일
   * @param id    할 일 ID
   */
  @Transactional
  public void deleteTodo(String email, Long id) {
    Todo todo = todoRepository.findByIdAndMemberEmail(id, email)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiStatus.NOT_FOUND));
    todo.softDelete();
  }
}
