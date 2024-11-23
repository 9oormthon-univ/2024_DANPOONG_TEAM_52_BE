package com.groom.orbit.config.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.config.security.domain.MemberDetails;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member =
        memberRepository
            .findById(Long.parseLong(username))
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

    return new MemberDetails(member);
  }
}
