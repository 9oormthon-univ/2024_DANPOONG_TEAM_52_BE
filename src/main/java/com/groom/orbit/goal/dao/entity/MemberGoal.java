package com.groom.orbit.goal.dao.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import com.groom.orbit.common.dao.entity.BaseTimeEntity;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@DynamicUpdate
@Table(name = "member_goal")
public class MemberGoal extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_goal_id")
  private Long memberGoalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "goal_id")
  private Goal goal;

  @ColumnDefault("false")
  @Column(nullable = false)
  private Boolean isComplete = false;

  @Setter
  @Column(name = "sequence")
  private Integer sequence;

  @OneToMany(mappedBy = "memberGoal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Quest> quests = new ArrayList<>();

  public static MemberGoal create(Member member, Goal goal) {
    MemberGoal memberGoal = new MemberGoal();
    memberGoal.member = member;
    memberGoal.goal = goal;

    return memberGoal;
  }

  public String getTitle() {
    return this.goal.getTitle();
  }

  public void updateGoal(Goal goal) {
    this.goal.decreaseCount();
    goal.increaseCount();
    this.goal = goal;
  }

  public void validateMember(Long memberId) {
    this.member.validateId(memberId);
  }
}
