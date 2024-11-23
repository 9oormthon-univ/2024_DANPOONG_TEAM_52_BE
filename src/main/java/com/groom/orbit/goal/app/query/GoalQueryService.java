package com.groom.orbit.goal.app.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.goal.app.dto.response.GetGoalCategoryResponseDto;
import com.groom.orbit.goal.dao.entity.GoalCategory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalQueryService {

  public GetGoalCategoryResponseDto getGoalCategory() {
    return new GetGoalCategoryResponseDto(GoalCategory.getAll());
  }
}
