package com.groom.orbit.config.openai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "open-ai-client",
    url = "https://api.openai.com/v1/chat/completions",
    configuration = OpenAiClientConfig.class)
public interface OpenAiClient {
  @PostMapping()
  AiFeedbackResponseDto createAiFeedback(@RequestBody AiFeedbackRequestDto requestDto);

  @PostMapping()
  GoalRecommendResponseDto createGoalRecommend(@RequestBody GoalRecommendRequestDto requestDto);

  @PostMapping()
  QuestRecommendResponseDto createQuestRecommend(@RequestBody QuestRecommendRequestDto requestDto);
}
