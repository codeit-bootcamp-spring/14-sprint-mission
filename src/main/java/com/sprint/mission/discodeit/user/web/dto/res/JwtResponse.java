package com.sprint.mission.discodeit.user.web.dto.res;

public record JwtResponse(
    UserResponseDTO userDto,
    String accessToken
) {
  public static JwtResponse of(UserResponseDTO responseDTO, String accessToken){
    return new JwtResponse(
        responseDTO,
        accessToken
    );
  }
}
