package com.groom.orbit.auth.dao;

import com.groom.orbit.auth.dao.entity.Member;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByNickname(String email);
}
