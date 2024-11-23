package com.groom.orbit.goal.controller.query;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.app.dto.response.GetCompletedGoalResponseDto;
import com.groom.orbit.goal.app.dto.response.GetOnGoingGoalResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goal")
public class MemberGoalQueryController {

  private final MemberGoalService memberGoalService;

  @GetMapping("/on-going")
  public ResponseDto<List<GetOnGoingGoalResponseDto>> getOnGoingGoals(@AuthMember Long memberId) {
    return ResponseDto.ok(memberGoalService.findOnGoingGoals(memberId));
  }

  @GetMapping("/completed")
  public ResponseDto<List<GetCompletedGoalResponseDto>> getCompletedGoals(
      @AuthMember Long memberId) {
    return ResponseDto.ok(memberGoalService.findCompletedGoals(memberId));
  }
}
