package com.groom.orbit.schedule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  @Query(
      "SELECT s FROM Schedule s WHERE YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month AND s.member.id = :memberId ORDER BY s.startDate ASC")
  List<Schedule> findAllByMonthAndMemberId(Long memberId, Integer month, Integer year);
}
