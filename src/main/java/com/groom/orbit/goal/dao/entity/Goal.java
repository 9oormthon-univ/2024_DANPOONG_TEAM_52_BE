package com.groom.orbit.goal.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

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
    private String category;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isComplete;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer count;

    public static Goal create(String title, String category) {
        Goal goal = new Goal();
        goal.title = title;
        goal.category = category;

        return goal;
    }
}

