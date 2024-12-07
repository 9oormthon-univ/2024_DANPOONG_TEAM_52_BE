package com.groom.orbit.quest.app.dto.request;

import java.time.LocalDate;

public record UpdateQuestRequestDto(String title, Boolean isComplete, LocalDate deadline) {}
