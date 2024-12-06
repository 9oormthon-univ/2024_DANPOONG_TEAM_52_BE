package com.groom.orbit.goal.controller.command;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.goal.app.command.QuestCommandService;
import com.groom.orbit.goal.app.command.QuestUpdateService;
import com.groom.orbit.goal.app.dto.request.CreateQuestRequestDto;
import com.groom.orbit.goal.app.dto.request.UpdateQuestRequestDto;
import com.groom.orbit.goal.app.dto.request.UpdateQuestSequenceRequestDto;
import com.groom.orbit.goal.app.dto.response.CreateQuestResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest")
public class QuestCommandController {

  private final QuestCommandService questCommandService;
  private final QuestUpdateService questUpdateService;

  @PostMapping
  public ResponseDto<CreateQuestResponse> createQuest(
      @AuthMember Long memberId, @RequestBody CreateQuestRequestDto dto) {
    return ResponseDto.created(questCommandService.createQuest(memberId, dto));
  }

  @PatchMapping("/{quest_id}")
  public ResponseDto<CommonSuccessDto> updateQuest(
      @AuthMember Long memberId,
      @PathVariable("quest_id") Long questId,
      @RequestBody UpdateQuestRequestDto dto) {
    return ResponseDto.ok(questUpdateService.updateQuest(memberId, questId, dto));
  }

  @PatchMapping
  public ResponseDto<CommonSuccessDto> updateQuestsSequence(
      @AuthMember Long memberId, @RequestBody List<UpdateQuestSequenceRequestDto> dtos) {
    return ResponseDto.ok(questUpdateService.updateQuestSequence(memberId, dtos));
  }

  @DeleteMapping("/{quest_id}")
  public ResponseDto<CommonSuccessDto> deleteQuest(
      @AuthMember Long memberId, @PathVariable("quest_id") Long questId) {
    return ResponseDto.ok(questCommandService.deleteOneQuest(memberId, questId));
  }
}
