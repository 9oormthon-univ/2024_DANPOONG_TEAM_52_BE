package com.groom.orbit.goal.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {

  @Query(
      "select q from Quest q"
          + " join fetch q.member m"
          + " join fetch q.goal g"
          + " where q.member.id=:member_id and q.goal.goalId=:goal_id")
  List<Quest> findByMemberIdAndGoalId(
      @Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  @Query("select count(*) from Quest  q" + " join q.goal g" + " where g.goalId=:goal_id")
  int getCountByGoalId(@Param("goal_id") Long goalId);

  List<Quest> findByQuestIdIn(List<Long> ids);

  long countByGoal_GoalId(Long goalId);

  @Query("select count(*) from Quest q" + " where q.goal.goalId=:goal_id and q.isComplete=true")
  long countCompletedByGoal_GoalId(@Param("goal_id") Long goalId);

  @Query(
      "SELECT q FROM Quest q WHERE MONTH(q.deadline) = :month AND q.member.id = :memberId ORDER BY q.deadline ASC")
  List<Quest> findAllByMonthAndMemberId(Long memberId, Integer month);
}
