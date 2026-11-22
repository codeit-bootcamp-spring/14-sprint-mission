package com.sprint.mission.discodeit.global.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.global.security.exception.InvalidTokenException;
import com.sprint.mission.discodeit.user.domain.entity.Role;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Component;

/*
  1. 필터에서 로그인 시 generateToken 2개 호출
    1-1. 성공하면 토큰 생성 후 헤더 넣기
  2. 다음 요청부터 헤더 토큰 확인 후 토큰 유효성 검증
    2-1. 만료됐으면 실패 처리 > 프론트에서 쿠키 리프레쉬 토큰으로 재요청
    2-2. 리프레쉬 토큰 호출하여 새로운 액세스 토큰 발급
 */
@Component
public class JwtTokenProvider {

  private static final String TOKEN_TYPE = "token_type";
  private static final String ACCESS = "access";
  private static final String REFRESH = "refresh";

  private final JwtProperties properties;
  private final byte[] secretKey;

  // 스프링에서 프로퍼티스 주입
  public JwtTokenProvider(JwtProperties properties) {
    if (properties.secretKey() == null || properties.secretKey().getBytes(StandardCharsets.UTF_8).length < 32) {
      throw new IllegalArgumentException("JWT secret key must contain at least 32 bytes");
    }
    if (properties.accessTokenExpireTime() <= 0 || properties.refreshTokenExpireTime() <= 0) {
      throw new IllegalArgumentException("JWT expiration times must be positive");
    }
    this.properties = properties;
    this.secretKey = properties.secretKey().getBytes(StandardCharsets.UTF_8);
  }

  // 핸들러에서 성공 로직 후 호출
  public String generateAccessToken(UUID userId, Role role) {
    return generateToken(userId, ACCESS, properties.accessTokenExpireTime(), role);
  }

  public String generateRefreshToken(UUID userId) {
    return generateToken(userId, REFRESH, properties.refreshTokenExpireTime(), null);
  }

  // 인증 > 토큰에서 인증정보 확보
  public JwtPrincipal getPrincipalFromAccessToken(String token) {
    JWTClaimsSet claims = getClaims(token, ACCESS);
    try {
      String role = claims.getStringClaim("role");
      if (role == null) {
        throw new InvalidTokenException("JWT 역할이 없습니다.");
      }
      return new JwtPrincipal(UUID.fromString(claims.getSubject()), Role.valueOf(role));
    } catch (ParseException | IllegalArgumentException e) {
      throw new InvalidTokenException("JWT 사용자 정보가 유효하지 않습니다.", e);
    }
  }

  public UUID getUserIdFromRefreshToken(String token) {
    return UUID.fromString(getClaims(token, REFRESH).getSubject());
  }

  private String generateToken(UUID userId, String type, long expirationMillis, Role role) {
    Instant now = Instant.now();
    JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
        .subject(userId.toString())
        .issueTime(Date.from(now))
        .expirationTime(Date.from(now.plusMillis(expirationMillis)))
        .claim(TOKEN_TYPE, type);
    if (role != null) {
      claims.claim("role", role.name());
    }

    SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims.build());
    try {
      jwt.sign(new MACSigner(secretKey));
      return jwt.serialize();
    } catch (JOSEException e) {
      throw new IllegalStateException("JWT 인증 실패", e);
    }
  }

  // 토큰(액세스,리프레시) 에서 유효한 유저 정보 추출
  private JWTClaimsSet getClaims(String token, String expectedType) {
    if (token == null || token.isBlank()) {
      throw new InvalidTokenException("JWT 토큰 값이 유효하지 않습니다.");
    }
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      // 시그니처 변조 + 헤더 페이로드 검증 => 대칭키 방식(시크릿키)
      if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm()) || !jwt.verify(new MACVerifier(secretKey))) {
        throw new InvalidTokenException("JWT 서명이 올바르지 않습니다.");
      }
      JWTClaimsSet claims = jwt.getJWTClaimsSet();
      Date expiration = claims.getExpirationTime();         // 토큰 만료됐는지 체킹
      if (!expectedType.equals(claims.getStringClaim(TOKEN_TYPE))
          || expiration == null || !expiration.toInstant().isAfter(Instant.now())
          || claims.getSubject() == null) {
        throw new InvalidTokenException("JWT");
      }
      UUID.fromString(claims.getSubject());
      return claims;
    } catch (ParseException | JOSEException | IllegalArgumentException e) {
      throw new InvalidTokenException("JWT 로직에서 실패 발생", e);
    }
  }
}
