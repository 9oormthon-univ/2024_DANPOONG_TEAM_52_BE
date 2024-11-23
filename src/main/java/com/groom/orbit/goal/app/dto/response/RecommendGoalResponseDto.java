package com.groom.orbit.goal.app.dto.response;

public record RecommendGoalResponseDto(String title, String category) {

  public static RecommendGoalResponseDto from(String title, String category) {
    return new RecommendGoalResponseDto(title, category);
  }
}
