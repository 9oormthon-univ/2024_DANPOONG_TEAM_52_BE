package com.groom.orbit.config.security;

import java.util.List;

public class SecurityConst {

  private SecurityConst() {}

  // jwt
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  // requestMatcher
  public static final List<String> ALLOWED_URLS = List.of("/api", "api/auth/kakao", "/kakao/**");
}
