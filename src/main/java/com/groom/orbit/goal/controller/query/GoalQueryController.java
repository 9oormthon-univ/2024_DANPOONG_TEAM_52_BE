package com.groom.orbit.goal.controller.query;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.goal.app.dto.response.GetGoalCategoryResponseDto;
import com.groom.orbit.goal.app.query.GoalQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goal")
public class GoalQueryController {

  private final GoalQueryService goalQueryService;

  @GetMapping("/categories")
  public ResponseDto<GetGoalCategoryResponseDto> getCategories() {
    return ResponseDto.ok(goalQueryService.getGoalCategory());
  }
}
