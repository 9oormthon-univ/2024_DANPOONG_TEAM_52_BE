package com.groom.orbit.common.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(Long.class)
        && parameter.hasParameterAnnotation(AuthMember.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    if (principal == null || principal.getClass() == String.class) {
      throw new CommonException(ErrorCode.NOT_FOUND_MEMBER);
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        (UsernamePasswordAuthenticationToken) authentication;
    return Long.parseLong(authenticationToken.getName());
  }
}
