package com.groom.orbit.member.member.app.dto.response;

import com.groom.orbit.member.member.dao.jpa.entity.Member;

public record GetMemberAiFeedbackResponseDto(String aiFeedback) {

  public static GetMemberAiFeedbackResponseDto fromMember(Member member) {
    return new GetMemberAiFeedbackResponseDto(member.getAiFeedback());
  }
}
