package com.example.api.sample.repository;

import com.example.api.common.constants.Constants;
import com.example.api.domain.entity.QMember;
import com.example.api.sample.dto.SampleDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * 샘플 QueryDSL 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class SampleRepository {

  private final JPAQueryFactory jpaQueryFactory;

  /**
   * 회원 목록 조회
   *
   * <p>이름, 이메일 조건으로 동적 검색
   *
   * @param request 조회 조건
   * @return 회원 목록
   */
  public List<SampleDto.MemberResponse> selectMemberList(SampleDto.MemberListRequest request) {
    QMember member = QMember.member;
    BooleanBuilder builder = new BooleanBuilder();
    if (StringUtils.hasText(request.getName())) {
      builder.and(member.name.contains(request.getName()));
    }
    if (StringUtils.hasText(request.getEmail())) {
      builder.and(member.email.contains(request.getEmail()));
    }
    return jpaQueryFactory
        .select(Projections.fields(SampleDto.MemberResponse.class,
            member.id,
            member.name,
            member.email))
        .from(member)
        .where(builder)
        .orderBy(member.id.desc())
        .setHint(Constants.HIBERNATE_SQL_COMMENT, "SampleRepository.selectMemberList")
        .fetch();
  }

  /**
   * 회원 단건 조회
   *
   * @param request 조회 조건
   * @return 회원 정보 (미존재 시 {@code null})
   */
  public SampleDto.MemberResponse selectMember(SampleDto.MemberRequest request) {
    QMember member = QMember.member;
    return jpaQueryFactory
        .select(Projections.fields(SampleDto.MemberResponse.class,
            member.id,
            member.name,
            member.email))
        .from(member)
        .where(member.id.eq(request.getId()))
        .setHint(Constants.HIBERNATE_SQL_COMMENT, "SampleRepository.selectMember")
        .fetchOne();
  }
}
