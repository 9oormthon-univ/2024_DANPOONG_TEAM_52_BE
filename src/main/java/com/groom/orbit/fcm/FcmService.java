package com.groom.orbit.fcm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmService {

  private final MemberQueryService memberQueryService;
  private final MemberRepository memberRepository;

  @Value("${fcm.fcm-url}")
  private final String fcmUrl;

  public FcmService(
      @Value("${fcm.fcm-url}") final String fcmUrl,
      MemberQueryService memberQueryService,
      MemberRepository memberRepository) {
    this.fcmUrl = fcmUrl;
    this.memberQueryService = memberQueryService;
    this.memberRepository = memberRepository;
  }

  public void saveFcmToken(Long memberId, SaveFcmTokenRequestDto requestDto) {

    Member member = memberQueryService.findMember(memberId);

    member.setFcmToken(requestDto.fcmToken());

    memberRepository.save(member);
  }

  public Integer sendMessageTo(FcmSendRequestDto requestDto) throws IOException {

    String message = makeMessage(requestDto);

    RestTemplate restTemplate = new RestTemplate();

    restTemplate
        .getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + getGoogleAccessToken());

    HttpEntity entity = new HttpEntity<>(message, headers);

    String API_URL = fcmUrl;
    ResponseEntity response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

    System.out.println(response.getStatusCode());

    return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
  }

  private String getGoogleAccessToken() throws IOException {
    String fireBaseConfigPath = "firebase/orbit-fcm.json";

    GoogleCredentials googleCredentials =
        GoogleCredentials.fromStream(new ClassPathResource(fireBaseConfigPath).getInputStream())
            .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

    googleCredentials.refreshIfExpired();

    return googleCredentials.getAccessToken().getTokenValue();
  }

  private String makeMessage(FcmSendRequestDto requestDto) throws JsonProcessingException {

    ObjectMapper om = new ObjectMapper();

    Notification notification = Notification.from(requestDto);

    Message message = Message.of(notification, requestDto.fcmToken());

    FcmMessageDto fcmMessageDto = FcmMessageDto.of(false, message);

    return om.writeValueAsString(fcmMessageDto);
  }
}
