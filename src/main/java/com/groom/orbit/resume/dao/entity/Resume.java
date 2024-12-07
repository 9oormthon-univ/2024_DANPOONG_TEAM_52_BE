package com.groom.orbit.resume.dao.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.dto.ResumeRequestDto;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Table(name = "resume")
public class Resume {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "resume_id")
  private Long resumeId;

  @Column(name = "category", length = 10)
  @Enumerated(EnumType.STRING)
  private ResumeCategory resumeCategory;

  @Column(name = "title", length = 50)
  private String title;

  @Column(name = "content", length = 50)
  private String content;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Setter
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_goal_id")
  private MemberGoal memberGoal;

  public void update(ResumeRequestDto requestDto) {
    if (requestDto.resumeCategory() != null) {
      this.resumeCategory = requestDto.resumeCategory();
    }
    if (requestDto.title() != null) {
      this.title = requestDto.title();
    }
    if (requestDto.content() != null) {
      this.content = requestDto.content();
    }
    if (requestDto.startDate() != null) {
      this.startDate = requestDto.startDate();
    }
    if (requestDto.endDate() != null) {
      this.endDate = requestDto.endDate();
    }
  }

  public void createFromMemberGoal(MemberGoal memberGoal) {
    this.memberGoal = memberGoal;
    memberGoal.createResume();
  }

  public void validate(Long memberId) {
    this.member.validateId(memberId);
  }

  public void delete() {
    if (this.memberGoal == null) {
      return;
    }
    this.memberGoal.deleteResume();
  }
}
