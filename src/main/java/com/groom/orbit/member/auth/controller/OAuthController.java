package com.groom.orbit.member.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.orbit.common.exception.BaseResponse;
import com.groom.orbit.member.auth.app.AuthService;
import com.groom.orbit.member.auth.app.dto.LoginResponseDto;
import com.groom.orbit.member.auth.app.oauth.kakao.KakaoLoginParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {

  private final AuthService authService;

  @PostMapping("/kakao")
  public BaseResponse<LoginResponseDto> loginKakao(@RequestBody KakaoLoginParams params) {
    return BaseResponse.onSuccess(authService.login(params));
  }
}
