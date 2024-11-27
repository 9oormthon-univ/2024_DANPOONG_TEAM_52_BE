package com.groom.orbit.goal.app.dto.response;

import java.util.List;

import com.groom.orbit.goal.dao.entity.GoalCategory;

public record GetMemberGoalResponseDto(
    Long memberGoalId,
    String goalTitle,
    GoalCategory category,
    Integer sequence,
    List<GetQuestResponseDto> quests) {}
