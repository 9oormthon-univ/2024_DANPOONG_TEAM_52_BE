package com.groom.orbit.schedule.app;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.dao.QuestRepository;
import com.groom.orbit.schedule.app.dto.GetCalendarResponseDto;
import com.groom.orbit.schedule.app.dto.GetQuestResponseDto;
import com.groom.orbit.schedule.app.dto.GetScheduleResponseDto;
import com.groom.orbit.schedule.dao.Schedule;
import com.groom.orbit.schedule.dao.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryService {

  private final ScheduleRepository scheduleRepository;
  private final QuestRepository questRepository;

  public Schedule findById(Long scheduleId) {
    return scheduleRepository
        .findById(scheduleId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SCHEDULE));
  }

  public GetCalendarResponseDto getCalendar(Long memberId, Integer month, Integer year) {

    List<GetScheduleResponseDto> scheduleResponseDtoList =
        scheduleRepository.findAllByMonthAndMemberId(memberId, month, year).stream()
            .map(schedule -> GetScheduleResponseDto.fromSchedule(schedule))
            .toList();

    List<GetQuestResponseDto> questResponseDtoList =
        questRepository.findAllByMonthAndMemberId(memberId, month, year).stream()
            .map(quest -> GetQuestResponseDto.fromQuest(quest))
            .toList();

    return GetCalendarResponseDto.fromCalendarList(questResponseDtoList, scheduleResponseDtoList);
  }
}
