package com.groom.orbit.resume.app.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groom.orbit.resume.dao.entity.Resume;
import com.groom.orbit.resume.dao.entity.ResumeCategory;

public record ResumeResponseDto(
    ResumeCategory resumeCategory,
    String title,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

  public static ResumeResponseDto fromResume(Resume resume) {
    return new ResumeResponseDto(
        resume.getResumeCategory(),
        resume.getTitle(),
        resume.getContent(),
        resume.getStartDate(),
        resume.getEndDate());
  }
}
