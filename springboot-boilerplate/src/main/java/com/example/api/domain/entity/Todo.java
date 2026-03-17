package com.example.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

/**
 * 할 일 엔티티
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@SQLRestriction("deleted_at IS NULL")
@Table(name = "todo")
@Entity
public class Todo extends BaseAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Column(name = "completed", nullable = false)
  private boolean completed;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  /**
   * 할 일 완료 처리
   */
  public void complete() {
    this.completed = true;
  }

  /**
   * 할 일 생성
   *
   * @param title  할 일 내용
   * @param member 소유 회원
   * @return Todo
   */
  public static Todo of(String title, Member member) {
    return Todo.builder()
        .title(title)
        .completed(false)
        .member(member)
        .build();
  }
}
