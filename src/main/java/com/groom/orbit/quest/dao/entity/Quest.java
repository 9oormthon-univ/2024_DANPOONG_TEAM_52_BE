package com.groom.orbit.quest.dao.entity;

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
import com.groom.orbit.goal.dao.entity.MemberGoal;
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

  private Integer sequence;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_goal_id")
  private MemberGoal memberGoal;

  public static Quest create(
      String title, MemberGoal memberGoal, LocalDate deadline, int newSequence) {
    Quest quest = copyQuest(title, memberGoal);
    quest.sequence = newSequence;
    quest.deadline = deadline;

    return quest;
  }

  public static Quest copyQuest(String title, MemberGoal memberGoal) {
    Quest quest = new Quest();
    quest.title = title;
    quest.memberGoal = memberGoal;
    memberGoal.getQuests().add(quest);

    return quest;
  }

  public void validateMember(Long memberId) {
    Member member = this.memberGoal.getMember();
    member.validateId(memberId);
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
