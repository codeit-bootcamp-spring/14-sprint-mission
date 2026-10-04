package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.user.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User creating) {
        validateNameAndEmailAvailable(creating.getUsername(), creating.getEmail());
        return userRepository.save(creating);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
    }

    public List<User> findByIds(List<UUID> userIds) {
        return userRepository.findByIdIn(userIds);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(UUID id) {
        User deleting = findById(id);
        userRepository.delete(deleting);
    }

    public User findByNameAndPassword(String name, String password) {
        validateExistsByName(name);
        return userRepository.findByUsernameAndPassword(name, password)
                .orElseThrow(() -> new CustomException(ExceptionType.LOGIN_FAILED));
    }

    public User update(User updating, String username, String email, String password, @Nullable BinaryContent profile) {
        validateUsernameAndEmailAvailableWhereUserNot(username, email, updating.getId());
        return updating.update(username, email, password, profile);
    }


    public User updateNewLastActiveAt(UUID id, Instant newLastActiveAt) {
        User updating = findById(id);
        return updating.updateLastActiveAt(newLastActiveAt);
    }

    public void validateExistsById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }
    }

    private void validateExistsByName(String name) {
        if(!userRepository.existsByUsername(name)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }
    }

    private void validateNameAndEmailAvailable(String name, String email) {
        if (userRepository.existsByUsernameOrEmail(name, email)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }
    }

    private void validateUsernameAndEmailAvailableWhereUserNot(String username, String email, UUID id) {
        if(userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }
    }

    public void flush() {
        userRepository.flush();
    }
}
