package com.groom.orbit.fcm;

import lombok.Builder;

@Builder
public record Notification(String title, String body, String image) {

  public static Notification from(FcmSendRequestDto requestDto) {
    return Notification.builder()
        .title(requestDto.title())
        .body(requestDto.body())
        .image(
            "https://raw.githubusercontent.com/AlohaTime/aloha-react-frontend/refs/heads/main/public/images/refresh.svg")
        .build();
  }
}
