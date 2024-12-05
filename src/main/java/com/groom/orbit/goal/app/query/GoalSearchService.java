package com.groom.orbit.goal.app.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.app.dto.response.GoalSearchDetailResponseDto;
import com.groom.orbit.goal.app.dto.response.GoalSearchResponseDto;
import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.Quest;
import com.groom.orbit.job.app.InterestJobService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalSearchService {

  private final MemberGoalService memberGoalService;
  private final InterestJobService interestJobService;

  public GoalSearchDetailResponseDto findGoal(Long goalId) {
    List<MemberGoal> memberGoals = memberGoalService.findAllMemberGoal(goalId);
    Goal goal = findGoal(memberGoals);
    List<String> questTitles =
        memberGoals.stream()
            .map(MemberGoal::getQuests)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet())
            .stream()
            .sorted(Comparator.comparing(Quest::getCreatedAt).reversed())
            .map(Quest::getTitle)
            .toList();

    return new GoalSearchDetailResponseDto(
        goal.getCategory().getCategory(), goal.getTitle(), questTitles);
  }

  private static Goal findGoal(List<MemberGoal> memberGoals) {
    if (memberGoals.isEmpty()) {
      throw new CommonException(ErrorCode.NOT_FOUND_GOAL);
    }
    return memberGoals.getFirst().getGoal();
  }

  public Page<GoalSearchResponseDto> searchGoals(
      Long memberId, String category, List<Long> jobIds, Pageable pageable) {
    List<Long> memberIds =
        new ArrayList<>(
            interestJobService.findMemberInInterestJob(jobIds).stream().distinct().toList());
    memberIds.remove(memberId);
    Page<MemberGoal> memberGoals =
        memberGoalService.findMemberGoalInMemberId(
            memberIds, category, pageable); // 해당 사용자들의 목표를 조회

    return memberGoals.map(
        memberGoal ->
            new GoalSearchResponseDto(
                memberGoal.getGoal().getGoalId(), memberGoal.getGoal().getTitle()));
  }
}
