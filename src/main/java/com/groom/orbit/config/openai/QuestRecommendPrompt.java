package com.groom.orbit.config.openai;

public class QuestRecommendPrompt {

  private static final String SYSTEM_PROMPT = "내 관심 직무는 {job}고 내 폭표는 {goal}야";

  private static final String USER_PROMPT =
      """

            {questList}

            이건 같은 목표를 갖고 있는 사람들이 했던 퀘스트인데 어떤거를 하면 좋을까?

            하나만 추천해줘. 저 중 하나만 골라서 이야기해줘 딱 저 중 하나만 이야기하면 되

            답변의 예시로는 "데이터 분석 참여"야
            이런 식으로 답변만 이야기해

            """;

  public static String getSystemPrompt(String job, String goal) {
    SYSTEM_PROMPT.replace("{job}", job);
    SYSTEM_PROMPT.replace("{goal}", goal);
    return SYSTEM_PROMPT;
  }

  public static String getUserPrompt(String questList) {
    return USER_PROMPT.replace("{questList}", questList);
  }
}
