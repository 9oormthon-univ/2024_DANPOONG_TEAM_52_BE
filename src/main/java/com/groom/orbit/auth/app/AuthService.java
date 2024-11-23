package com.groom.orbit.auth.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.auth.dao.AuthMemberRepository;
import com.groom.orbit.auth.dao.entity.AuthMember;
import com.groom.orbit.config.security.oAuth.AuthToken;
import com.groom.orbit.config.security.oAuth.AuthTokenGenerator;
import com.groom.orbit.config.security.oAuth.OAuthInfoResponse;
import com.groom.orbit.config.security.oAuth.OAuthLoginParams;
import com.groom.orbit.config.security.oAuth.RequestOAuthInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final AuthMemberRepository memberRepository;
  private final AuthTokenGenerator authTokensGenerator;
  private final RequestOAuthInfoService requestOAuthInfoService;

  public AuthToken login(OAuthLoginParams params) {
    OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
    Long userId = findOrCreateMember(oAuthInfoResponse);
    return authTokensGenerator.generate(userId);
  }

  public Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
    return memberRepository
        .findByKakaoNickname(oAuthInfoResponse.getKakaoNickname())
        .map(AuthMember::getId)
        .orElseGet(() -> newUser(oAuthInfoResponse));
  }

  private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
    AuthMember member =
        AuthMember.builder()
            .imageUrl(oAuthInfoResponse.getKakaoImage())
            .kakaoNickname(oAuthInfoResponse.getKakaoNickname())
            .nickname(oAuthInfoResponse.getKakaoNickname())
            .build();

    return memberRepository.save(member).getId();
  }
}
