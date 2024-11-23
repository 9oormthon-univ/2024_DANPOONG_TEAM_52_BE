package com.groom.orbit.schedule.app.dto;

import java.util.List;

public record GetCalendarResponseDto(
    List<GetQuestResponseDto> questResponseDtoList,
    List<GetScheduleResponseDto> scheduleResponseDtoList) {

  public static GetCalendarResponseDto fromCalendarList(
      List<GetQuestResponseDto> questResponseDtoList,
      List<GetScheduleResponseDto> scheduleResponseDtoList) {
    return new GetCalendarResponseDto(questResponseDtoList, scheduleResponseDtoList);
  }
}
