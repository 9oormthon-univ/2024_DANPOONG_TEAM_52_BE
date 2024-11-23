package com.groom.orbit.config.security;

import static com.groom.orbit.config.security.SecurityConst.ALLOWED_URLS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.groom.orbit.config.security.filter.JwtAuthExceptionHandlingFilter;
import com.groom.orbit.config.security.filter.JwtRequestFilter;
import com.groom.orbit.config.security.handler.JwtAccessDeniedHandler;
import com.groom.orbit.config.security.handler.JwtAuthenticationEntryPoint;
import com.groom.orbit.config.web.CorsConfig;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CorsConfig corsConfig;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtRequestFilter jwtRequestFilter;
  private final JwtAuthExceptionHandlingFilter jwtAuthExceptionHandlingFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.formLogin(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);

    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    http.sessionManagement(
        sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));

    http.exceptionHandling(
        (configurer ->
            configurer
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)));

    http.authorizeHttpRequests(
        (authorize) ->
            authorize
                .requestMatchers(ALLOWED_URLS.toArray(new String[0]))
                .permitAll()
                .anyRequest()
                .authenticated());

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthExceptionHandlingFilter, JwtRequestFilter.class);

    return http.build();
  }
}
