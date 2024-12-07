package com.groom.orbit.member.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.app.dto.response.GetMemberAiFeedbackResponseDto;
import com.groom.orbit.member.app.dto.response.GetMemberProfileResponseDto;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

  private final MemberRepository memberRepository;

  public Member findMember(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));
  }

  public GetMemberProfileResponseDto getMemberProfile(Long memberId) {

    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

    return GetMemberProfileResponseDto.fromMember(member);
  }

  public GetMemberAiFeedbackResponseDto getMemberAiFeedback(Long memberId) {

    Member member = findMember(memberId);

    return GetMemberAiFeedbackResponseDto.fromMember(member);
  }
}
