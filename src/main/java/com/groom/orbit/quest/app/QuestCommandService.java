package com.groom.orbit.quest.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.ai.app.VectorService;
import com.groom.orbit.ai.app.dto.CreateVectorDto;
import com.groom.orbit.ai.app.dto.UpdateVectorQuestDto;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.quest.app.dto.request.CreateQuestRequestDto;
import com.groom.orbit.quest.app.dto.request.UpdateQuestRequestDto;
import com.groom.orbit.quest.app.dto.response.CreateQuestResponse;
import com.groom.orbit.quest.dao.QuestRepository;
import com.groom.orbit.quest.dao.entity.Quest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestCommandService {

  private final MemberGoalService memberGoalService;
  private final QuestQueryService questQueryService;
  private final QuestRepository questRepository;
  private final VectorService vectorService;

  /** TODO join 최적화 */
  public CreateQuestResponse createQuest(Long memberId, CreateQuestRequestDto dto) {
    MemberGoal memberGoal = memberGoalService.findMemberGoal(memberId, dto.goalId());
    memberGoal.validateMember(memberId);

    int newQuestSequence = questQueryService.getQuestCountsByGoalId(dto.goalId()) + 1;
    Quest quest = Quest.create(dto.title(), memberGoal, dto.deadline(), newQuestSequence);
    questRepository.save(quest);
    saveVector(memberId, dto);

    return CreateQuestResponse.fromEntity(quest);
  }

  public CommonSuccessDto deleteOneQuest(Long memberId, Long questId) {
    Quest quest = questQueryService.findQuest(questId);
    quest.validateMember(memberId);

    questRepository.delete(quest);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateQuest(Long memberId, Long questId, UpdateQuestRequestDto dto) {
    Quest quest = questQueryService.findQuest(questId);
    quest.validateMember(memberId);

    updateVector(memberId, dto, quest);
    quest.update(dto.title(), dto.isComplete(), dto.deadline());

    return new CommonSuccessDto(true);
  }

  private void saveVector(Long memberId, CreateQuestRequestDto dto) {
    CreateVectorDto vectorDto =
        CreateVectorDto.builder().memberId(memberId).quest(dto.title()).build();
    vectorService.save(vectorDto);
  }

  private void updateVector(Long memberId, UpdateQuestRequestDto dto, Quest quest) {
    UpdateVectorQuestDto updateDto =
        UpdateVectorQuestDto.builder()
            .memberId(memberId)
            .quest(quest.getTitle())
            .newQuest(dto.title())
            .build();
    vectorService.updateQuest(updateDto);
  }
}
