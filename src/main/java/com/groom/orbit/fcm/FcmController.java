package com.groom.orbit.fcm;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;

import com.groom.orbit.common.annotation.AuthMember;
import com.groom.orbit.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fcm")
public class FcmController {

  private final FcmService fcmService;

  @PostMapping("/test")
  public ResponseDto<Integer> pushMessage(@RequestBody FcmSendRequestDto requestDto)
      throws IOException {
    return ResponseDto.ok(fcmService.sendMessageTo(requestDto));
  }

  @PatchMapping
  public void saveFcmTokenToMember(
      @AuthMember Long memberId, @RequestBody SaveFcmTokenRequestDto requestDto) {
    fcmService.saveFcmToken(memberId, requestDto);
  }
}
