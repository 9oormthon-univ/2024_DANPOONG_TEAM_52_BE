package com.groom.orbit.goal.app.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.dto.response.QuestInfoResponseDto;
import com.groom.orbit.goal.dao.QuestRepository;
import com.groom.orbit.goal.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestQueryService {

  private final QuestRepository questRepository;

  public Quest findQuest(Long questId) {
    return questRepository
        .findById(questId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QUEST));
  }

  public List<Quest> findQuestsByMemberAndGoal(Long memberId, Long goalId) {
    return questRepository.findByMemberIdAndGoalId(memberId, goalId);
  }

  public List<QuestInfoResponseDto> findQuestsByGoalId(Long memberId, Long goalId) {
    List<Quest> quests = findQuestsByMemberAndGoal(memberId, goalId);

    return quests.stream()
        .map(
            quest ->
                new QuestInfoResponseDto(
                    quest.getQuestId(), quest.getTitle(), quest.getIsComplete()))
        .toList();
  }

  public int getQuestCountsByGoalId(Long goalId) {
    return questRepository.getCountByGoalId(goalId);
  }

  public List<Quest> findByQuestIdIn(List<Long> questIds) {
    return questRepository.findByQuestIdIn(questIds);
  }

  public long getTotalQuestCount(Long goalId) {
    return questRepository.countByGoal_GoalId(goalId);
  }

  public long getFinishQuestCount(Long goalId) {
    return questRepository.countCompletedByGoal_GoalId(goalId);
  }
}
