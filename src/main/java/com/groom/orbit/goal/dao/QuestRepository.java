package com.groom.orbit.goal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.Quest;

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
      "SELECT q FROM Quest q join fetch q.memberGoal mg join fetch mg.member m WHERE MONTH(q.deadline) = :month AND q.deadline is not null AND m.id = :memberId ORDER BY q.deadline ASC")
  List<Quest> findAllByMonthAndMemberId(Long memberId, Integer month);

  @Query(
      "select q from Quest q"
          + " join fetch q.memberGoal mg"
          + " where mg.memberGoalId=:member_goal_id")
  List<Quest> findByMemberGoalId(@Param("member_goal_id") Long memberGoalId);
}
