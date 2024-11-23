package com.groom.orbit.auth.app.dto;

import com.groom.orbit.config.security.oAuth.AuthToken;

public record LoginResponseDto(AuthToken authToken, String nickname) {

  public static LoginResponseDto fromLogin(AuthToken authToken, String nickname) {
    return new LoginResponseDto(authToken, nickname);
  }
}
