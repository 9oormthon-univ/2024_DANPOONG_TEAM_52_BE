package com.groom.orbit.member.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.member.app.MemberCommandService;
import com.groom.orbit.member.app.dto.request.UpdateMemberRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberCommandController {

  private final MemberCommandService memberCommandService;

  @PostMapping("/ai")
  public ResponseDto<String> createAiFeedback(@AuthMember Long memberId) {
    return ResponseDto.ok(memberCommandService.createAiFeedbackResponse(memberId));
  }

  @PatchMapping
  public ResponseDto<CommonSuccessDto> updateMember(
      @AuthMember Long memberId, @RequestBody UpdateMemberRequestDto requestDto) {
    return ResponseDto.ok(memberCommandService.updateMember(memberId, requestDto));
  }

  @PatchMapping(
      value = "/image",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseDto<CommonSuccessDto> updateMemberProfileImageUrl(
      @AuthMember Long memberId,
      @RequestPart(value = "file", required = false) MultipartFile file) {
    return ResponseDto.ok(memberCommandService.updateMemberProfileImage(memberId, file));
  }
}
