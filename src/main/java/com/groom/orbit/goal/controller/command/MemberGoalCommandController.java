package com.groom.orbit.goal.controller.command;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.dto.ResponseDto;
import com.groom.orbit.config.openai.GoalRecommendRequestDto;
import com.groom.orbit.config.openai.GoalRecommendResponseDto;
import com.groom.orbit.config.openai.OpenAiClient;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.app.command.GoalCommandService;
import com.groom.orbit.goal.app.dto.request.MemberGoalRequestDto;
import com.groom.orbit.goal.app.dto.response.RecommendGoalResponseDto;
import com.groom.orbit.goal.dao.GoalRepository;
import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goal")
public class MemberGoalCommandController {

  private final MemberGoalService memberGoalService;
  private final GoalCommandService goalCommandService;
  private final MemberQueryService memberQueryService;
  private final OpenAiClient openAiClient;
  private final GoalRepository goalRepository;

  @DeleteMapping("/{goal_id}")
  public ResponseDto<CommonSuccessDto> deleteMemberGoal(
      @AuthMember Long memberId, @PathVariable("goal_id") Long goalId) {
    return ResponseDto.ok(memberGoalService.deleteGoal(memberId, goalId));
  }

  @PutMapping
  public ResponseDto<CommonSuccessDto> createMemberGoal(
      @AuthMember Long memberId, @RequestBody MemberGoalRequestDto dto) {
    return ResponseDto.created(memberGoalService.createGoal(memberId, dto));
  }

  @PatchMapping("/{goal_id}")
  public ResponseDto<CommonSuccessDto> updateMemberGoal(
      @AuthMember Long memberId,
      @PathVariable("goal_id") Long goalId,
      @RequestBody MemberGoalRequestDto dto) {
    return ResponseDto.ok(memberGoalService.updateGoal(memberId, goalId, dto));
  }

  @PostMapping("/recommend")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ResponseDto<RecommendGoalResponseDto> creatRecommendGoal(@AuthMember Long memberId) {
    Member member = memberQueryService.findMember(memberId);
    String job = member.getInterestJobs().get(0).getJob().getName();

    List<String> goal = memberGoalService.findMemberGoalNotCompleted(memberId);

    String goalList = String.join(", ", goal);

    GoalRecommendResponseDto goalRecommendResponseDto =
        openAiClient.createGoalRecommend(GoalRecommendRequestDto.from(job, goalList));

    Goal newGoal =
        Goal.create(
            goalRecommendResponseDto.getAnswer().split(",")[0],
            goalRecommendResponseDto.getAnswer().split(",")[1]);

    goalRepository.save(newGoal);
    return ResponseDto.ok(
        RecommendGoalResponseDto.from(newGoal.getTitle(), newGoal.getCategory().toString()));
  }
}
