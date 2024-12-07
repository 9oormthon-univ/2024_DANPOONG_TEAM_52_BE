package com.groom.orbit.goal.app.dto.request;

import java.util.List;

public record MemberGoalRequestDto(
    String title, String category, List<QuestTitleRequestDto> quests) {}
