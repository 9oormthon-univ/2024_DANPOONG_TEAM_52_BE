package com.groom.orbit.member.auth.app.oauth.kakao;

public class KakaoSecurityConst {

  private KakaoSecurityConst() {}

  public static final String KAKAO_AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";
  public static final String REQUEST_ACCESS_TOKEN_URI = "/oauth/token";
  public static final String REQUEST_OAUTH_INFO_URI = "/v2/user/me";
  public static final String KAKAO_REQUEST_PROPERTY_PROFILE = "[\"kakao_account.profile\"]";
  public static final String GRANT_TYPE = "grant_type";
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String PROPERTY_KEYS = "property_keys";
}
