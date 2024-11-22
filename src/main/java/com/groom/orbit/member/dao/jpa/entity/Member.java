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

import com.groom.orbit.job.dao.jpa.entity.InterestJob;
import com.groom.orbit.job.dao.jpa.entity.Job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "member")
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(name = "nickname", length = 100)
  private String nickname;

  @Column(name = "image_url")
  private String imageUrl = "";

  @Column(name = "known_prompt", length = 1000)
  private String knownPrompt = "";

  @Column(name = "help_prompt", length = 1000)
  private String helpPrompt = "";

  @Column(name = "is_notification")
  private Boolean isNotification = false;

  @Column(name = "is_profile")
  private Boolean isProfile = false;

  @Setter
  @Column(name = "ai_feedback", length = 50000)
  private String aiFeedback = "";

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<InterestJob> interestJobs = new ArrayList<>();

  public void addInterestJobs(List<Job> jobs) {
    List<InterestJob> interestJobs =
        jobs.stream().map(job -> InterestJob.create(this, job)).toList();
    this.interestJobs.addAll(interestJobs);
  }
}
