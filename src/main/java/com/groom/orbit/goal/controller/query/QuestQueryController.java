package com.groom.orbit.goal.controller.query;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.goal.app.dto.response.GetQuestResponseDto;
import com.groom.orbit.goal.app.query.QuestQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest")
public class QuestQueryController {

  private final QuestQueryService questQueryService;

  /** TODO add annotation */
  @GetMapping
  public ResponseDto<List<GetQuestResponseDto>> findQuest(
      @AuthMember Long memberId, @RequestParam("goal_id") Long goalId) {
    return ResponseDto.ok(questQueryService.findQuestsByGoalId(memberId, goalId));
  }

  @GetMapping("/recommend")
  public ResponseDto<?> getRecommendedQuests(@RequestParam("member_goal_id") Long memberGoalId) {
    return ResponseDto.ok(questQueryService.getRecommendedQuests(memberGoalId));
  }
}
