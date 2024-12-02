package com.groom.orbit.resume.controller;

import org.springframework.web.bind.annotation.*;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.resume.app.ResumeCommendService;
import com.groom.orbit.resume.app.dto.ResumeRequestDto;
import com.groom.orbit.resume.app.dto.ResumeResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class ResumeCommandController {

  private final ResumeCommendService resumeCommandService;

  @PostMapping()
  public ResponseDto<CommonSuccessDto> createResume(
      @AuthMember Long memberId, @RequestBody ResumeRequestDto requestDto) {
    return ResponseDto.ok(resumeCommandService.createResume(memberId, requestDto));
  }

  @PatchMapping("/{resumeId}")
  public ResponseDto<CommonSuccessDto> updateResume(
      @AuthMember Long memberId,
      @PathVariable Long resumeId,
      @RequestBody ResumeRequestDto requestDto) {
    return ResponseDto.ok(resumeCommandService.updateResume(resumeId, requestDto));
  }

  @DeleteMapping("/{resumeId}")
  public ResponseDto<CommonSuccessDto> deleteResume(
      @AuthMember Long memberId, @PathVariable Long resumeId) {
    return ResponseDto.ok(resumeCommandService.deleteResume(memberId, resumeId));
  }

  @PostMapping("/{member_goal_id}")
  public ResponseDto<ResumeResponseDto> createResumeFromMemberGoal(
      @AuthMember Long memberId,
      @PathVariable(name = "member_goal_id") Long memberGoalId,
      @RequestBody ResumeRequestDto requestDto) {
    return ResponseDto.ok(
        resumeCommandService.createResumeFromMemberGoal(memberId, memberGoalId, requestDto));
  }
}
