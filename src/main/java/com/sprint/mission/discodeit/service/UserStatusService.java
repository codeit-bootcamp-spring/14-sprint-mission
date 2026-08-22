package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;

    public UserStatus create(UserStatus userStatus) {
        return userStatusRepository.create(userStatus);
    }

    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
    }

    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));
    }

    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    public UserStatus updateLastActiveAtByUserId(UUID userId, Instant newLastActiveAt) {
        validateExistsByUserId(userId);
        return userStatusRepository.updateLastActiveAtByUserId(userId, newLastActiveAt);
    }

    public UserStatus deleteById(UUID id) {
        validateExists(id);
        return userStatusRepository.deleteById(id);
    }

    public void validateExists(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE);
        }
    }

    public void validateExistsByUserId(UUID userId) {
        if (!userStatusRepository.existsByUserId(userId)) {
            throw new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE);
        }
    }

    public void validateUserIdAvailable(UUID userId) {
        if (userStatusRepository.existsByUserId(userId)) {
            throw new CustomException(ExceptionType.USERSTATUS_ALREADY_EXISTS);
        }
    }
}
