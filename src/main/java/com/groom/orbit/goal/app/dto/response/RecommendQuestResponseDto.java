package com.groom.orbit.goal.app.dto.response;

public record RecommendQuestResponseDto(String title) {

  public static RecommendQuestResponseDto from(String title) {
    return new RecommendQuestResponseDto(title);
  }
}
