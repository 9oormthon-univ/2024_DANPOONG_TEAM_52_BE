package com.groom.orbit.ai.app;

import com.groom.orbit.goal.app.dto.response.RecommendGoalListResponseDto;
import com.groom.orbit.member.app.dto.response.GetFeedbackResponseDto;
import com.groom.orbit.quest.app.dto.response.RecommendQuestListResponseDto;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;

public interface AiService {

  GetFeedbackResponseDto getMemberFeedback(String interestJobs, GetResumeResponseDto dto);

  RecommendGoalListResponseDto recommendGoal(Long memberId);

  RecommendQuestListResponseDto recommendQuest(Long memberId);
}
