package com.groom.orbit.job.app;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.job.app.dto.JobDetailResponseDto;
import com.groom.orbit.job.app.dto.JobGroupingByCategoryResponseDto;
import com.groom.orbit.job.dao.jpa.JobRepository;
import com.groom.orbit.job.dao.jpa.entity.Job;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JobQueryService {

  private final JobRepository jobRepository;
  private final InterestJobService interestJobService;

  public JobGroupingByCategoryResponseDto findAllJobs() {
    List<Job> findJobs = jobRepository.findAll();
    Map<String, List<JobDetailResponseDto>> jobs =
        findJobs.stream()
            .sorted(Comparator.comparing(Job::getJobId))
            .collect(
                Collectors.groupingBy(
                    Job::getCategory,
                    LinkedHashMap::new,
                    Collectors.mapping(
                        job ->
                            new JobDetailResponseDto(
                                job.getJobId(), job.getCategory(), job.getName()),
                        Collectors.toList())));

    return new JobGroupingByCategoryResponseDto(jobs);
  }

  public List<JobDetailResponseDto> findJobsByUser(Long memberId) {
    return interestJobService.findJobsByMemberId(memberId);
  }

  public List<Job> findJobsByIds(List<Long> ids) {
    return jobRepository.findAllById(ids);
  }
}
