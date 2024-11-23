package com.groom.orbit.auth.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groom.orbit.auth.dao.entity.AuthMember;

public interface AuthMemberRepository extends JpaRepository<AuthMember, Long> {

  Optional<AuthMember> findByKakaoNickname(String kakaoNickname);
}
