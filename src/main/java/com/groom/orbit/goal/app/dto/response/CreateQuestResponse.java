package com.groom.orbit.goal.app.dto.response;

import java.time.LocalDate;

import com.groom.orbit.goal.dao.entity.Quest;

public record CreateQuestResponse(
    Long questId, String title, Boolean isComplete, LocalDate deadline, Integer sequence) {

  public static CreateQuestResponse fromEntity(Quest quest) {
    return new CreateQuestResponse(
        quest.getQuestId(),
        quest.getTitle(),
        quest.getIsComplete(),
        quest.getDeadline(),
        quest.getSequence());
  }
}
