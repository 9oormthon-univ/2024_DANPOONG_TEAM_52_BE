package com.groom.orbit.goal.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.command.GoalCommandService;
import com.groom.orbit.goal.app.dto.request.MemberGoalRequestDto;
import com.groom.orbit.goal.app.dto.response.GetMemberGoalResponseDto;
import com.groom.orbit.goal.app.dto.response.GetQuestResponseDto;
import com.groom.orbit.goal.app.query.GoalQueryService;
import com.groom.orbit.goal.dao.MemberGoalRepository;
import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberGoalService {

  private final MemberGoalRepository memberGoalRepository;
  private final MemberQueryService memberQueryService;
  private final GoalQueryService goalQueryService;
  private final GoalCommandService goalCommandService;

  @Transactional(readOnly = true)
  public MemberGoal findMemberGoal(Long memberId, Long goalId) {

    return memberGoalRepository
        .findById(memberId, goalId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_GOAL));
  }

  @Transactional(readOnly = true)
  public MemberGoal findMemberGoal(Long memberGoalId) {

    return memberGoalRepository
        .findById(memberGoalId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_GOAL));
  }

  public CommonSuccessDto deleteGoal(Long memberId, Long goalId) {
    MemberGoal memberGoal = findMemberGoal(memberId, goalId);
    Goal goal = memberGoal.getGoal();

    goal.decreaseCount();
    memberGoalRepository.delete(memberGoal);

    return new CommonSuccessDto(true);
  }

  public CommonSuccessDto createGoal(Long memberId, MemberGoalRequestDto dto) {
    Member member = memberQueryService.findMember(memberId);
    Goal goal = getGoal(dto.title(), dto.category());
    MemberGoal memberGoal = MemberGoal.create(member, goal);
    goal.increaseCount();

    memberGoalRepository.save(memberGoal);

    return new CommonSuccessDto(true);
  }

  public CommonSuccessDto updateGoal(Long memberId, Long goalId, MemberGoalRequestDto dto) {
    MemberGoal memberGoal = findMemberGoal(memberId, goalId);
    Goal goal = getGoal(dto.title(), dto.category());

    validateMemberGoal(memberId, goal.getGoalId());
    memberGoal.updateGoal(goal);

    return new CommonSuccessDto(true);
  }

  private void validateMemberGoal(Long memberId, Long goalId) {
    if (memberGoalRepository.findByMemberIdAndGoalId(memberId, goalId).isPresent()) {
      throw new CommonException(ErrorCode.ALREADY_EXISTS_GOAL);
    }
  }

  private Goal getGoal(String title, String category) {
    Optional<Goal> findGoal = goalQueryService.findGoalByTitleAndCategory(title, category);

    return findGoal.orElseGet(() -> goalCommandService.createGoal(title, category));
  }

  public List<GetMemberGoalResponseDto> findGoals(Long memberId, Boolean isComplete) {
    List<MemberGoal> memberGoals =
        memberGoalRepository.findByMemberIdAndIsComplete(memberId, isComplete);

    return memberGoals.stream()
        .map(
            memberGoal ->
                new GetMemberGoalResponseDto(
                    memberGoal.getMemberGoalId(),
                    memberGoal.getTitle(),
                    getGetQuestResponseDtos(memberGoal)))
        .toList();
  }

  private static List<GetQuestResponseDto> getGetQuestResponseDtos(MemberGoal mg) {
    return mg.getQuests().stream()
        .map(q -> new GetQuestResponseDto(q.getQuestId(), q.getTitle(), q.getIsComplete()))
        .toList();
  }

  public List<String> findMemberGoalNotCompleted(Long memberId) {
    List<MemberGoal> memberGoals = memberGoalRepository.findNotCompletedByMemberId(memberId);
    List<Long> startIds = getStartIds(memberGoals);

    if (!startIds.isEmpty()) {
      return getGoalTitle(startIds);
    }

    return new ArrayList<>();
  }

  private static List<Long> getStartIds(List<MemberGoal> memberGoals) {
    return memberGoals.stream().map(mg -> mg.getGoal().getGoalId()).toList();
  }

  private List<String> getGoalTitle(List<Long> startIds) {
    List<Goal> goals = goalQueryService.findNotIn(startIds);

    return goals.stream().map(Goal::getTitle).toList();
  }

  public List<MemberGoal> findMemberGoalsByGoalId(Long goalId) {
    return memberGoalRepository.findAllByGoalId(goalId);
  }
}
