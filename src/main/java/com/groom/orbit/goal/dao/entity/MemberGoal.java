package com.groom.orbit.goal.dao.entity;

import java.time.LocalDateTime;
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
import com.groom.orbit.quest.dao.entity.Quest;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
  @Setter
  @Column(nullable = false)
  private Boolean isComplete = false;

  @Setter
  @Column(name = "sequence")
  private Integer sequence;

  @Setter
  @ColumnDefault("2000-12-31 00:00:00")
  @Column(name = "completed_date")
  private LocalDateTime completedDate = LocalDateTime.of(2000, 12, 31, 00, 00);

  @ColumnDefault("false")
  @Column(name = "is_resume")
  private Boolean isResume = false;

  @OneToMany(mappedBy = "memberGoal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Quest> quests = new ArrayList<>();

  public static MemberGoal create(Member member, Goal goal, int memberGoalSize) {
    MemberGoal memberGoal = new MemberGoal();
    memberGoal.member = member;
    memberGoal.goal = goal;
    memberGoal.sequence = memberGoalSize + 1;
    goal.increaseCount();

    return memberGoal;
  }

  public String getTitle() {
    return this.goal.getTitle();
  }

  public void update(Goal goal) {
    this.goal.decreaseCount();
    goal.increaseCount();
    this.goal = goal;
  }

  public void validateMember(Long memberId) {
    this.member.validateId(memberId);
  }

  public MemberGoal copyMemberGoal(Member member, Goal goal, Integer memberGoalLen) {
    return MemberGoal.builder()
        .member(member)
        .goal(goal)
        .isComplete(false)
        .sequence(memberGoalLen + 1)
        .isResume(false)
        .completedDate(this.completedDate)
        .build();
  }

  public void deleteResume() {
    this.isResume = false;
  }

  public void createResume() {
    this.isResume = true;
  }
}
