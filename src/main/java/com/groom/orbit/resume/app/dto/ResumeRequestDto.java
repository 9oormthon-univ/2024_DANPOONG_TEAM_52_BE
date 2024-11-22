package com.groom.orbit.resume.app.dto;

import java.util.Date;

import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.dao.entity.Resume;
import com.groom.orbit.resume.dao.entity.ResumeCategory;

public record ResumeRequestDto(
    ResumeCategory resumeCategory, String title, String content, Date startDate, Date endDate) {

  public Resume toResume(Member member) {
    return Resume.builder()
        .resumeCategory(this.resumeCategory)
        .title(this.title)
        .content(this.content)
        .startDate(this.startDate)
        .endDate(this.endDate)
        .member(member)
        .build();
  }
}