package com.groom.orbit.goal.app.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.app.dto.request.CreateQuestRequestDto;
import com.groom.orbit.goal.app.query.QuestQueryService;
import com.groom.orbit.goal.dao.QuestRepository;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestCommandService {

  private final MemberGoalService memberGoalService;
  private final QuestQueryService questQueryService;
  private final QuestRepository questRepository;

  /** TODO join 최적화 */
  public CommonSuccessDto createQuest(Long memberId, CreateQuestRequestDto dto) {
    MemberGoal memberGoal = memberGoalService.findMemberGoal(memberId, dto.goalId());
    int newQuestSequence = questQueryService.getQuestCountsByGoalId(dto.goalId()) + 1;
    Quest quest = Quest.create(dto.title(), memberGoal, dto.deadline(), newQuestSequence);

    questRepository.save(quest);

    return CommonSuccessDto.fromEntity(true);
  }

  /** select 최적화 */
  public CommonSuccessDto deleteQuest(Long memberId, Long questId, Long goalId) {
    List<Quest> quests = questQueryService.findQuestsByMemberAndGoal(memberId, goalId);
    Quest removeQuest =
        quests.stream()
            .filter(q -> q.getQuestId().equals(questId))
            .findFirst()
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QUEST));
    Integer removeSequence = removeQuest.getSequence();
    updateSequence(quests, removeSequence);

    questRepository.delete(removeQuest);

    return CommonSuccessDto.fromEntity(true);
  }

  private static void updateSequence(List<Quest> quests, Integer removeSequence) {
    for (Quest quest : quests) {
      if (quest.getSequence() > removeSequence) {
        quest.decreaseSequence();
      }
    }
  }
}
