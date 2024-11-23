package com.groom.orbit.config.security.oAuth;

import org.springframework.stereotype.Component;

import com.groom.orbit.config.security.JwtTokenProvider;

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
