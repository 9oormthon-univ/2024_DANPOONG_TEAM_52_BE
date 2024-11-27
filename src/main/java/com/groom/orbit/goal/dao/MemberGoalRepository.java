package com.groom.orbit.goal.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.MemberGoal;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, Long> {

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.member m"
          + " join fetch mg.goal g"
          + " where m.id=:member_id and g.goalId=:goal_id")
  Optional<MemberGoal> findById(@Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " join fetch mg.member m"
          + " where mg.isComplete=:is_complete"
          + " and m=:member_id")
  List<MemberGoal> findByIsComplete(
      @Param("member_id") Long memberId, @Param("is_complete") Boolean isComplete);

  @Query(
      """
          select mg from MemberGoal mg
          join fetch mg.member m
          where m.id=:member_id and mg.isComplete=:is_complete
          order by mg.sequence asc
          """)
  List<MemberGoal> findByMemberIdAndIsComplete(
      @Param("member_id") Long memberId, @Param("is_complete") Boolean isComplete);

  @Query("select mg from MemberGoal mg" + " join fetch mg.member m" + " join fetch mg.goal g")
  List<MemberGoal> findNotCompletedByMemberId(Long memberId);

  @Query("select mg from MemberGoal mg" + " join fetch mg.goal g" + " where g.goalId=:goal_id")
  List<MemberGoal> findAllByGoalId(@Param("goal_id") Long goalId);

  List<MemberGoal> findAllByMemberIdAndIsCompleteFalse(Long memberId);

  List<MemberGoal> findAllByMemberIdAndIsCompleteFalseAndSequenceGreaterThan(
      Long memberId, Long sequence);
}
