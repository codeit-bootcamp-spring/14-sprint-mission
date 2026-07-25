package com.sprint.mission.discodeit.service.validator;

import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public void validate(UUID userId) {
        if (isNotExistingUser(userId)) {
            throw new IllegalArgumentException(String.format("repository에 존재하지 않는 User, userId=%s", userId));
        }
    }

    @Override
    public void validateAll(List<UUID> userIds) {
        userIds.forEach(this::validate);
    }

    private boolean isNotExistingUser(UUID id) {
        return !userRepository.existsById(id);
    }
}
