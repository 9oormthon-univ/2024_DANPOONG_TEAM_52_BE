package com.groom.orbit.schedule.controller;

import org.springframework.web.bind.annotation.*;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.schedule.app.ScheduleCommandService;
import com.groom.orbit.schedule.app.dto.ScheduleRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleCommandController {

  private final ScheduleCommandService scheduleCommandService;

  @PostMapping
  public ResponseDto<CommonSuccessDto> createSchedule(
      @AuthMember Long memberId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
    return ResponseDto.ok(scheduleCommandService.createSchedule(memberId, scheduleRequestDto));
  }

  @PatchMapping("/{scheduleId}")
  public ResponseDto<CommonSuccessDto> updateSchedule(
      @AuthMember Long memberId,
      @PathVariable Long scheduleId,
      @RequestBody ScheduleRequestDto scheduleRequestDto) {
    return ResponseDto.ok(
        scheduleCommandService.updateSchedule(memberId, scheduleId, scheduleRequestDto));
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseDto<CommonSuccessDto> deleteSchedule(
      @AuthMember Long memberId, @PathVariable Long scheduleId) {
    return ResponseDto.ok(scheduleCommandService.deleteSchedule(memberId, scheduleId));
  }
}
