package com.groom.orbit.resume.app.dto;

import java.util.List;

public record GetOtherResumeResponseDto(
    List<ResumeResponseDto> academyList,
    List<ResumeResponseDto> careerList,
    List<ResumeResponseDto> qualificationList,
    List<ResumeResponseDto> experienceList,
    List<ResumeResponseDto> etcList,
    String profileImage,
    String nickname) {}
