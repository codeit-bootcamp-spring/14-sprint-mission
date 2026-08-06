package com.sprint.mission.discodeit.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateRequestDto(
    String name,
    String password,
    String email,
    MultipartFile profileImage) {

}
