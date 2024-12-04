package com.groom.orbit.resume.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.resume.app.ResumeQueryService;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class ResumeQueryController {

  private final ResumeQueryService resumeQueryService;

  @GetMapping
  public ResponseDto<GetResumeResponseDto> getResume(@AuthMember Long memberId) {
    return ResponseDto.ok(resumeQueryService.getResume(memberId));
  }

  @GetMapping("/{otherId}")
  public ResponseDto<Object> getResume(@AuthMember Long memberId, @PathVariable Long otherId) {
    return ResponseDto.ok(resumeQueryService.checkIsResume(memberId, otherId));
  }
}
