package com.groom.orbit.goal.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.GoalCategory;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.member.dao.jpa.entity.Member;

public interface MemberGoalRepository extends JpaRepository<MemberGoal, Long> {

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.member m"
          + " join fetch mg.goal g"
          + " where m.id=:member_id and mg.memberGoalId=:goal_id")
  Optional<MemberGoal> findById(@Param("member_id") Long memberId, @Param("goal_id") Long goalId);

  @Query(
      """
            select mg from MemberGoal mg
            join fetch mg.member m
            where m.id=:member_id and mg.isComplete=:is_complete
            order by mg.sequence asc
            """)
  List<MemberGoal> findByMemberIdAndIsComplete(
      @Param("member_id") Long memberId, @Param("is_complete") Boolean isComplete);

  @Query("select mg from MemberGoal mg" + " join fetch mg.goal g" + " where g.goalId=:goal_id")
  List<MemberGoal> findAllByGoalId(@Param("goal_id") Long goalId);

  List<MemberGoal> findAllByMemberIdAndIsCompleteFalse(Long memberId);

  List<MemberGoal> findAllByMemberIdAndIsCompleteFalseAndSequenceGreaterThan(
      Long memberId, Long sequence);

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " join fetch mg.quests q"
          + " where g.goalId=:goal_id")
  List<MemberGoal> findAllWithQuestsByGoalId(@Param("goal_id") Long goalId);

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " where mg.member.id in :member_ids"
          + " and (:category is null or g.category = :category)"
          + " group by g.goalId"
          + " order by g.count desc")
  Page<MemberGoal> findByMemberIdsAndCategoryCountDesc(
      @Param("member_ids") List<Long> memberIds,
      @Param("category") GoalCategory category,
      Pageable pageable);

  @Query(
      "select mg from MemberGoal mg"
          + " join fetch mg.goal g"
          + " where mg.member.id in :member_ids"
          + " and (:category is null or g.category = :category)"
          + " group by g.goalId"
          + " order by g.createdAt desc")
  Page<MemberGoal> findByMemberIdsAndCategoryCreatedAtDesc(
      @Param("member_ids") List<Long> memberIds,
      @Param("category") GoalCategory category,
      Pageable pageable);

  @Query(
      """
        select mg
        from MemberGoal mg
        where mg.member = :member and
              mg.isComplete = true and
              mg.completedDate between :yesterdayStart and :yesterdayEnd
              """)
  List<MemberGoal> findCompleteMemberGoalYesterday(
      @Param("member") Member member,
      @Param("yesterdayStart") LocalDateTime yesterdayStart,
      @Param("yesterdayEnd") LocalDateTime yesterdayEnd);
}
