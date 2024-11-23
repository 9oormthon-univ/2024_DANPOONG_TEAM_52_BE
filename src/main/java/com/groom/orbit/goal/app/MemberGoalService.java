package com.groom.orbit.goal.app;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.dto.response.GetCompletedGoalResponseDto;
import com.groom.orbit.goal.app.dto.response.GetOnGoingGoalResponseDto;
import com.groom.orbit.goal.app.query.QuestQueryService;
import com.groom.orbit.goal.dao.MemberGoalRepository;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.MemberGoalId;
import com.groom.orbit.goal.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberGoalService {

  private final MemberGoalRepository memberGoalRepository;
  private final QuestQueryService questQueryService;

  @Transactional(readOnly = true)
  public MemberGoal findMemberGoal(Long memberId, Long goalId) {
    MemberGoalId memberGoalId = MemberGoalId.create(memberId, goalId);

    return memberGoalRepository
        .findById(memberGoalId.getMemberId(), memberGoalId.getGoalId())
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_GOAL));
  }

  public List<GetOnGoingGoalResponseDto> findOnGoingGoals(Long memberId) {
    List<MemberGoal> memberGoals = memberGoalRepository.findByIsComplete(memberId, false);
    return memberGoals.stream()
        .map(
            memberGoal -> {
              Long goalId = memberGoal.getGoalId();
              long totalQuestCount = questQueryService.getTotalQuestCount(goalId);
              long finishQuestCount = questQueryService.getFinishQuestCount(goalId);

              return new GetOnGoingGoalResponseDto(
                  memberGoal.getGoalId(), memberGoal.getTitle(), totalQuestCount, finishQuestCount);
            })
        .toList();
  }

  public List<GetCompletedGoalResponseDto> findCompletedGoals(Long memberId) {
    List<MemberGoal> memberGoals = memberGoalRepository.findByIsComplete(memberId, true);
    return memberGoals.stream()
        .map(
            memberGoal -> {
              List<Quest> quests =
                  questQueryService.findQuestsByMemberAndGoal(memberId, memberGoal.getGoalId());
              return new GetCompletedGoalResponseDto(
                  memberGoal.getTitle(), quests.stream().map(Quest::getTitle).toList());
            })
        .toList();
  }
}
