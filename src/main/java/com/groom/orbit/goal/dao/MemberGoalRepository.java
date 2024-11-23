package com.groom.orbit.goal.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.MemberGoalId;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, MemberGoalId> {

  @Query("select mg from MemberGoal mg" + " where mg.memberId=:member_id and mg.goalId=:goal_id")
  Optional<MemberGoal> findById(@Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " join fetch mg.member m"
          + " where mg.isComplete=:is_complete"
          + " and mg.memberId=:member_id")
  List<MemberGoal> findByIsComplete(
      @Param("member_id") Long memberId, @Param("is_complete") Boolean isComplete);
}
