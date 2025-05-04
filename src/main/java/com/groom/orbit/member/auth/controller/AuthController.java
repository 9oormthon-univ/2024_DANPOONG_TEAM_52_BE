package com.groom.orbit.member.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.exception.BaseResponse;
import com.groom.orbit.member.auth.app.AuthService;
import com.groom.orbit.member.auth.app.oauth.AuthToken;
import com.groom.orbit.member.auth.app.oauth.kakao.KakaoReissueParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/reissue")
  public BaseResponse<AuthToken> reissue(@RequestBody KakaoReissueParams params) {
    return BaseResponse.onSuccess(authService.reissue(params));
  }
}
