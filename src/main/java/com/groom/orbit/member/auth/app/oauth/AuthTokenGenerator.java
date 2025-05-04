package com.groom.orbit.member.auth.app.oauth;

import org.springframework.stereotype.Component;

import com.groom.orbit.member.auth.app.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {

  private static final String BEARER = "Bearer";

  private final JwtTokenProvider jwtTokenProvider;

  public AuthToken generate(Long memberId) {
    String accessToken = jwtTokenProvider.generateAccessToken(memberId);
    String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);

    return new AuthToken(accessToken, refreshToken);
  }
}
