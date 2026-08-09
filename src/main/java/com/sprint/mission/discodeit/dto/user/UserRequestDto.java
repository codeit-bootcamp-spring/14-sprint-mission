package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record UserRequestDto(
        String userName,
        String email,
        String password,
        @Nullable
        byte[] data){

}
