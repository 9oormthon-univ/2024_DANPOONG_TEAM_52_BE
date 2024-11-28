package com.groom.orbit.job.app;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.job.app.dto.JobDetailResponseDto;
import com.groom.orbit.job.dao.jpa.InterestJobRepository;
import com.groom.orbit.job.dao.jpa.entity.InterestJob;
import com.groom.orbit.job.dao.jpa.entity.Job;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestJobService {

  private final MemberQueryService memberQueryService;
  private final InterestJobRepository interestJobRepository;

  public List<JobDetailResponseDto> findJobsByMemberId(Long memberId) {
    List<Job> jobs =
        interestJobRepository.findAllByMemberId(memberId).stream()
            .map(InterestJob::getJob)
            .toList();

    return jobs.stream()
        .map(job -> new JobDetailResponseDto(job.getJobId(), job.getCategory(), job.getName()))
        .toList();
  }

  public void saveInterestJob(Long memberId, List<Job> jobs) {
    Member member = memberQueryService.findMember(memberId);
    member.addInterestJobs(jobs);
  }
}
