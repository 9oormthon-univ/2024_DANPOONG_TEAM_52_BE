package com.groom.orbit.quest.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.quest.dao.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {

  @Query(
      "select q from Quest q"
          + " join fetch q.memberGoal mg"
          + " join fetch mg.goal g"
          + " join fetch mg.member m"
          + " where m.id=:member_id and mg.memberGoalId=:goal_id")
  List<Quest> findByMemberIdAndMemberGoalId(
      @Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  @Query(
      "select count(*) from Quest q"
          + " join q.memberGoal mg"
          + " join mg.goal g"
          + " where g.goalId=:goal_id")
  int getCountByGoalId(@Param("goal_id") Long goalId);

  List<Quest> findByQuestIdIn(List<Long> ids);

  @Query(
      "SELECT q FROM Quest q join fetch q.memberGoal mg join fetch mg.member m WHERE YEAR(q.deadline) = :year AND MONTH(q.deadline) = :month AND q.deadline is not null AND m.id = :memberId ORDER BY q.deadline ASC")
  List<Quest> findAllByMonthAndMemberId(Long memberId, Integer month, Integer year);

  @Query(
      "select q from Quest q"
          + " join fetch q.memberGoal mg"
          + " where mg.memberGoalId=:member_goal_id")
  List<Quest> findByMemberGoalId(@Param("member_goal_id") Long memberGoalId);

  @Query(
      "SELECT q FROM Quest q WHERE q.deadline IS NOT NULL AND q.deadline <= :deadline AND q.isComplete = false ")
  List<Quest> findNotCompleteAndOverDeadlineQuest(@Param("deadline") LocalDate deadline);

  @Query(
      "select q from Quest q Where q.deadline is not null and q.deadline = :deadline and q.memberGoal.member = :member and q.isComplete = false")
  List<Quest> findNotDeadlineQuestToday(
      @Param("deadline") LocalDate deadline, @Param("member") Member member);
}
