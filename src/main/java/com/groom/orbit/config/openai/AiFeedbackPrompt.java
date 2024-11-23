package com.groom.orbit.config.openai;

public class AiFeedbackPrompt {
  private static final String SYSTEM_PROMPT =
      "내 관심 직무 분야는 {job}인데 이에 관한 내 활동사항들이야. 이 항목 대해 피드백을 해줘";
  private static final String USER_PROMPT =
      """
              {academy}

              이건 내 학력이고,

              {career}

              이건 내 경력이야.

              {qualification}

              이건 내 자격, 어학, 수상에 관련된 내용들이고

              {experience}

              이건 내 경험, 활동, 교육에 관한 내용들이야.

              {etc}

              이건 기타사항이야
              """;

  public static String getSystemPrompt(String job) {
    return SYSTEM_PROMPT.replace("{job}", job);
  }

  public static String getUserPrompt(
      String academy, String career, String qualification, String experience, String etc) {
    USER_PROMPT.replace("{academy}", academy);
    USER_PROMPT.replace("{career}", career);
    USER_PROMPT.replace("{qualification}", qualification);
    USER_PROMPT.replace("{experience}", experience);
    USER_PROMPT.replace("{etc}", etc);
    return USER_PROMPT;
  }
}
