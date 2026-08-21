package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(String name, String email, String password, @Nullable UUID profileId) {
        validateNameAndEmailAvailable(name, email);
        return userRepository.create(new User(name, email, password, profileId));
    }

    public User findById(UUID id) {
        return validateExistsAndThenFindById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User deleteById(UUID id) {
        return userRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsAllByIds(List<UUID> ids) {
        return userRepository.existsAllByIds(ids);
    }

    public boolean existsByNameOrEmail(String name, String email) {
        return userRepository.existsByNameOrEmail(name, email);
    }

    public User findByNameAndPassword(String name, String password) {
        return userRepository.findByNameAndPassword(name, password)
                .orElseThrow(() -> new CustomException(ExceptionType.LOGIN_FAILED));
    }

    public User update(UUID id, String name, String email, String password, @Nullable UUID profileId) {
        validateNameAndEmailAvailable(name, email);
        return validateExistsAndThenFindById(id).update(name, email, password, profileId);
    }

    public void validateExistsByName(String name) {
        if(!userRepository.existsByName(name)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }
    }

    public void validateAllExists(List<UUID> userIds) {
        userIds.stream()
                .filter(userId -> !userRepository.existsById(userId))
                .findAny()
                .ifPresent(id -> {
                    throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
                });
    }

    public User validateExistsAndThenFindById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
    }

    public void validateNameAndEmailAvailable(String name, String email) {
        if (userRepository.existsByNameOrEmail(name, email)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }
    }
}
