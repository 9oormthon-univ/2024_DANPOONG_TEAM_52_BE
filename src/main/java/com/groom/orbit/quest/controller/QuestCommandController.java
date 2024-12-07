package com.groom.orbit.quest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.quest.app.QuestCommandService;
import com.groom.orbit.quest.app.dto.request.CreateQuestRequestDto;
import com.groom.orbit.quest.app.dto.request.UpdateQuestRequestDto;
import com.groom.orbit.quest.app.dto.response.CreateQuestResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest")
public class QuestCommandController {

  private final QuestCommandService questCommandService;

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
    return ResponseDto.ok(questCommandService.updateQuest(memberId, questId, dto));
  }

  @DeleteMapping("/{quest_id}")
  public ResponseDto<CommonSuccessDto> deleteQuest(
      @AuthMember Long memberId, @PathVariable("quest_id") Long questId) {
    return ResponseDto.ok(questCommandService.deleteOneQuest(memberId, questId));
  }
}
