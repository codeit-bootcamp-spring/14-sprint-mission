package com.sprint.mission.discodeit.user.application.dto;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
