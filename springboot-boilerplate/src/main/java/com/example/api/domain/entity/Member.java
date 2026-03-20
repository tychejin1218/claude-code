package com.example.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

/**
 * 회원 엔티티
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@SQLRestriction("deleted_at IS NULL")
@Table(name = "member")
@Entity
public class Member extends BaseAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, length = 200)
  private String email;

  @Column(name = "password", nullable = false, length = 200)
  private String password;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  private MemberRole role = MemberRole.ROLE_USER;

  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<Todo> todos = new ArrayList<>();

  /**
   * 회원 생성 (이메일 + 비밀번호)
   *
   * @param email           이메일
   * @param name            이름
   * @param encodedPassword BCrypt 인코딩된 비밀번호
   * @return Member
   */
  public static Member of(String email, String name, String encodedPassword) {
    return Member.builder()
        .email(email)
        .name(name)
        .password(encodedPassword)
        .build();
  }
}
