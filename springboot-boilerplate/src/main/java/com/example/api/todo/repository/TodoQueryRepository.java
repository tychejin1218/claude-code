package com.example.api.todo.repository;

import com.example.api.common.constants.Constants;
import com.example.api.domain.entity.QMember;
import com.example.api.domain.entity.QTodo;
import com.example.api.domain.entity.Todo;
import com.example.api.todo.dto.TodoDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 할 일 QueryDSL 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

  private static final String STATUS_COMPLETED = "completed";
  private static final String STATUS_INCOMPLETE = "incomplete";
  private static final String SORT_TITLE = "title";

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 할 일 목록 조회 (페이지네이션 + 상태 필터 + 정렬)
   *
   * @param email   회원 이메일
   * @param request 조회 조건 (page, size, status, sort)
   * @return 할 일 목록
   */
  public List<Todo> selectTodoList(String email, TodoDto.TodoListRequest request) {
    QTodo todo = QTodo.todo;
    QMember member = QMember.member;

    BooleanBuilder builder = buildCondition(todo, member, email, request.getStatus());

    return jpaQueryFactory
        .selectFrom(todo)
        .join(todo.member, member)
        .where(builder)
        .orderBy(buildOrder(todo, request.getSort()))
        .offset((long) request.getPage() * request.getSize())
        .limit(request.getSize())
        .setHint(Constants.HIBERNATE_SQL_COMMENT, "TodoQueryRepository.selectTodoList")
        .fetch();
  }

  /**
   * 할 일 전체 개수 조회 (상태 필터 적용)
   *
   * @param email   회원 이메일
   * @param request 조회 조건 (status)
   * @return 전체 개수
   */
  public long selectTodoCount(String email, TodoDto.TodoListRequest request) {
    QTodo todo = QTodo.todo;
    QMember member = QMember.member;

    BooleanBuilder builder = buildCondition(todo, member, email, request.getStatus());

    Long count = jpaQueryFactory
        .select(todo.count())
        .from(todo)
        .join(todo.member, member)
        .where(builder)
        .setHint(Constants.HIBERNATE_SQL_COMMENT, "TodoQueryRepository.selectTodoCount")
        .fetchOne();
    return count != null ? count : 0L;
  }

  private BooleanBuilder buildCondition(QTodo todo, QMember member, String email, String status) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(member.email.eq(email));
    if (STATUS_COMPLETED.equals(status)) {
      builder.and(todo.completed.isTrue());
    } else if (STATUS_INCOMPLETE.equals(status)) {
      builder.and(todo.completed.isFalse());
    }
    return builder;
  }

  private OrderSpecifier<?> buildOrder(QTodo todo, String sort) {
    if (SORT_TITLE.equals(sort)) {
      return todo.title.asc();
    }
    return todo.id.desc();
  }
}
