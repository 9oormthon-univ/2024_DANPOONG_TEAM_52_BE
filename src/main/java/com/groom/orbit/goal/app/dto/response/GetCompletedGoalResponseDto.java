package com.groom.orbit.goal.app.dto.response;

import java.util.List;

public record GetCompletedGoalResponseDto(String title, List<String> questName) {}
