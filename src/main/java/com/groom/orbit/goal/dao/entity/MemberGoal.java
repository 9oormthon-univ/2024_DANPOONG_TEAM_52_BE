package com.groom.orbit.goal.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.Getter;

@Entity
@Getter
@Table(name = "member_goal")
@IdClass(MemberGoalId.class)
public class MemberGoal {

  @Id
  @Column(name = "member_id") // 필드 명시
  private Long memberId; // 변경

  @Id
  @Column(name = "goal_id") // 필드 명시
  private Long goalId; // 변경

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", insertable = false, updatable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "goal_id", insertable = false, updatable = false)
  private Goal goal;

  @ColumnDefault("false")
  @Column(nullable = false)
  private Boolean isComplete;

  public static MemberGoal create(Member member, Goal goal) {
    MemberGoal memberGoal = new MemberGoal();
    memberGoal.member = member;
    memberGoal.goal = goal;

    return memberGoal;
  }

  public String getTitle() {
    return this.goal.getTitle();
  }
}
