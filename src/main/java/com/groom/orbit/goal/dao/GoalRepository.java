package com.groom.orbit.goal.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groom.orbit.goal.dao.entity.Goal;
import com.groom.orbit.goal.dao.entity.GoalCategory;

public interface GoalRepository extends JpaRepository<Goal, Long> {

  Optional<Goal> findByTitleAndCategory(String title, GoalCategory category);

  @Query(
      "select g from Goal g" + " where g.goalId not in :start_ids order by g.count desc limit 30")
  List<Goal> findNotIn(@Param("start_ids") List<Long> startIds);
}
