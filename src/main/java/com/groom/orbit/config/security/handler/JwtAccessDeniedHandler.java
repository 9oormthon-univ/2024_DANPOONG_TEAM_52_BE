package com.groom.orbit.config.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.orbit.common.exception.BaseResponse;
import com.groom.orbit.common.exception.BaseResponseStatus;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(HttpStatus.FORBIDDEN.value());

    System.out.println("Access denied" + request.getRequestURI());
    accessDeniedException.printStackTrace();

    BaseResponse<Object> errorResponse = BaseResponse.onFailure(BaseResponseStatus.NO_USER, null);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
