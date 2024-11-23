package com.groom.orbit.config.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.orbit.common.exception.BaseResponse;
import com.groom.orbit.common.exception.BaseResponseStatus;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    BaseResponse<Object> errorResponse = BaseResponse.onFailure(BaseResponseStatus.NO_USER, null);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
