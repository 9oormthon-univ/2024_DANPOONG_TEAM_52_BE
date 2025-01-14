package com.groom.orbit.quest.app;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.fcm.FcmService;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.quest.app.dto.response.GetQuestResponseDto;
import com.groom.orbit.quest.dao.QuestRepository;
import com.groom.orbit.quest.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestQueryService {

  private final QuestRepository questRepository;
  private final MemberGoalService memberGoalService;
  private final FcmService fcmService;

  public Quest findQuest(Long questId) {
    return questRepository
        .findById(questId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QUEST));
  }

  public List<Quest> findQuestsByMemberAndGoal(Long memberGoalId) {
    return questRepository.findByMemberGoalId(memberGoalId);
  }

  public List<GetQuestResponseDto> findQuestsByGoalId(Long memberGoalId) {
    List<Quest> quests = findQuestsByMemberAndGoal(memberGoalId);

    return quests.stream()
        .map(
            quest ->
                new GetQuestResponseDto(
                    quest.getQuestId(),
                    quest.getTitle(),
                    quest.getDeadline(),
                    quest.getIsComplete()))
        .toList();
  }

  public int getQuestCountsByGoalId(Long goalId) {
    return questRepository.getCountByGoalId(goalId);
  }

  public List<String> getRecommendedQuests(Long memberGoalId) {
    MemberGoal memberGoal = memberGoalService.findMemberGoal(memberGoalId);
    Set<Quest> myQuests = new HashSet<>(memberGoal.getQuests());

    List<MemberGoal> memberGoals =
        memberGoalService.findMemberGoalsByGoalId(memberGoal.getGoal().getGoalId()); // goals 조회
    Set<Quest> quests =
        memberGoals.stream()
            .map(MemberGoal::getQuests)
            .flatMap(List::stream)
            .collect(Collectors.toSet());

    return quests.stream().filter(quest -> !myQuests.contains(quest)).map(Quest::getTitle).toList();
  }
}
