package com.groom.orbit.member.member.app.dto.request;

public record UpdateMemberRequestDto(
    String nickname,
    String knownPrompt,
    String helpPrompt,
    Boolean isNotification,
    Boolean isProfile) {}
