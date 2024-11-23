package com.groom.orbit.auth.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.auth.app.dto.LoginResponseDto;
import com.groom.orbit.auth.dao.AuthMemberRepository;
import com.groom.orbit.auth.dao.entity.AuthMember;
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

  public LoginResponseDto login(OAuthLoginParams params) {
    OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
    AuthMember authMember = findOrCreateMember(oAuthInfoResponse);
    return LoginResponseDto.fromLogin(
        authTokensGenerator.generate(authMember.getId()), authMember.getKakaoNickname());
  }

  public AuthMember findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
    return memberRepository
        .findByKakaoNickname(oAuthInfoResponse.getKakaoNickname())
        .orElseGet(() -> newUser(oAuthInfoResponse));
  }

  private AuthMember newUser(OAuthInfoResponse oAuthInfoResponse) {
    AuthMember member =
        AuthMember.builder()
            .imageUrl(oAuthInfoResponse.getKakaoImage())
            .kakaoNickname(oAuthInfoResponse.getKakaoNickname())
            .nickname(oAuthInfoResponse.getKakaoNickname())
            .build();

    return memberRepository.save(member);
  }
}
