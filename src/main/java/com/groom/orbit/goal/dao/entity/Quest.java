package com.groom.orbit.goal.dao.entity;

import com.groom.orbit.common.dao.entity.BaseTimeEntity;
import com.groom.orbit.member.dao.jpa.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quest")
public class Quest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long questId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isComplete = false;

    private java.sql.Timestamp deadline;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false, columnDefinition = "false")
    private Boolean isNotificationSend = false;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;
}

