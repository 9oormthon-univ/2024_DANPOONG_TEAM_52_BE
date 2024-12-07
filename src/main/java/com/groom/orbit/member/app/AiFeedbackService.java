package com.groom.orbit.member.app;

import java.util.List;

import org.springframework.stereotype.Service;

import com.groom.orbit.ai.app.AiService;
import com.groom.orbit.job.app.InterestJobService;
import com.groom.orbit.job.app.dto.JobDetailResponseDto;
import com.groom.orbit.member.app.dto.response.GetFeedbackResponseDto;
import com.groom.orbit.resume.app.ResumeQueryService;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiFeedbackService {

  private final AiService aiService;
  private final InterestJobService interestJobService;
  private final ResumeQueryService resumeQueryService;

  public GetFeedbackResponseDto getFeedback(Long memberId) {
    String interestJobs = getInterestJobs(memberId);
    GetResumeResponseDto dto = resumeQueryService.getMyResume(memberId);

    return aiService.getMemberFeedback(interestJobs, dto);
  }

  private String getInterestJobs(Long memberId) {
    List<String> jobs =
        interestJobService.findJobsByMemberId(memberId).stream()
            .map(JobDetailResponseDto::name)
            .toList();
    return String.join(",", jobs);
  }
}
