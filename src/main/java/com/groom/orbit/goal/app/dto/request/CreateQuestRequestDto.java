package com.groom.orbit.goal.app.dto.request;

import java.time.LocalDate;

public record CreateQuestRequestDto(Long goalId, String title, LocalDate deadline) {}
