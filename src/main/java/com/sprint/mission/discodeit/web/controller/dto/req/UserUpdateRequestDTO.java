package com.sprint.mission.discodeit.web.controller.dto.req;


public record UserUpdateRequestDTO(
    String newUserName,
    String newEmail,
    String newPassword
) {}
