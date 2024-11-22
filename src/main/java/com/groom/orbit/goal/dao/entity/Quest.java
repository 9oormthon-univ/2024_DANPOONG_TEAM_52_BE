package com.groom.orbit.goal.dao.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.groom.orbit.common.dao.entity.BaseTimeEntity;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.Getter;

@Entity
@Getter
@DynamicUpdate
@Table(name = "quest")
public class Quest extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "quest_id")
  private Long questId;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false)
  private Boolean isComplete = false;

  private LocalDate deadline;

  @Column(nullable = false)
  private Integer sequence;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "goal_id", nullable = false)
  private Goal goal;

  public static Quest create(
      String title, MemberGoal memberGoal, LocalDate deadline, int newSequence) {
    Quest quest = new Quest();
    quest.title = title;
    quest.member = memberGoal.getMember();
    quest.goal = memberGoal.getGoal();
    quest.sequence = newSequence;
    quest.deadline = deadline;

    return quest;
  }

  public void decreaseSequence() {
    this.sequence -= 1;
    if (this.sequence <= 0) {
      throw new CommonException(ErrorCode.INVALID_STATE);
    }
  }

  public void validateMember(Long memberId) {
    this.member.validateId(memberId);
  }

  public void update(String title, Boolean isComplete, LocalDate deadline) {
    if (title != null && !title.equals(this.title)) {
      this.title = title;
    }
    if (isComplete != null && !isComplete.equals(this.isComplete)) {
      this.isComplete = isComplete;
    }
    if (deadline != null && !deadline.equals(this.deadline)) {
      this.deadline = deadline;
    }
  }
}
