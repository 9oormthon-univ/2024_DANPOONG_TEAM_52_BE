package com.groom.orbit.goal.app.dto.response;

public record GetOnGoingGoalResponseDto(
    Long goalId, String title, long totalQuestCount, long finishQuestCount) {}
