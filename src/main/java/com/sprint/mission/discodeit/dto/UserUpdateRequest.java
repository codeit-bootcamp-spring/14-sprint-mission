package com.sprint.mission.discodeit.dto;

/**
 * 수정 대상 id는 성격이 다른 정보라 메소드 파라미터로 따로 받는다.
 */
public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
