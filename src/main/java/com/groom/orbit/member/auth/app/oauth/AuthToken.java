package com.groom.orbit.member.auth.app.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

  private String accessToken;
  private String refreshToken;

  public static AuthToken of(String accessToken, String refreshToken) {
    return new AuthToken(accessToken, refreshToken);
  }
}
