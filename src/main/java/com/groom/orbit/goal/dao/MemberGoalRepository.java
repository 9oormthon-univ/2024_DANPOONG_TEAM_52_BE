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
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " join fetch mg.member m"
          + " where mg.member.id=:member_id and mg.goal.goalId=:goal_id")
  Optional<MemberGoal> findByMemberIdAndGoalId(
      @Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  //  @Modifying
  //  @Query("UPDATE MemberGoal mg SET mg.goal.goalId = :goalId WHERE mg.memberGoalId =
  // :memberGoalId")
  //  void updateGoalId(Long goalId);
}
