package com.groom.orbit.config.security.oAuth;

import org.springframework.stereotype.Component;

import com.groom.orbit.config.security.kakao.KakaoReissueParams;

@Component
public class RequestOAuthInfoService {

  private final OAuthApiClient client;

  public RequestOAuthInfoService(OAuthApiClient client) {
    this.client = client;
  }

  public OAuthInfoResponse request(OAuthLoginParams params) {
    String accessToken = client.requestAccessToken(params);
    return client.requestOauthInfo(accessToken);
  }

  public OAuthInfoResponse reissue(KakaoReissueParams params) {
    String accessToken = client.reissueAccessToken(params);
    return client.requestOauthInfo(accessToken);
  }
}
