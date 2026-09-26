package com.sprint.mission.discodeit.application.user.dto;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
