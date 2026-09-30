package com.sprint.mission.discodeit.user.domain.service;

import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void validateUsernameNotDuplicated(String username) {
        if (userRepository.existsByName(username)) {
            throw new CustomException(CustomErrorCode.USER_DUPLICATE_EMAIL);
        }
    }

    @Override
    public void validateEmailNotDuplicated(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(CustomErrorCode.USER_DUPLICATE_EMAIL);
        }
    }
}
