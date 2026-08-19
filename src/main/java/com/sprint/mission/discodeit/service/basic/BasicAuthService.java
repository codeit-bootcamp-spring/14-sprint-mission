package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public UserDto login(String name, String password) {
        User retrieved = userRepository.findByNameAndPassword(name, password)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
        UserStatus userStatus = userStatusRepository.findByUserId(retrieved.getId())
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));
        userStatus.updateLastSeenAt();
        return UserDto.of(retrieved, userStatus);
    }
}
