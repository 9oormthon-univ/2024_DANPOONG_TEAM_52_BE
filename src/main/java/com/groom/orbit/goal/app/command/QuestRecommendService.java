package com.groom.orbit.goal.app.command;

import org.springframework.stereotype.Service;

import com.groom.orbit.ai.app.AiService;
import com.groom.orbit.goal.app.dto.response.RecommendQuestListResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestRecommendService {

  private final AiService aiService;

  public RecommendQuestListResponseDto recommendQuest(Long memberId) {
    return aiService.recommendQuest(memberId);
  }
}