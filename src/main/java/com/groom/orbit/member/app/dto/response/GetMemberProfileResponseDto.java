package com.groom.orbit.member.app.dto.response;

import com.groom.orbit.member.dao.jpa.entity.Member;

public record GetMemberProfileResponseDto(
    Long memberId,
    String imageUrl,
    String nickname,
    String knownPrompt,
    String helpPrompt,
    Boolean isNotification,
    Boolean isResume) {

  public static GetMemberProfileResponseDto fromMember(Member member) {
    return new GetMemberProfileResponseDto(
        member.getId(),
        member.getImageUrl(),
        member.getNickname(),
        member.getKnownPrompt(),
        member.getHelpPrompt(),
        member.getIsNotification(),
        member.getIsProfile());
  }
}
