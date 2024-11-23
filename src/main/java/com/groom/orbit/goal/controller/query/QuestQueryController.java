package com.groom.orbit.goal.controller.query;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.goal.app.dto.response.GetQuestResponseDto;
import com.groom.orbit.goal.app.query.QuestQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest")
public class QuestQueryController {

  private final QuestQueryService questQueryService;

  @GetMapping
  public ResponseDto<List<GetQuestResponseDto>> findQuest(
      @RequestParam("member_goal_id") Long memberGoalId) {
    return ResponseDto.ok(questQueryService.findQuestsByGoalId(memberGoalId));
  }

  @GetMapping("/recommend")
  public ResponseDto<?> getRecommendedQuests(@RequestParam("member_goal_id") Long memberGoalId) {
    return ResponseDto.ok(questQueryService.getRecommendedQuests(memberGoalId));
  }
}
