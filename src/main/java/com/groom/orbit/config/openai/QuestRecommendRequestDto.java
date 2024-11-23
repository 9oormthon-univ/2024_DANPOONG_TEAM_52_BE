package com.groom.orbit.config.openai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class QuestRecommendRequestDto {

  private String model = "gpt-4o-mini";

  private List<Message> messages;

  public QuestRecommendRequestDto(List<Message> messages) {
    this.messages = messages;
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Message {
    private Role role;
    private List<Content> content;
  }

  @RequiredArgsConstructor
  @Getter
  public enum Role {
    USER("user"),
    SYSTEM("system");
    @JsonValue private final String value;
  }

  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Content {
    private Type type;

    private String text;

    public static Content textContent(String text) {
      return new Content(Type.TEXT, text);
    }
  }

  @RequiredArgsConstructor
  @Getter
  public enum Type {
    TEXT("text");
    @JsonValue private final String value;
  }

  public static QuestRecommendRequestDto from(String job, String goal, String questList) {
    return new QuestRecommendRequestDto(createMessage(job, goal, questList));
  }

  private static List<Message> createMessage(String job, String goal, String questList) {
    return List.of(
        new Message(Role.SYSTEM, createSystemContents(job, goal)),
        new Message(Role.USER, createContents(questList)));
  }

  private static List<Content> createSystemContents(String job, String goal) {
    return List.of(Content.textContent(QuestRecommendPrompt.getSystemPrompt(job, goal)));
  }

  private static List<Content> createContents(String questList) {
    return List.of(Content.textContent(QuestRecommendPrompt.getUserPrompt(questList)));
  }
}
