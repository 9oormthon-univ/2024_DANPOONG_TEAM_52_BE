package com.groom.orbit.config.security.oAuth;

import com.groom.orbit.config.security.kakao.KakaoReissueParams;

public interface OAuthApiClient {

  String requestAccessToken(OAuthLoginParams params);

  OAuthInfoResponse requestOauthInfo(String accessToken);

  String reissueAccessToken(KakaoReissueParams params);
}
