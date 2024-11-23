package com.groom.orbit.goal.app.command;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.dto.request.UpdateQuestRequestDto;
import com.groom.orbit.goal.app.dto.request.UpdateQuestSequenceRequestDto;
import com.groom.orbit.goal.app.query.QuestQueryService;
import com.groom.orbit.goal.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestUpdateService {

  private final QuestQueryService questQueryService;

  public CommonSuccessDto updateQuest(Long memberId, Long questId, UpdateQuestRequestDto dto) {
    Quest quest = questQueryService.findQuest(questId);
    quest.validateMember(memberId);

    quest.update(dto.title(), dto.isComplete(), dto.deadline());

    return new CommonSuccessDto(true);
  }

  public CommonSuccessDto updateQuestSequence(
      Long memberId, List<UpdateQuestSequenceRequestDto> dtos) {
    List<Long> questIds = dtos.stream().map(UpdateQuestSequenceRequestDto::questId).toList();
    List<Quest> sortedQuest =
        questQueryService.findByQuestIdIn(questIds).stream()
            .sorted(Comparator.comparingLong(Quest::getQuestId))
            .toList();

    validateQuests(memberId, sortedQuest);

    for (UpdateQuestSequenceRequestDto dto : dtos) {
      Quest findQuest = findQuestInSortedByQuestId(sortedQuest, dto.questId());
      findQuest.updateSequence(dto.sequence());
    }

    return CommonSuccessDto.fromEntity(true);
  }

  private static void validateQuests(Long memberId, List<Quest> sortedQuest) {
    sortedQuest.forEach(quest -> quest.validateMember(memberId));
  }

  private Quest findQuestInSortedByQuestId(List<Quest> quests, Long questId) {
    int left = 0, right = quests.size() - 1;
    while (left <= right) {
      int mid = left + (right - left) / 2;
      Quest quest = quests.get(mid);
      if (quest.compareWithId(questId) == 0) {
        return quest;
      } else if (quest.compareWithId(questId) > 0) {
        right = mid - 1;
      } else {
        left = mid + 1;
      }
    }

    throw new CommonException(ErrorCode.NOT_FOUND_QUEST);
  }
}
