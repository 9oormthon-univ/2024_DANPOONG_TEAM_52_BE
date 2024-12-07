package com.groom.orbit.fcm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groom.orbit.goal.dao.MemberGoalRepository;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoalFcmService {

  private final MemberRepository memberRepository;
  private final MemberGoalRepository memberGoalRepository;
  private final FcmService fcmService;

  private final String TITLE = "오르빛";

  @Scheduled(cron = "0 0 9 * * ?")
  public void sendNotDeadlineQuestTodayNotificationAtNine() {

    List<Member> memberList = memberRepository.findAll();

    LocalDateTime yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay();
    LocalDateTime yesterdayEnd = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);

    for (Member member : memberList) {

      List<MemberGoal> completeMemberGoalYesterdayList =
          memberGoalRepository.findCompleteMemberGoalYesterday(
              member, yesterdayStart, yesterdayEnd);

      if (!completeMemberGoalYesterdayList.isEmpty()) {
        String fcmToken = member.getFcmToken();
        String body = member.getNickname() + "님! 어제는 정말 멋진 별자리가 완성되었어요! 오늘도 힘내서 목표 달성에 한발자국 다가가봐요!";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(fcmToken, TITLE, body);
        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }
}
