package com.sprint.mission.discodeit.service.domain.user;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Qualifier("userService")
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;

    public UserDomainServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateUnique(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ExceptionType.USER_USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ExceptionType.USER_EMAIL_EXISTS);
        }
    }

    @Override
    public User create(User user) {
        validateUnique(user.getUsername(), user.getEmail());

        return userRepository.save(user);
    }

    @Override
    public User findById(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND, userId));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(
            UUID userId,
            User userUpdates
    ) {
        User originalUser = findById(userId);
        validateUnique(
                originalUser,
                userUpdates.getUsername(),
                userUpdates.getEmail()
        );

        // user 업데이트
        originalUser.updateAccountDetails(userUpdates);

        return userRepository.save(originalUser);
    }

    private void validateUnique(
            User originalUser,
            String username,
            String email
    ) {
        boolean usernameChanged = !Objects.equals(
                originalUser.getUsername(),
                username
        );
        boolean emailChanged = !Objects.equals(
                originalUser.getEmail(),
                email
        );

        if (usernameChanged && userRepository.existsByUsername(username)) {
            throw new CustomException(ExceptionType.USER_USERNAME_EXISTS);
        }
        if (emailChanged && userRepository.existsByEmail(email)) {
            throw new CustomException(ExceptionType.USER_EMAIL_EXISTS);
        }
    }

    @Override
    public void delete(UUID userId) {
        findById(userId);
        userRepository.delete(userId);
    }
}
