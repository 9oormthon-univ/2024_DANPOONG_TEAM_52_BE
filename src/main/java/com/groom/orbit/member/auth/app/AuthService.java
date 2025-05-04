package com.groom.orbit.member.auth.app;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.ai.app.VectorService;
import com.groom.orbit.ai.app.dto.CreateVectorDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.auth.app.dto.LoginResponseDto;
import com.groom.orbit.member.auth.app.oauth.*;
import com.groom.orbit.member.auth.app.oauth.kakao.KakaoReissueParams;
import com.groom.orbit.member.auth.app.provider.JwtTokenProvider;
import com.groom.orbit.member.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final MemberRepository memberRepository;
  private final AuthTokenGenerator authTokensGenerator;
  private final RequestOAuthInfoService requestOAuthInfoService;
  private final VectorService vectorService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.refresh-token-validity}")
  private Long refreshTokenValidityMilliseconds;

  public LoginResponseDto login(OAuthLoginParams params) {
    OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
    Member authMember = findOrCreateMember(oAuthInfoResponse);
    authMember.setLastLogin(LocalDate.now());
    return LoginResponseDto.fromLogin(
        authTokensGenerator.generate(authMember.getId()), authMember.getKakaoNickname());
  }

  public Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
    return memberRepository
        .findByKakaoNickname(oAuthInfoResponse.getKakaoNickname())
        .orElseGet(() -> newUser(oAuthInfoResponse));
  }

  private Member newUser(OAuthInfoResponse oAuthInfoResponse) {
    Member member =
        Member.builder()
            .imageUrl(oAuthInfoResponse.getKakaoImage())
            .kakaoNickname(oAuthInfoResponse.getKakaoNickname())
            .nickname(oAuthInfoResponse.getKakaoNickname())
            .build();

    Member savedMember = memberRepository.save(member);
    saveVector(savedMember);
    return savedMember;
  }

  private void saveVector(Member member) {
    CreateVectorDto vectorDto = CreateVectorDto.builder().memberId(member.getId()).build();
    vectorService.save(vectorDto);
  }

  public AuthToken reissue(KakaoReissueParams params) {

    String refreshToken = params.getRefreshToken();

    Long memberId = jwtTokenProvider.parseRefreshToken(refreshToken);

    if (!refreshToken.equals(redisTemplate.opsForValue().get(memberId.toString()))) {
      throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
    }

    Member member =
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
