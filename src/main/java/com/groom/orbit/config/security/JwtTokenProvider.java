package com.groom.orbit.config.security;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  private final Key key;
  private final long accessTokenValidityMilliseconds;
  private final long refreshTokenValidityMilliseconds;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.access-token-validity}") final long accessTokenValidityMilliseconds,
      @Value("${jwt.refresh-token-validity}") final long refreshTokenValidityMilliseconds) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenValidityMilliseconds = accessTokenValidityMilliseconds;
    this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
  }

  public String generate(Long userId, long validityMilliseconds) {
    Claims claims = Jwts.claims();

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(validityMilliseconds / 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userId.toString())
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
        .setIssuer("Orbit")
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateAccessToken(Long userId) {
    return generate(userId, accessTokenValidityMilliseconds);
  }

  public String generateRefreshToken(Long userId) {
    return generate(userId, refreshTokenValidityMilliseconds);
  }

  public String extractSubject(String accessToken) {
    Claims claims = parseClaims(accessToken);
    return claims.getSubject();
  }

  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  private Jws<Claims> getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }

  public boolean isTokenValid(String token) {
    try {
      Jws<Claims> claims = getClaims(token);
      Date expiredDate = claims.getBody().getExpiration();
      return expiredDate.after(new Date());
    } catch (ExpiredJwtException e) {
      throw new RuntimeException();
    } catch (SecurityException
        | MalformedJwtException
        | UnsupportedJwtException
        | IllegalArgumentException e) {
      throw new RuntimeException();
    }
  }

  public Long getSubject(String token) {
    return Long.valueOf(getClaims(token).getBody().getSubject());
  }
}
