package com.groom.orbit.member.dao.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.job.dao.jpa.entity.InterestJob;
import com.groom.orbit.job.dao.jpa.entity.Job;
import com.groom.orbit.member.app.dto.request.UpdateMemberRequestDto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "member")
@Table(name = "member")
@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(name = "nickname", length = 100)
  private String nickname;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @Column(name = "known_prompt", length = 1000)
  private String knownPrompt = "";

  @Column(name = "help_prompt", length = 1000)
  private String helpPrompt = "";

  @Column(name = "is_notification")
  private Boolean isNotification = false;

  @Column(name = "is_profile")
  private Boolean isResume = false;

  @Setter
  @Column(name = "ai_feedback", length = 50000, columnDefinition = "TEXT")
  private String aiFeedback = "";

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<InterestJob> interestJobs = new ArrayList<>();

  public void addInterestJobs(List<Job> jobs) {
    List<InterestJob> interestJobs =
        jobs.stream().map(job -> InterestJob.create(this, job)).toList();
    this.interestJobs.addAll(interestJobs);
  }

  public void updateMember(UpdateMemberRequestDto requestDto, String newProfileUrl) {
    this.nickname = requestDto.nickname();
    this.imageUrl = newProfileUrl;
    this.knownPrompt = requestDto.knownPrompt();
    this.helpPrompt = requestDto.helpPrompt();
    this.isNotification = requestDto.isNotification();
    this.isResume = requestDto.isProfile();
  }

  public void validateId(Long memberId) {
    if (!this.id.equals(memberId)) {
      throw new CommonException(ErrorCode.ACCESS_DENIED);
    }
  }
}
