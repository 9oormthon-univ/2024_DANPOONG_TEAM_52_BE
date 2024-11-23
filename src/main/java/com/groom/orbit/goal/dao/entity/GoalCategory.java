package com.groom.orbit.goal.dao.entity;

import java.util.Arrays;
import java.util.List;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;

public enum GoalCategory {
  CERTIFICATION("자격·어학·수상"),
  EXPERIENCE("경험·활동·교육"),
  CAREER("경력"),
  ETC("기타");

  private String category;

  GoalCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }

  public static GoalCategory from(String category) {
    return Arrays.stream(GoalCategory.values())
        .filter(goalCategory -> goalCategory.getCategory().equals(category))
        .findFirst()
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_GOAL_CATEGORY));
  }

  public static List<String> getAll() {
    return Arrays.stream(GoalCategory.values()).map(GoalCategory::getCategory).toList();
  }
}
