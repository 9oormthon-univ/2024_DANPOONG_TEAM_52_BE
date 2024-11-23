package com.groom.orbit.config.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.config.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtAuthProvider;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader != null) {
      String token = authorizationHeader;

      if (jwtAuthProvider.isTokenValid(token)) {
        Long userId = jwtAuthProvider.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

        if (userDetails != null) {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, "", userDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
          throw new CommonException(ErrorCode.NOT_FOUND_MEMBER);
        }
      } else {
        throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
      }
    }
    filterChain.doFilter(request, response);
  }
}
