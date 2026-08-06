package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record UserCreateRequestDto(
    @NotBlank
    String name,
    @NotBlank
    String password,
    @NotBlank
    String email,
    MultipartFile profileImage) {

}
