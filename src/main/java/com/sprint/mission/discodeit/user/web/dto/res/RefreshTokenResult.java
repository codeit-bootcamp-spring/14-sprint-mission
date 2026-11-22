package com.sprint.mission.discodeit.user.web.dto.res;

public record RefreshTokenResult(
    JwtResponse jwtResponse,
    String refreshToken
) {
  public static RefreshTokenResult of(JwtResponse jwtResponse, String refreshToken){
    return new RefreshTokenResult(
        jwtResponse,
        refreshToken
    );
  }
}
