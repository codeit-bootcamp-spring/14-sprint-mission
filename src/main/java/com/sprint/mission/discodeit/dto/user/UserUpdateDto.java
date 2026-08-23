package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.UUID;

@Value
public class UserUpdateDto {
    @NotBlank(message = "유저 갱신 시 name이 꼭 필요합니다.")
    String name;
    @NotBlank(message = "유저 갱신 시 name이 꼭 필요합니다.")
    String email;
    @NotBlank(message = "유저 갱신 시 name이 꼭 필요합니다.")
    String password;

    UUID profileId;
}
