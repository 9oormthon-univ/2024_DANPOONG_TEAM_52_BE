package com.groom.orbit.goal.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;

@Entity
@Getter
@Table(name = "goal")
public class Goal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "goal_id")
  private Long goalId;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private GoalCategory category;

  @ColumnDefault("0")
  @Column(nullable = false)
  private Integer count;

  public static Goal create(String title, String category) {
    Goal goal = new Goal();
    goal.title = title;
    goal.category = GoalCategory.from(category);

    return goal;
  }
}
