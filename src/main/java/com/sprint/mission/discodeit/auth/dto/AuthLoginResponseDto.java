package com.sprint.mission.discodeit.auth.dto;

public record AuthLoginResponseDto(
        String username,
        String email
) {
    public static AuthLoginResponseDto from(String username,  String email){

        return new AuthLoginResponseDto(username, email);
    }
}
