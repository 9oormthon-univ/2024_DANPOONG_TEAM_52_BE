package com.groom.orbit.schedule.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.schedule.app.ScheduleQueryService;
import com.groom.orbit.schedule.app.dto.GetCalendarResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarQueryController {

  private final ScheduleQueryService scheduleQueryService;

  @GetMapping("/{month}")
  public ResponseDto<GetCalendarResponseDto> getCalendar(
      @AuthMember Long memberId, @PathVariable Integer month) {
    return ResponseDto.ok(scheduleQueryService.getCalendar(memberId, month));
  }
}
