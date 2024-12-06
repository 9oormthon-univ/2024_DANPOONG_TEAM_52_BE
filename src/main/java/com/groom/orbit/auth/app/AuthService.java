package com.groom.orbit.auth.app;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.ai.app.VectorService;
import com.groom.orbit.ai.app.dto.CreateVectorDto;
import com.groom.orbit.auth.app.dto.LoginResponseDto;
import com.groom.orbit.auth.dao.AuthMemberRepository;
import com.groom.orbit.auth.dao.entity.AuthMember;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.config.security.JwtTokenProvider;
import com.groom.orbit.config.security.kakao.KakaoReissueParams;
import com.groom.orbit.config.security.oAuth.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final AuthMemberRepository memberRepository;
  private final AuthTokenGenerator authTokensGenerator;
  private final RequestOAuthInfoService requestOAuthInfoService;
  private final VectorService vectorService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.refresh-token-validity}")
  private Long refreshTokenValidityMilliseconds;

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

    AuthMember savedMember = memberRepository.save(member);
    saveVector(savedMember);
    return savedMember;
  }

  private void saveVector(AuthMember member) {
    CreateVectorDto vectorDto = CreateVectorDto.builder().memberId(member.getId()).build();
    vectorService.save(vectorDto);
  }

  public AuthToken reissue(KakaoReissueParams params) {

    String refreshToken = params.getRefreshToken();

    Long memberId = jwtTokenProvider.parseRefreshToken(refreshToken);

    if (!refreshToken.equals(redisTemplate.opsForValue().get(memberId.toString()))) {
      throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
    }

    AuthMember member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

    String newAccessToken = jwtTokenProvider.generateAccessToken(member.getId());

    String newRefreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

    redisTemplate
        .opsForValue()
        .set(
            member.getId().toString(),
            newRefreshToken,
            refreshTokenValidityMilliseconds,
            TimeUnit.MILLISECONDS);

    return AuthToken.of(newAccessToken, newRefreshToken);
  }
}
