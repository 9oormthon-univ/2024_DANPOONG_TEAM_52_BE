package com.groom.orbit.goal.app;

import java.time.LocalDateTime;
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
import com.groom.orbit.goal.app.dto.request.UpdateMemberGoalSequenceRequestDto;
import com.groom.orbit.goal.app.dto.response.GetMemberGoalResponseDto;
import com.groom.orbit.goal.app.dto.response.GetQuestResponseDto;
import com.groom.orbit.goal.app.query.GoalQueryService;
import com.groom.orbit.goal.dao.GoalRepository;
import com.groom.orbit.goal.dao.MemberGoalRepository;
import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.Quest;
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
  private final GoalRepository goalRepository;

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

  public CommonSuccessDto deleteMemberGoal(Long memberId, Long memberGoalId) {
    MemberGoal memberGoal = findMemberGoal(memberGoalId);

    memberGoal.validateMember(memberId);
    Goal goal = memberGoal.getGoal();
    goal.decreaseCount();
    memberGoalRepository.delete(memberGoal);

    return new CommonSuccessDto(true);
  }

  public GetMemberGoalResponseDto createGoal(Long memberId, MemberGoalRequestDto dto) {
    Member member = memberQueryService.findMember(memberId);
    Goal goal = getGoal(dto.title(), dto.category());

    MemberGoal memberGoal = MemberGoal.create(member, goal);
    goal.increaseCount();

    goalRepository.save(goal);

    Integer MemberGoalLen =
        memberGoalRepository.findAllByMemberIdAndIsCompleteFalse(memberId).size();

    memberGoal.setSequence(MemberGoalLen + 1);

    MemberGoal savedMemberGoal = memberGoalRepository.save(memberGoal);

    List<GetQuestResponseDto> questDtos =
        savedMemberGoal.getQuests().stream()
            .map(
                quest ->
                    new GetQuestResponseDto(
                        quest.getQuestId(), quest.getTitle(), quest.getIsComplete()))
            .toList();

    return new GetMemberGoalResponseDto(
        savedMemberGoal.getMemberGoalId(),
        savedMemberGoal.getTitle(),
        savedMemberGoal.getGoal().getCategory(),
        savedMemberGoal.getIsComplete(),
        savedMemberGoal.getSequence(),
        savedMemberGoal.getCreatedAt().toLocalDate(),
        savedMemberGoal.getCompletedDate().toLocalDate(),
        questDtos);
  }

  public CommonSuccessDto updateGoal(Long memberId, Long memberGoalId, MemberGoalRequestDto dto) {
    MemberGoal memberGoal = findMemberGoal(memberGoalId);
    Goal goal = getGoal(dto.title(), dto.category());

    memberGoal.validateMember(memberId);
    memberGoal.updateGoal(goal);

    return new CommonSuccessDto(true);
  }

  private Goal getGoal(String title, String category) {
    Optional<Goal> findGoal = goalQueryService.findGoalByTitleAndCategory(title, category);

    return findGoal.orElseGet(() -> goalCommandService.createGoal(title, category));
  }

  public List<GetMemberGoalResponseDto> findGoals(Long memberId, Boolean isComplete) {
    if (isComplete == null) {
      isComplete = false;
    }

    List<MemberGoal> memberGoals =
        memberGoalRepository.findByMemberIdAndIsComplete(memberId, isComplete);

    return memberGoals.stream()
        .map(
            memberGoal ->
                new GetMemberGoalResponseDto(
                    memberGoal.getMemberGoalId(),
                    memberGoal.getTitle(),
                    memberGoal.getGoal().getCategory(),
                    memberGoal.getIsComplete(),
                    memberGoal.getSequence(),
                    memberGoal.getCreatedAt().toLocalDate(),
                    memberGoal.getCompletedDate().toLocalDate(),
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

  public GetMemberGoalResponseDto findGoal(Long memberGoalId) {
    MemberGoal memberGoal = findMemberGoal(memberGoalId);
    List<Quest> quests = memberGoal.getQuests();

    List<GetQuestResponseDto> questDtos =
        quests.stream()
            .map(
                quest ->
                    new GetQuestResponseDto(
                        quest.getQuestId(), quest.getTitle(), quest.getIsComplete()))
            .toList();

    return new GetMemberGoalResponseDto(
        memberGoal.getMemberGoalId(),
        memberGoal.getTitle(),
        memberGoal.getGoal().getCategory(),
        memberGoal.getIsComplete(),
        memberGoal.getSequence(),
        memberGoal.getCreatedAt().toLocalDate(),
        memberGoal.getCompletedDate().toLocalDate(),
        questDtos);
  }

  public CommonSuccessDto updateMemberGoalSequence(
      Long memberId, List<UpdateMemberGoalSequenceRequestDto> requestDtoList) {

    List<MemberGoal> memberGoalList =
        memberGoalRepository.findAllByMemberIdAndIsCompleteFalse(memberId);

    for (UpdateMemberGoalSequenceRequestDto dto : requestDtoList) {
      memberGoalList.stream()
          .filter(memberGoal -> memberGoal.getMemberGoalId().equals(dto.memberGoalId()))
          .findFirst()
          .ifPresent(memberGoal -> memberGoal.setSequence(dto.sequence()));
    }

    memberGoalRepository.saveAll(memberGoalList);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateMemberGoalIsComplete(Long memberId, Long memberGoalId) {

    MemberGoal memberGoal = findMemberGoal(memberGoalId);

    memberGoal.setIsComplete(true);
    memberGoal.setCompletedDate(LocalDateTime.now());

    List<MemberGoal> memberGoalList =
        memberGoalRepository.findAllByMemberIdAndIsCompleteFalseAndSequenceGreaterThan(
            memberId, memberGoal.getSequence().longValue());

    memberGoalList.forEach(goal -> goal.setSequence(goal.getSequence() - 1));

    memberGoalRepository.save(memberGoal);
    memberGoalRepository.saveAll(memberGoalList);

    return CommonSuccessDto.fromEntity(true);
  }
}
