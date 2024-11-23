package com.groom.orbit.goal.app.dto.request;

import java.time.LocalDate;

public record UpdateQuestRequestDto(String title, Boolean isComplete, LocalDate deadline) {}
