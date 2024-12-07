package com.groom.orbit.fcm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.groom.orbit.goal.dao.QuestRepository;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.goal.dao.entity.Quest;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestFcmService {

  private final QuestRepository questRepository;
  private final FcmService fcmService;
  private final MemberRepository memberRepository;

  private final LocalDate today = LocalDate.now();

  private String TITLE = "안녕하세요!, 저는 벼리에요!";
  private String NOT_QUEST_TODAY_TITLE = "오르빛";

  @Scheduled(cron = "0 0 9 * * ?")
  public void sendOverDeadlineNotificationAtNine() {

    List<Quest> notCompleteAndOverDeadlineQuestList =
        questRepository.findNotCompleteAndOverDeadlineQuest(today);

    Member member = notCompleteAndOverDeadlineQuestList.get(0).getMemberGoal().getMember();

    for (Quest quest : notCompleteAndOverDeadlineQuestList) {

      MemberGoal memberGoal = quest.getMemberGoal();

      String fcmToken = memberGoal.getMember().getFcmToken();

      Long date = ChronoUnit.DAYS.between(quest.getDeadline(), LocalDate.now());

      if (fcmToken != null && (date == 1 || date == 2 || date == 3 || date == 7)) {

        String body = member.getNickname() + "님, 마감일로부터" + date + "일 지났어요! 아직 늦지 않았으니 지금 달성하러 가봐요!";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(member.getFcmToken(), TITLE, body);

        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }

  @Scheduled(cron = "0 0 15   * * ?")
  public void sendOverDeadlineNotificationAtThree() {

    List<Quest> notCompleteAndOverDeadlineQuestList =
        questRepository.findNotCompleteAndOverDeadlineQuest(today);

    Member member = notCompleteAndOverDeadlineQuestList.get(0).getMemberGoal().getMember();

    for (Quest quest : notCompleteAndOverDeadlineQuestList) {

      MemberGoal memberGoal = quest.getMemberGoal();

      String fcmToken = memberGoal.getMember().getFcmToken();

      Long date = ChronoUnit.DAYS.between(quest.getDeadline(), LocalDate.now());

      if (fcmToken != null && (date == 1 || date == 2 || date == 3 || date == 7)) {

        String body =
            member.getNickname() + "님, 마감일로부터" + date + "일 지났어요! 하지만 괜찮습니다! 지금이라도 빠르게 완료해보는건 어떨까요?";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(member.getFcmToken(), TITLE, body);

        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }

  @Scheduled(cron = "0 0 9 * * ?")
  public void sendNotDeadlineQuestTodayNotificationAtNine() {

    List<Member> memberList = memberRepository.findAll();

    for (Member member : memberList) {

      List<Quest> todayQuestList = questRepository.findNotDeadlineQuestToday(today, member);

      if (todayQuestList.isEmpty()) {
        String fcmToken = member.getFcmToken();
        String body = "오르빛은 언제나 당신의 든든한 지원군입니다! 하나씩 퀘스트를 달성해볼까요?";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(fcmToken, NOT_QUEST_TODAY_TITLE, body);
        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }

  @Scheduled(cron = "0 0 15 * * ?")
  public void sendNotDeadlineQuestTodayNotificationAtThree() {

    List<Member> memberList = memberRepository.findAll();

    for (Member member : memberList) {

      List<Quest> todayQuestList = questRepository.findNotDeadlineQuestToday(today, member);

      if (todayQuestList.isEmpty()) {
        String fcmToken = member.getFcmToken();
        String body = "오늘 시작한 도전이 곧 큰 변화를 만들어 낼 거에요! 오르빛이 함께 할게요!";
        FcmSendRequestDto requestDto = FcmSendRequestDto.of(fcmToken, NOT_QUEST_TODAY_TITLE, body);
        try {
          fcmService.sendMessageTo(requestDto);
        } catch (IOException e) {
          System.err.println("FCM 알림 전송 실패: " + e.getMessage());
        }
      }
    }
  }
}
