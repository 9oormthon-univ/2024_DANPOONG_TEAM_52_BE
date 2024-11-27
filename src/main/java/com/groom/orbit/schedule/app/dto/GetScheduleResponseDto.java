package com.groom.orbit.schedule.app.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groom.orbit.schedule.dao.Schedule;

public record GetScheduleResponseDto(
    Long scheduleId,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
    String content) {

  public static GetScheduleResponseDto fromSchedule(Schedule schedule) {
    return new GetScheduleResponseDto(
        schedule.getScheduleId(),
        schedule.getStartDate(),
        schedule.getEndDate(),
        schedule.getContent());
  }
}
