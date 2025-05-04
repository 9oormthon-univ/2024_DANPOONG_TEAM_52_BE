package com.groom.orbit.member.member.dao.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groom.orbit.member.member.dao.jpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByKakaoNickname(String kakaoNickname);
}
