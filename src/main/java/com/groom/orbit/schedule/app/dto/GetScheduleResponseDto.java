package com.groom.orbit.schedule.app.dto;

import java.time.LocalDate;

import com.groom.orbit.schedule.dao.Schedule;

public record GetScheduleResponseDto(LocalDate startDate, LocalDate endDate, String content) {

  public static GetScheduleResponseDto fromSchedule(Schedule schedule) {
    return new GetScheduleResponseDto(
        schedule.getStartDate(), schedule.getEndDate(), schedule.getContent());
  }
}
