package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.Role;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserException;
import com.sprint.mission.discodeit.domain.user.UserExceptionType;
import com.sprint.mission.discodeit.application.user.out.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String password, @Nullable BinaryContent profile) {
        validateNameAndEmailAvailable(username, email);
        User creating = User.create(username, email, passwordEncoder.encode(password), profile);
        return userRepository.save(creating);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(
                                UserExceptionType.USER_NOT_FOUND,
                                Map.of("id", id)
                        )
                );
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
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UserException(UserExceptionType.LOGIN_FAILED, Map.of("name", name)));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserExceptionType.LOGIN_FAILED, Map.of("name", name));
        }
        return user;
    }

    public User update(UUID id, String username, String email, String password, @Nullable BinaryContent profile) {
        validateUsernameAndEmailAvailableWhereUserNot(username, email, id);
        User updating = findById(id);
        String encodedPassword = password == null ? null : passwordEncoder.encode(password);
        return updating.update(username, email, encodedPassword, profile);
    }


    public User updateNewLastActiveAt(UUID id, Instant newLastActiveAt) {
        User updating = findById(id);
        return updating.updateLastActiveAt(newLastActiveAt);
    }

    public void validateExistsById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND, Map.of("id", id));
        }
    }

    private void validateNameAndEmailAvailable(String name, String email) {
        if (userRepository.existsByUsernameOrEmail(name, email)) {
            throw new UserException(
                    UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of(
                            "username,", name,
                            "email", email
                    )
            );
        }
    }

    private void validateUsernameAndEmailAvailableWhereUserNot(String username, String email, UUID id) {
        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new UserException(
                    UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of(
                            "username", username,
                            "id", id
                    )
            );
        }
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new UserException(
                    UserExceptionType.USER_UNIQUE_FIELD_CONFLICT,
                    Map.of(
                            "email", email,
                            "id", id
                    )
            );
        }
    }

    public void flush() {
        userRepository.flush();
    }

    public void createBootstrapAdmin(String username, String email, String password) {
        if (notExistsByRole(Role.ADMIN)) {
            User newAdmin = User.createAdmin(username, email, passwordEncoder.encode(password));
            userRepository.save(newAdmin);
        }
    }

    private boolean notExistsByRole(Role role) {
        return !userRepository.existsByRole(role);
    }
}
