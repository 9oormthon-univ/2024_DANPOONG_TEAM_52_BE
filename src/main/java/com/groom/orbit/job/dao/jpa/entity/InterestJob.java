package com.groom.orbit.job.dao.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.groom.orbit.member.member.dao.jpa.entity.Member;

import lombok.Getter;

@Entity
@Getter
@Table(name = "interest_job")
@IdClass(InterestJobId.class)
public class InterestJob {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_id")
  private Job job;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(name = "member_id", insertable = false, updatable = false)
  private Long memberId;

  @Column(name = "job_id", insertable = false, updatable = false)
  private Long jobId;

  public static InterestJob create(Member member, Job job) {
    InterestJob entity = new InterestJob();
    entity.member = member;
    entity.job = job;

    return entity;
  }
}
