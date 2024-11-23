package com.groom.orbit.auth.dao.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "auth_member")
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(name = "kakao_nickname")
  private String kakaoNickname;

  @Column(name = "nickname", length = 100)
  private String nickname;

  @ColumnDefault("")
  @Column(name = "image_url")
  @Builder.Default
  private String imageUrl = "";
}
