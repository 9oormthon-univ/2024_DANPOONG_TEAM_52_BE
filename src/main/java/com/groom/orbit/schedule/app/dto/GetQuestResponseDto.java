package com.groom.orbit.schedule.app.dto;

import java.time.LocalDate;

import com.groom.orbit.goal.dao.entity.Quest;

public record GetQuestResponseDto(LocalDate deadline, String title) {

  public static GetQuestResponseDto fromQuest(Quest quest) {
    return new GetQuestResponseDto(quest.getDeadline(), quest.getTitle());
  }
}
