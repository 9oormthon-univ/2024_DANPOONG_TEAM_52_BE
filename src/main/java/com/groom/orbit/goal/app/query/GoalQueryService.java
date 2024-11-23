package com.groom.orbit.goal.app.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.dto.response.GetGoalCategoryResponseDto;
import com.groom.orbit.goal.dao.GoalRepository;
import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.goal.dao.entity.GoalCategory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalQueryService {

  private final GoalRepository goalRepository;

  public Goal findGoal(Long goalId) {
    return goalRepository
        .findById(goalId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_GOAL));
  }

  public Optional<Goal> findGoalByTitleAndCategory(String title, String category) {

    return goalRepository.findByTitleAndCategory(title, GoalCategory.from(category));
  }

  public GetGoalCategoryResponseDto getGoalCategory() {
    return new GetGoalCategoryResponseDto(GoalCategory.getAll());
  }

  public List<Goal> findNotIn(List<Long> startIds) {
    return goalRepository.findNotIn(startIds);
  }
}
