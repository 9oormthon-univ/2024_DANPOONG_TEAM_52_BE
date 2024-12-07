package com.groom.orbit.quest.app.dto.request;

import java.time.LocalDate;

public record CreateQuestRequestDto(Long goalId, String title, LocalDate deadline) {}
