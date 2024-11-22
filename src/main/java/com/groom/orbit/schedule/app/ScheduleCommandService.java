package com.groom.orbit.schedule.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.schedule.app.dto.ScheduleRequestDto;
import com.groom.orbit.schedule.dao.Schedule;
import com.groom.orbit.schedule.dao.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleCommandService {

  private final ScheduleRepository scheduleRepository;
  private final ScheduleQueryService scheduleQueryService;
  private final MemberQueryService memberQueryService;

  public CommonSuccessDto createSchedule(Long memberId, ScheduleRequestDto requestDto) {

    Member member = memberQueryService.findMember(memberId);

    Schedule schedule =
        Schedule.builder()
            .content(requestDto.content())
            .startDate(requestDto.startDate())
            .endDate(requestDto.endDate())
            .member(member)
            .build();

    scheduleRepository.save(schedule);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateSchedule(Long scheduleId, ScheduleRequestDto requestDto) {

    Schedule schedule = scheduleQueryService.findById(scheduleId);

    schedule.updateSchedule(requestDto);

    scheduleRepository.save(schedule);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto deleteSchedule(Long scheduleId) {
    scheduleRepository.deleteById(scheduleId);
    return CommonSuccessDto.fromEntity(true);
  }
}