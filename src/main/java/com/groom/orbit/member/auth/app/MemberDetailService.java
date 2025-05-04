package com.groom.orbit.member.auth.app;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.auth.app.domain.MemberDetails;
import com.groom.orbit.member.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  //  private final MemberRepository memberRepository;
  private final MemberRepository authMemberRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Member member =
        authMemberRepository
            .findById(Long.parseLong(userId))
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

    return new MemberDetails(member);
  }
}
