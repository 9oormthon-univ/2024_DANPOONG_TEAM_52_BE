package com.groom.orbit.config.openai;

public class GoalRecommendPrompt {

  private static final String SYSTEM_PROMPT = "내 관심 직무는 {job}이야";

  private static final String USER_PROMPT =
      """

            {goalList}

            이건 사람들과 내가 해왔고 달성하기 위해 노력하는 목표인데 어떤 목표를 더 달성하면 좋을까?

            자격·어학·수상, 경험·활동·교육, 경력, 기타 이게 목표의 카테고리야

            저 카테고리 중 하나와 그에 맞는 카테고리를 ~하기라고 하나만 추천해줘. 대답은 저기 중 카테고리 하나와 ~하기라고만 해

            답변의 예시는 "자격·어학·수상,~하기"야
            예시처럼 답변해

            """;

  public static String getSystemPrompt(String job) {
    return SYSTEM_PROMPT.replace("{job}", job);
  }

  public static String getUserPrompt(String goalList) {
    return USER_PROMPT.replace("{goalList}", goalList);
  }
}
