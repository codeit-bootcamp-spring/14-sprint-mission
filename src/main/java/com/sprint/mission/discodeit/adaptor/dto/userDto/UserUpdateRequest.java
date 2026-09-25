package com.sprint.mission.discodeit.adaptor.dto.userDto;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
