package com.groom.orbit.fcm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginFcmService {

  private final MemberRepository memberRepository;
  private final FcmService fcmService;

  private final String TITLE = "오르빛";

  @Scheduled(cron = "0 0 9 * * ?")
  public void sendNotDeadlineQuestTodayNotificationAtNine() {

    List<Member> memberList = memberRepository.findAll();

    for (Member member : memberList) {

      Long lastLogin = ChronoUnit.DAYS.between(member.getLastLogin(), LocalDate.now());

      if (lastLogin.equals(7L)) {
        String fcmToken = member.getFcmToken();
        String body = member.getNickname() + "님 저희 다시 시작해봐요! 오르빛은 항상 당신을 응원하고 있어요!";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(fcmToken, TITLE, body);
        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }

  @Scheduled(cron = "0 0 9 * * ?")
  public void sendNotDeadlineQuestTodayNotificationAtThree() {

    List<Member> memberList = memberRepository.findAll();

    for (Member member : memberList) {

      Long lastLogin = ChronoUnit.DAYS.between(member.getLastLogin(), LocalDate.now());

      if (lastLogin.equals(7L)) {
        String fcmToken = member.getFcmToken();
        String body = "들어오지 않은지 일주일이 지났어요! 저희는 " + member.getNickname() + "님을 기다리고 있어요!";
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
