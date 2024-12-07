package com.groom.orbit.quest.app.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record GetQuestResponseDto(
    Long id,
    String title,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate deadline,
    Boolean isComplete) {}
