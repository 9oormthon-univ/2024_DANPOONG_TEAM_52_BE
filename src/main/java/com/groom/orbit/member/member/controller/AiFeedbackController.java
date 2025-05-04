package com.groom.orbit.member.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.member.member.app.AiFeedbackService;
import com.groom.orbit.member.member.app.dto.response.GetFeedbackResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class AiFeedbackController {

  private final AiFeedbackService aiFeedbackService;

  @PostMapping("/ai")
  public ResponseDto<GetFeedbackResponseDto> getFeedback(@AuthMember Long memberId) {
    return ResponseDto.ok(aiFeedbackService.getFeedback(memberId));
  }
}
