package com.groom.orbit.schedule.app.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groom.orbit.goal.dao.entity.GoalCategory;
import com.groom.orbit.quest.dao.entity.Quest;

public record GetQuestResponseDto(
    Long questId,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate deadline,
    String title,
    GoalCategory goalCategory) {

  public static GetQuestResponseDto fromQuest(Quest quest) {
    return new GetQuestResponseDto(
        quest.getQuestId(),
        quest.getDeadline(),
        quest.getTitle(),
        quest.getMemberGoal().getGoal().getCategory());
  }
}
